package com.themoment.officialgsm.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private String fileUrl;

    private String fileName;

    private String type;
}
