package org.spring.dojooo.global.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.S3Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3FileService {

    private final AmazonS3 amazonS3;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    // 확장자별 Content-Type 매핑
    private static final Map<String, String> contentTypeMap = Map.of(
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png",
            "pdf", "application/pdf",
            "doc", "application/msword",
            "txt", "text/plain"
    );

    public String upload(MultipartFile file,String dirName) {
        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) { //빈파일명 이거나 파일명이 null이면 예외 발생
            throw new S3Exception(ErrorCode.EMPTY_FILE_EXCEPTION);
        }
        return this.uploadFile(file,dirName);
    }

    public String uploadFile(MultipartFile file, String dirName) {
        this.validateFileExtension(file.getOriginalFilename());
        try { //업로드전에 파일 확장자 유효성 검사
            return this.uploadToS3(file, dirName);
        } catch (IOException e) {
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private void validateFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) { //-1 확장자 없음
            throw new S3Exception(ErrorCode.NO_FILE_EXTENTION);
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        if (!contentTypeMap.containsKey(extension)) { //허용된 확장자 리스트에 포함되어 있는지 검증.
            throw new S3Exception(ErrorCode.NO_FILE_EXTENTION); //확장자는 있지만 허용된 확장자가 아님.
        }
    }

    private String uploadToS3(MultipartFile file, String dirName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase(); //확장자 추출 , 소문자로 변환
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename; //UUID를 앞에 붙여 S3에 저장할 새 파일 이름 생성
        String key = dirName + "/" + s3FileName;

        byte[] bytes = IOUtils.toByteArray(file.getInputStream());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentTypeMap.getOrDefault(extension, "application/octet-stream"));
        metadata.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); //파일을 S3에 업로드 - 공개 접근 허용
        } catch (Exception e) {
            throw new S3Exception(ErrorCode.PUT_OBJECT_EXCEPTION);
        } finally {
            byteArrayInputStream.close();
        }

        return amazonS3.getUrl(bucketName, key).toString();
    }
    //프로필 이미지 삭제
    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // Remove leading '/'
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }
}
