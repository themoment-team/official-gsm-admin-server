package com.themoment.officialgsm.domain.board.service;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.board.dto.FileDto;
import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.dto.request.ModifyPostRequest;
import com.themoment.officialgsm.domain.board.dto.response.PostListResponse;
import com.themoment.officialgsm.domain.board.entity.file.File;
import com.themoment.officialgsm.domain.board.entity.file.FileExtension;
import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import com.themoment.officialgsm.domain.board.repository.FileRepository;
import com.themoment.officialgsm.domain.board.repository.PostRepository;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.util.AwsS3Util;
import com.themoment.officialgsm.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final CurrentUserUtil currentUserUtil;
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final AwsS3Util awsS3Util;

    @Transactional(readOnly = true)
    public Page<PostListResponse> findPostList(int pageNumber, Category category) {
        Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("createdAt").descending());   // pageSize는 추후 수정
        Page<Post> postList = postRepository.findAllByCategory(pageable, category);

        return postList.map(PostListResponse::from);
    }

    @Transactional
    public void addPost(AddPostRequest addPostRequest, List<MultipartFile> multipartFiles) {
        User user = currentUserUtil.CurrentUser();
        Post post = Post.builder()
                .postTitle(addPostRequest.getPostTitle())
                .postContent(addPostRequest.getPostContent())
                .category(addPostRequest.getCategory())
                .user(user)
                .build();

        postRepository.save(post);
        saveFiles(post, multipartFiles);
    }

    @Transactional
    public void modifyPost(Long postSeq, ModifyPostRequest modifyPostRequest, List<MultipartFile> multipartFiles) {
        Post post = postRepository.findById(postSeq)
                .orElseThrow(() -> new CustomException("게시글 수정 과정에서 게시글을 찾지 못하였습니다.", HttpStatus.NOT_FOUND));

        post.update(
                modifyPostRequest.getPostTitle(),
                modifyPostRequest.getPostContent(),
                modifyPostRequest.getCategory()
                );

        List<String> deleteFileUrls = modifyPostRequest.getDeleteFileUrl();
        if (!deleteFileUrls.isEmpty()) {
            deleteS3Files(deleteFileUrls);
        }

        saveFiles(post, multipartFiles);
    }

    @Transactional
    public void removePost(Long postSeq) {
        Post post = postRepository.findById(postSeq)
                .orElseThrow(() -> new CustomException("게시글 삭제 과정에서 게시글을 찾지 못하였습니다.", HttpStatus.NOT_FOUND));

        deletePostFiles(post.getFiles());
        postRepository.delete(post);
    }

    private void saveFiles(Post post, List<MultipartFile> multipartFiles) {
        List<FileDto> fileDtoList = awsS3Util.upload(multipartFiles);

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

        fileRepository.saveAll(fileList);
    }

    private void deleteS3Files(List<String> deleteFileUrls) {
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
