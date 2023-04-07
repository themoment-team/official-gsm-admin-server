package com.themoment.officialgsm.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.themoment.officialgsm.domain.board.dto.FileDto;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3 amazonS3;

    public List<FileDto> upload(List<MultipartFile> multipartFiles) {
        List<FileDto> fileDtoList = new ArrayList<>();

        if(multipartFiles == null) {
            return fileDtoList;
        }

        for (MultipartFile file : multipartFiles) {
            String originalFileName = file.getOriginalFilename();
            String fileName = createFileName(originalFileName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                FileDto fileDto = new FileDto(
                        getDomainUrl(amazonS3.getUrl(bucket, fileName).toString()),
                        originalFileName,
                        convertToConstant(getFileExtension(originalFileName))
                );
                fileDtoList.add(fileDto);
            } catch(IOException e) {
                throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }
        return fileDtoList;
    }

    private String convertToConstant(String fileExtension) {
        return fileExtension.replace(".","").toUpperCase();
    }

    private String createFileName(String filename) {
        return UUID.randomUUID().toString().concat(getFileExtension(filename));
    }

    private String getFileExtension(String filename){
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException(ErrorCode.WRONG_INPUT_FILE);
        }
    }

    private String getDomainUrl(String filePath) {    // 추후 domainUrl 수정 !!
        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/", bucket, region);
        String domainUrl = "https://my-domain.com/";

        String key = filePath.replace(s3Url, "");
        return domainUrl + key;
    }

    public void deleteS3(String deleteFileUrl){      // 추후 domainUrl 수정 !!
        String fileName = deleteFileUrl.replace("https://my-domain.com/", "");
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

}
