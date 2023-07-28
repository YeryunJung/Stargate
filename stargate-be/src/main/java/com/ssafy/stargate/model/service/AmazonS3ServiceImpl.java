package com.ssafy.stargate.model.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.stargate.model.dto.common.S3FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3ServiceImpl {

    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Autowired
    private final AmazonS3Client amazonS3Client;

    // S3로 파일 업로드
    public List<S3FileDto> uploadFiles(String fileType, List<MultipartFile> multipartFiles) {
        List<S3FileDto> s3Files = new ArrayList<>();
        String uploadFilePath = "TEST";
        for (MultipartFile multipartFile : multipartFiles) {
            String originFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUuidFileName(originFileName);
            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            System.out.println(uploadFileName);
            System.out.println(multipartFile.getSize());
            System.out.println(multipartFile.getContentType());
            
            try (InputStream inputStream = multipartFile.getInputStream()){
                String keyName = uploadFilePath + "/" + uploadFileName;
                
                // S3에 폴더 및 파일 업로드
                amazonS3Client.putObject(new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata));
                
                // 외부에 공개하는 파일인 경우 Public Read 권한 추가, ACL 확인
//                amazonS3Client.putObject(new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));

                // S3에 업로드한 폴더 및 파일 URL
                uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            s3Files.add(
                    S3FileDto.builder()
                            .originName(originFileName)
                            .uploadName(uploadFileName)
                            .uploadPath(uploadFilePath)
                            .uploadUrl(uploadFileUrl)
                            .build());
        }
        return s3Files;
    }

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }
}
