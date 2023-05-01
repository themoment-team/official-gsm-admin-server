package com.themoment.officialgsm.service.board;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.dto.request.ModifyPostRequest;
import com.themoment.officialgsm.domain.board.dto.response.PostListResponse;
import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import com.themoment.officialgsm.domain.board.repository.FileRepository;
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
import org.springframework.data.domain.Page;
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
    private CurrentUserUtil currentUserUtil;
    @Autowired
    private BoardService boardService;

    private AddPostRequest getAddPostRequest() {
        return AddPostRequest.builder()
                .postTitle("제목")
                .postContent("내용")
                .category(Category.NOTICE)
                .build();
    }

    private MockMultipartFile getMockFile() {
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
                .userName("최장우")
                .userId("아이디")
                .userPwd("비밀번호")
                .build();

        userRepository.save(user);

        em.flush();
        em.clear();

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getUserName(),"password");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        // when
        User currentUser = currentUserUtil.CurrentUser();

        // then
        assertNotNull(currentUser);
    }

    @Test
    @DisplayName("게시물 전체 조회")
    void findPosts() {

        // given
        AddPostRequest addPostRequest = getAddPostRequest();
        MockMultipartFile mockFile = getMockFile();

        for(int i = 0; i < 11; i++) {
            boardService.addPost(addPostRequest,List.of(mockFile));
        }

        em.flush();
        em.clear();

        // when
        Page<PostListResponse> postListResponses = boardService.findPostList(0, Category.NOTICE);

        // then
        assertThat(postListResponses.getSize()).isEqualTo(5);
        assertThat(postListResponses.getTotalElements()).isEqualTo(11);
        assertThat(postListResponses.getTotalPages()).isEqualTo(3);

        assertThat(postListResponses.getContent().get(0).getPostTitle()).isEqualTo(addPostRequest.getPostTitle());
        assertThat(postListResponses.getContent().get(0).getFileIsExist()).isTrue();
    }

    @Test
    @DisplayName("게시물 등록")
    void addPost() {

        // given
        AddPostRequest addPostRequest = getAddPostRequest();
        MockMultipartFile mockFile = getMockFile();

        boardService.addPost(addPostRequest, List.of(mockFile));

        em.flush();
        em.clear();

        // when
        Post post = postRepository.findAll().get(0);

        //then
        assertThat(post.getPostTitle()).isEqualTo(addPostRequest.getPostTitle());
        assertThat(post.getPostContent()).isEqualTo(addPostRequest.getPostContent());
        assertThat(post.getCategory()).isEqualTo(addPostRequest.getCategory());

        assertThat(post.getUser().getUserName()).isEqualTo("최장우");
        assertThat(post.getFiles().get(0).getFileName()).isEqualTo(mockFile.getOriginalFilename());
    }

    @Test
    @DisplayName("게시물 수정")
    void modifyPost() {

        // given
        AddPostRequest addPostRequest = getAddPostRequest();
        MockMultipartFile mockFile = getMockFile();

        boardService.addPost(addPostRequest, List.of(mockFile));

        em.flush();
        em.clear();

        ModifyPostRequest modifyPostRequest = ModifyPostRequest.builder()
                .postTitle("변경된 제목")
                .postContent("변경된 내용")
                .category(Category.FAMILY_NEWSLETTER)
                .deleteFileUrl(List.of(postRepository.findAll().get(0).getFiles().get(0).getFileUrl()))
                .build();

        Long postSeq = postRepository.findAll().get(0).getPostSeq();

        boardService.modifyPost(postSeq, modifyPostRequest, List.of(mockFile, mockFile, mockFile));

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
        MockMultipartFile mockFile = getMockFile();

        boardService.addPost(addPostRequest, List.of(mockFile));

        em.flush();
        em.clear();

        Long postSeq = postRepository.findAll().get(0).getPostSeq();
        boardService.removePost(postSeq);

        // then
        assertThat(postRepository.findAll().size()).isEqualTo(0);
        assertThat(fileRepository.findAll().size()).isEqualTo(0);
    }

}
