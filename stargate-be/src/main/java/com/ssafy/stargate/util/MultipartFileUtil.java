package com.ssafy.stargate.util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.stargate.exception.CRUDException;
import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.handler.FileHandler;
import com.ssafy.stargate.model.dto.common.SavedFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * Service 단에서 사용되는 Multipart 파일에 관한 유틸리티
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MultipartFileUtil {
    @Autowired
    private final FileHandler fileHandler;

    public String getKey(String filePath, String filename) {
        return fileHandler.getKey(filePath, filename);
    }

    public SavedFileDto getFileInfo(String key) {
        try {
            return fileHandler.getFileInfo(key);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }


    public  uploadFile(String key, MultipartFile multipartFile) {
        if (multipartFile != null) {

        }
        try {
            fileHandler.uploadFile(key, multipartFile);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    public void uploadFile(String key, MultipartFile multipartFile) throws CRUDException {
        log.info("upload start. key: {}, size: {}, contentType: {}", key, multipartFile.getSize(), multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            // ACL로 Public Read 권한 추가하여 외부에 파일 공개
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, key, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead)
            );

        } catch (IOException e) {
            throw new CRUDException("S3 파일 업로드에 실패했습니다.");
        }

        if (!amazonS3Client.doesObjectExist(bucketName, key)) {
            throw new CRUDException("S3 파일 업로드에 실패했습니다.");
        }

        log.info("Success upload. Url: {}", amazonS3Client.getUrl(bucketName, key));
    }

    /**
     * 파일을 삭제한다.
     *
     * @param key [String] 삭제할 파일 경로
     * @throws NotFoundException 해당 파일이 없음
     * @throws CRUDException     삭제에 실패
     */
    public void deleteFile(String key) throws NotFoundException, CRUDException {
        if (!amazonS3Client.doesObjectExist(bucketName, key)) {
            throw new NotFoundException("해당 S3 파일이 존재하지 않습니다. key: " + key);
        }

        amazonS3Client.deleteObject(bucketName, key);

        if (amazonS3Client.doesObjectExist(bucketName, key)) {
            throw new CRUDException("S3 파일 삭제에 실패했습니다.");
        }
    }
}
