package com.themoment.officialgsm.domain.board.dto.request;

import com.themoment.officialgsm.domain.board.entity.post.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddPostRequest {

    private String postTitle;
    private String postContent;
    private Category category;
}
