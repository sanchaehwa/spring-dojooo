package org.spring.dojooo.Image.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.Image.domain.Image;
import org.spring.dojooo.Image.repository.ImageRepository;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.S3.S3FileService;
import org.spring.dojooo.main.users.domain.Profile;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3FileService s3Uploader;
    private final UserRepository userRepository;

    @Transactional
    public String uploadImage(MultipartFile file, String email, String type) {
        if (file == null || file.isEmpty()) {
            log.warn("업로드할 파일이 비어 있습니다. email={}", email);
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        String uploadedUrl;
        try {
            uploadedUrl = s3Uploader.upload(file, type); // type: "profile", "techlog", etc.
            log.info("이미지 S3 업로드 완료: {}", uploadedUrl);
        } catch (Exception e) {
            log.error("S3 업로드 실패. email={}, filename={}", email, file.getOriginalFilename(), e);
            throw new RuntimeException("S3 업로드 실패", e);
        }

        // 프로필 전용 처리
        if (type.equals("profile")) {
            imageRepository.findByUserId(user.getId()).ifPresent(oldImage -> {
                log.info("기존 프로필 이미지 삭제: {}", oldImage.getUrl());
                s3Uploader.deleteImageFromS3(oldImage.getUrl());
                imageRepository.delete(oldImage);
            });

            // 새 이미지 저장
            Image image = Image.builder()
                    .user(user)
                    .url(uploadedUrl)
                    .build();
            imageRepository.save(image);
        }

        // 프로필 외에는 S3에만 저장하고 URL만 반환
        return uploadedUrl;
    }
    }
