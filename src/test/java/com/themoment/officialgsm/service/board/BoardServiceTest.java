package com.themoment.officialgsm.service.board;

import com.themoment.officialgsm.domain.auth.entity.user.Role;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.auth.repository.UserRepository;
import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.dto.request.ModifyPostRequest;
import com.themoment.officialgsm.domain.board.entity.file.File;
import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import com.themoment.officialgsm.domain.board.repository.FileRepository;
import com.themoment.officialgsm.domain.board.repository.PostRepository;
import com.themoment.officialgsm.domain.board.service.BoardService;
import com.themoment.officialgsm.global.util.AwsS3Util;
import com.themoment.officialgsm.global.util.UserUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FileRepository fileRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private BoardService boardService;
    @Autowired
    private AwsS3Util awsS3Util;

    private AddPostRequest getAddPostRequest() {
        return AddPostRequest.builder()
                .postTitle("제목")
                .postContent("내용")
                .category(Category.NOTICE)
                .build();
    }

    private MockMultipartFile getMockMultipartFile() {
        return new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Some data for the .jpg file".getBytes()
        );
    }

    @BeforeEach
    void setUp() {

        // given
        User user = User.builder()
                .oauthId("1234567")
                .userName("최장우")
                .userEmail("s22018@gsm.hs.kr")
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);
        fileRepository.deleteAll();
        postRepository.deleteAll();

        em.flush();
        em.clear();

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getOauthId(), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        // when
        User currentUser = userUtil.getCurrentUser();

        // then
        assertNotNull(currentUser);
    }

    @AfterEach
    void clearS3() {

        List<File> fileList = fileRepository.findAll();
        if (fileList.isEmpty()) {
            return;
        }

        List<String> fileUrlList = fileList.stream().map(File::getFileUrl).toList();

        awsS3Util.deleteS3(fileUrlList);
    }

    private void savePost(AddPostRequest addPostRequest, MockMultipartFile file) {
        boardService.addPost(addPostRequest, List.of(file));

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("게시물 등록")
    void addPost() {

        // given
        AddPostRequest addPostRequest = getAddPostRequest();
        MockMultipartFile file = getMockMultipartFile();

        savePost(addPostRequest, file);

        // when
        Post post = postRepository.findAll().get(0);

        //then
        assertThat(post.getPostTitle()).isEqualTo(addPostRequest.getPostTitle());
        assertThat(post.getPostContent()).isEqualTo(addPostRequest.getPostContent());
        assertThat(post.getCategory()).isEqualTo(addPostRequest.getCategory());

        assertThat(post.getFiles().get(0).getFileName()).isEqualTo(file.getOriginalFilename());
    }

    @Test
    @DisplayName("게시물 수정")
    void modifyPost() {

        // given
        AddPostRequest addPostRequest = getAddPostRequest();
        MockMultipartFile file = getMockMultipartFile();

        savePost(addPostRequest, file);

        ModifyPostRequest modifyPostRequest = ModifyPostRequest.builder()
                .postTitle("변경된 제목")
                .postContent("변경된 내용")
                .category(Category.FAMILY_NEWSLETTER)
                .deleteFileUrl(List.of(postRepository.findAll().get(0).getFiles().get(0).getFileUrl()))
                .build();

        Long postSeq = postRepository.findAll().get(0).getPostSeq();

        boardService.modifyPost(postSeq, modifyPostRequest, List.of(file, file, file));

        em.flush();
        em.clear();

        // when
        Post modifiedPost = postRepository.findAll().get(0);

        // then
        assertThat(modifiedPost.getPostTitle()).isEqualTo(modifyPostRequest.getPostTitle());
        assertNotEquals(modifiedPost.getCreatedAt(), modifiedPost.getModifiedAt());
        assertThat(modifiedPost.getFiles().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("게시물 삭제")
    void removePost() {

        // given
        AddPostRequest addPostRequest = getAddPostRequest();
        MockMultipartFile file = getMockMultipartFile();

        savePost(addPostRequest,file);

        Long postSeq = postRepository.findAll().get(0).getPostSeq();
        boardService.removePost(postSeq);

        // then
        assertThat(postRepository.findAll().size()).isEqualTo(0);
        assertThat(fileRepository.findAll().size()).isEqualTo(0);
    }

}
