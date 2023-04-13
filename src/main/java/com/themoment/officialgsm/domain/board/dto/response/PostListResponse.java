package com.themoment.officialgsm.domain.board.dto.response;

import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {

    private Long postSeq;
    private String postTitle;
    private String postWriter;
    private LocalDateTime createdAt;
    private Boolean FileIsExist;

    public static PostListResponse of(Post post) {
        return PostListResponse.builder()
                .postSeq(post.getPostSeq())
                .postTitle(post.getPostTitle())
                .postWriter(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .FileIsExist(post.getFiles()!=null)
                .build();
    }
}
