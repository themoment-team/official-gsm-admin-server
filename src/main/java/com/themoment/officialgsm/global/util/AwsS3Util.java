package com.themoment.officialgsm.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.themoment.officialgsm.domain.board.dto.FileDto;
import com.themoment.officialgsm.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AwsS3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3 amazonS3;

    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".png", ".mp4", ".hwp", ".pdf", ".xlsx");
    private final String DOMAIN_URL = "http://bucket.ottokeng.site/";   // 추후 수정해야함 !

    public List<FileDto> uploadList(List<MultipartFile> multipartFiles) {
        if(multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }

        List<FileDto> fileDtoList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            fileDtoList.add(upload(file));
        }
        return fileDtoList;
    }

    private FileDto upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileName = createFileName(originalFileName);

        try(InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, extractMetaData(file))
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return new FileDto(
                    getDomainUrl(amazonS3.getUrl(bucket, fileName).toString()),
                    originalFileName,
                    convertToConstant(getFileExtension(originalFileName))
            );
        } catch(IOException e) {
            throw new CustomException("파일 업로드 과정에서 예외가 발생하였습니다.", HttpStatus.BAD_REQUEST, e);
        }
    }

    private ObjectMetadata extractMetaData(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        return objectMetadata;
    }

    private String convertToConstant(String fileExtension) {
        return fileExtension.replace(".","").toUpperCase();
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName){
        try {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                throw new CustomException("파일 확장자 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
            }
            return extension;
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException("파일 확장자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private String getDomainUrl(String filePath) {
        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/", bucket, region);
        String key = filePath.replace(s3Url, "");

        return DOMAIN_URL + key;
    }

    public void deleteS3(List<String> deleteFileUrls){
        List<String> deleteFileKeys = new ArrayList<>();
        for (String deleteFileUrl : deleteFileUrls) {
            deleteFileKeys.add(deleteFileUrl.replace(DOMAIN_URL, ""));
        }
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket)
                .withKeys(deleteFileKeys.toArray(new String[0]));

        amazonS3.deleteObjects(deleteObjectsRequest);
    }

}
