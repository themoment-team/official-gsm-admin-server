package com.themoment.officialgsm.domain.board.service;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.board.dto.FileDto;
import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.entity.file.File;
import com.themoment.officialgsm.domain.board.entity.file.Type;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import com.themoment.officialgsm.domain.board.repository.FileRepository;
import com.themoment.officialgsm.domain.board.repository.PostRepository;
import com.themoment.officialgsm.global.util.AwsS3Util;
import com.themoment.officialgsm.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final CurrentUserUtil currentUserUtil;
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final AwsS3Util awsS3Util;

    public void addPost(AddPostRequest addPostRequest, List<MultipartFile> multipartFiles) {
        List<FileDto> fileDtoList = awsS3Util.upload(multipartFiles);

        User user = currentUserUtil.CurrentUser();
        Post post = Post.builder()
                .postTitle(addPostRequest.getPostTitle())
                .postContent(addPostRequest.getPostContent())
                .category(addPostRequest.getCategory())
                .user(user)
                .build();
        postRepository.save(post);

        for (FileDto fileDto : fileDtoList) {
            File file = File.builder()
                    .fileUrl(fileDto.getFileUrl())
                    .fileName(fileDto.getFileName())
                    .type(Type.valueOf(fileDto.getType()))
                    .build();
            fileRepository.save(file);
        }
    }
}
