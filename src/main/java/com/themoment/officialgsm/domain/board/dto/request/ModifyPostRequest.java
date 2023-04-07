package com.themoment.officialgsm.domain.board.dto.request;

import com.themoment.officialgsm.domain.board.entity.post.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyPostRequest {

    private String postTitle;
    private String postContent;
    private Category category;
    private List<String> deleteFileUrl;
}
