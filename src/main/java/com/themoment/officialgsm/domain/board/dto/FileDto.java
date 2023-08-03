package com.themoment.officialgsm.domain.board.dto;

import com.themoment.officialgsm.domain.board.entity.file.FileExtension;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private String fileUrl;
    private String fileName;
    private FileExtension fileExtension;
}
