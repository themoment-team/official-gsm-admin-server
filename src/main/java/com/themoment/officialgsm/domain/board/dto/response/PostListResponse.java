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

    private String postTitle;
    private String postWriter;
    private Category category;
    private LocalDateTime createdAt;
    private Boolean FileIsExist;

    public static PostListResponse toDto(Post post) {
        return PostListResponse.builder()
                .postTitle(post.getPostTitle())
                .postWriter(post.getUser().getUserName())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .FileIsExist(post.getFiles()!=null)
                .build();
    }
}
