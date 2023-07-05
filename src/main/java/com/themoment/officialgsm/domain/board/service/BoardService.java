package com.themoment.officialgsm.domain.board.service;

import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.board.dto.FileDto;
import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.dto.request.ModifyPostRequest;
import com.themoment.officialgsm.domain.board.entity.file.File;
import com.themoment.officialgsm.domain.board.entity.file.FileExtension;
import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.entity.post.PinnedPost;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import com.themoment.officialgsm.domain.board.repository.FileBulkRepository;
import com.themoment.officialgsm.domain.board.repository.FileRepository;
import com.themoment.officialgsm.domain.board.repository.PinnedPostRepository;
import com.themoment.officialgsm.domain.board.repository.PostRepository;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.util.AwsS3Util;
import com.themoment.officialgsm.global.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final UserUtil currentUserUtil;
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final FileBulkRepository fileBulkRepository;
    private final PinnedPostRepository pinnedPostRepository;
    private final AwsS3Util awsS3Util;

    public void addPost(AddPostRequest addPostRequest, List<MultipartFile> files) {
        if(addPostRequest.getCategory() == Category.EVENT_GALLERY && files == null) {
            throw new CustomException("행사갤러리는 이미지가 필수입니다", HttpStatus.BAD_REQUEST);
        }

        User user = currentUserUtil.getCurrentUser();
        Post post = Post.builder()
                .postTitle(addPostRequest.getPostTitle())
                .postContent(addPostRequest.getPostContent())
                .category(addPostRequest.getCategory())
                .user(user)
                .build();

        postRepository.save(post);
        saveFiles(post, files);
    }

    public void modifyPost(Long postSeq, ModifyPostRequest modifyPostRequest, List<MultipartFile> files) {
        Post post = postRepository.findById(postSeq)
                .orElseThrow(() -> new CustomException("게시글 수정 과정에서 게시글을 찾지 못하였습니다.", HttpStatus.NOT_FOUND));

        post.update(
                modifyPostRequest.getPostTitle(),
                modifyPostRequest.getPostContent(),
                modifyPostRequest.getCategory()
        );

        List<String> deleteFileUrls = modifyPostRequest.getDeleteFileUrl();
        deleteS3Files(deleteFileUrls);

        saveFiles(post, files);
    }

    public void removePost(Long postSeq) {
        Post post = postRepository.findById(postSeq)
                .orElseThrow(() -> new CustomException("게시글 삭제 과정에서 게시글을 찾지 못하였습니다.", HttpStatus.NOT_FOUND));

        deletePostFiles(post.getFiles());
        postRepository.delete(post);
    }

    public void pinPost(Long postSeq) {
        Post post = postRepository.findById(postSeq)
                .orElseThrow(() -> new CustomException("게시글 고정 과정에서 게시글을 찾지 못하였습니다.", HttpStatus.NOT_FOUND));

        PinnedPost pinnedPost = PinnedPost.builder()
                .post(post)
                .build();

        pinnedPostRepository.save(pinnedPost);
    }

    private void saveFiles(Post post, List<MultipartFile> files) {
        List<FileDto> fileDtoList = awsS3Util.uploadList(files);

        List<File> fileList = new ArrayList<>();
        for (FileDto fileDto : fileDtoList) {
            File file = File.builder()
                    .fileUrl(fileDto.getFileUrl())
                    .fileName(fileDto.getFileName())
                    .fileExtension(FileExtension.valueOf(fileDto.getType()))
                    .post(post)
                    .build();

            fileList.add(file);
        }

        fileBulkRepository.saveAll(fileList);
    }

    private void deleteS3Files(List<String> deleteFileUrls) {
        if(deleteFileUrls.isEmpty()) {
            return;
        }

        awsS3Util.deleteS3(deleteFileUrls);
        fileRepository.deleteByFileUrls(deleteFileUrls);
    }

    private void deletePostFiles(List<File> fileList) {
        List<String> deleteFileUrls = new ArrayList<>();
        for (File file : fileList) {
            deleteFileUrls.add(file.getFileUrl());
        }
        deleteS3Files(deleteFileUrls);
    }
}
