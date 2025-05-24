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
    public String uploadProfileImage(MultipartFile file, String email) throws IOException {
        if (file == null || file.isEmpty()) {
            log.warn("업로드할 파일이 비어 있습니다. email={}", email);
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        // 기존 이미지 삭제
        imageRepository.findByUserId(user.getId()).ifPresent(oldImage -> {
            log.info("기존 이미지 삭제: {}", oldImage.getUrl());
            s3Uploader.deleteImageFromS3(oldImage.getUrl());
            imageRepository.delete(oldImage);
        });

        // S3에 이미지 업로드
        String uploadedUrl;
        try {
            uploadedUrl = s3Uploader.upload(file, "profile");
            log.info("이미지 S3 업로드 완료: {}", uploadedUrl);
        } catch (Exception e) {
            log.error("S3 업로드 실패. email={}, filename={}", email, file.getOriginalFilename(), e);
            throw new RuntimeException("S3 업로드 실패", e);
        }

        // 프로필 갱신
        Profile oldProfile = user.getProfile();
        if (oldProfile == null) {
            user.updateProfile(Profile.builder()
                    .profileImage(uploadedUrl)
                    .introduction("")
                    .build());
            log.info("새로운 프로필 생성됨.");
        } else {
            Profile updatedProfile = oldProfile.toBuilder()
                    .profileImage(uploadedUrl)
                    .build();
            user.updateProfile(updatedProfile);
            log.info("기존 프로필 이미지 갱신됨.");
        }

        // 이미지 엔티티 저장
        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .url(uploadedUrl)
                .user(user)
                .build();

        imageRepository.save(image);
        log.info("이미지 DB 저장 완료. filename={}, userId={}", file.getOriginalFilename(), user.getId());

        return uploadedUrl;
    }
}
