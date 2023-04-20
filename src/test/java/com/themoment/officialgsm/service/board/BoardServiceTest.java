package com.themoment.officialgsm.service.board;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import com.themoment.officialgsm.domain.board.repository.PostRepository;
import com.themoment.officialgsm.domain.board.service.BoardService;
import com.themoment.officialgsm.global.util.CurrentUserUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private CurrentUserUtil currentUserUtil;
    @Autowired
    private BoardService boardService;
    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void setUp() {

        // given
        User user = User.builder()
                .userName("최장우")
                .userId("아이디")
                .userPwd("비밀번호")
                .build();

        // when
        userRepository.save(user);

        em.flush();
        em.clear();

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getUserName(),"password");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        // then
        User currentUser = currentUserUtil.CurrentUser();

        assertNotNull(currentUser);
    }

    @Test
    @DisplayName("게시물 등록")
    void addPost() {

        // given
        AddPostRequest addPostRequest = AddPostRequest.builder()
                .postTitle("제목")
                .postContent("내용")
                .category(Category.NOTICE)
                .build();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Some data for the .jpg file".getBytes()
        );

        // when
        boardService.addPost(addPostRequest, List.of(mockFile));

        em.flush();
        em.clear();

        //then
        Post post = postRepository.findAll().get(0);

        assertThat(post.getPostTitle()).isEqualTo(addPostRequest.getPostTitle());
        assertThat(post.getPostContent()).isEqualTo(addPostRequest.getPostContent());
        assertThat(post.getCategory()).isEqualTo(addPostRequest.getCategory());

        assertThat(post.getUser().getUserName()).isEqualTo("최장우");
        assertThat(post.getFiles().get(0).getFileName()).isEqualTo(mockFile.getOriginalFilename());
    }
}