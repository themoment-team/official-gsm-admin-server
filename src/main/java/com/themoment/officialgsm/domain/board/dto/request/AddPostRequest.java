package com.themoment.officialgsm.domain.board.dto.request;

import com.themoment.officialgsm.domain.board.entity.post.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPostRequest {

    private String postTitle;
    private String postContent;
    private Category category;
}
