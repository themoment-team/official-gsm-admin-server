package com.themoment.officialgsm.domain.board.dto.request;

import com.themoment.officialgsm.domain.board.entity.post.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyPostRequest {

    @NotNull
    @Length(min = 5, max = 60)
    private String postTitle;

    @NotNull
    @Length(max = 5000)
    private String postContent;

    @NotNull
    private Category category;

    @NotNull
    private List<String> deleteFileUrl;
}
