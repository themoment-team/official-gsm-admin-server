package com.themoment.officialgsm.domain.board.dto.request;

import com.themoment.officialgsm.domain.board.entity.post.Category;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPostRequest {

    @NotNull
    @Length(min = 5, max = 60)
    private String postTitle;

    @NotNull
    @Length(max = 5000)
    private String postContent;

    @NotNull
    private Category category;
}
