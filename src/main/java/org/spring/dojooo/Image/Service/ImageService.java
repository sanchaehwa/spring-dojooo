package org.spring.dojooo.Image.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.Image.domain.ProfileImage;
import org.spring.dojooo.Image.domain.TechLogImage;
import org.spring.dojooo.Image.model.TechLogImageType;
import org.spring.dojooo.Image.repository.ImageProfileRepository;
import org.spring.dojooo.Image.repository.ImageTechLogRepository;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.S3.S3FileService;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.contents.repository.TechLogRepository;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.exception.NotUserEqualsCurrentUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final TechLogRepository techLogRepository;
    private final ImageTechLogRepository imageTechLogRepository;
    private final ImageProfileRepository imageProfileRepository;
    private final S3FileService s3Uploader;
    private final UserRepository userRepository;


    @Transactional
    public String uploadProfileImage(MultipartFile file, Long userId, String email, Authentication auth) {
        validateFile(file);
        User user = getValidatedUser(userId, auth);
        String uploadedUrl = uploadToS3(file, "profile", email);
        updateProfileImage(user, uploadedUrl);
        return uploadedUrl;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
    }

    @Transactional
    protected void updateProfileImage(User user, String newImageUrl) {
        imageProfileRepository.findByUserId(user.getId()).ifPresent(oldImage -> {
            log.info("기존 프로필 이미지 삭제: {}", oldImage.getUrl());
            s3Uploader.deleteImageFromS3(oldImage.getUrl());
            imageProfileRepository.delete(oldImage);
        });

        ProfileImage newImage = ProfileImage.builder()
                .user(user)
                .url(newImageUrl)
                .build();
        imageProfileRepository.save(newImage);
    }

    @Transactional
    public String uploadTechLogImage(MultipartFile file, Long userId, String email, TechLogImageType imageType, Long techLogId, Authentication auth) {
        validateFile(file);

        User user = getValidatedUser(userId, auth);

        String uploadedUrl = uploadToS3(file, imageType.name().toLowerCase(), email);

        if (imageType == TechLogImageType.THUMBNAIL) {
            TechLog techLog = getValidatedTechLog(techLogId, user);
            techLog.updateTechLogThumbnailImageUrl(uploadedUrl);
            saveOrUpdateThumbnail(user, techLog, uploadedUrl);
        } else if (imageType == TechLogImageType.CONTENT) {
            saveContentImage(user, uploadedUrl); // TechLog 없이 저장
        } else {
            throw new IllegalArgumentException("지원하지 않는 이미지 타입입니다: " + imageType);
        }

        return uploadedUrl;
    }
    //S3
    private String uploadToS3(MultipartFile file, String type, String email) {
        try {
            String url = s3Uploader.upload(file, type);
            log.info("S3 업로드 성공: {}", url);
            return url;
        } catch (Exception e) {
            log.error("S3 업로드 실패 - email={}, filename={}", email, file.getOriginalFilename(), e);
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }


    private User getValidatedUser(Long userId, Authentication auth) {
        Long currentUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        if (!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }

        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
    }

    private TechLog getValidatedTechLog(Long techLogId, User user) {
        return techLogRepository.findByTechLogidAndUserAndIsDeletedFalse(techLogId, user)
                .orElseThrow(() -> new RuntimeException("TechLog을 찾을 수 없습니다."));
    }


    //썸네일 사진
    private void saveOrUpdateThumbnail(User user, TechLog techLog, String newUrl) {

        imageTechLogRepository.findByTechLog_TechLogidAndImageType(techLog.getTechLogid(), TechLogImageType.THUMBNAIL)
                .ifPresent(this::deleteExistingImage);
        saveImage(user, techLog, newUrl, TechLogImageType.THUMBNAIL);
    }
    //기존이미지 삭제
    private void deleteExistingImage(TechLogImage techLogImage) {
        try {
            log.info("기존 썸네일 이미지 삭제: {}", techLogImage.getUrl());
            s3Uploader.deleteImageFromS3(techLogImage.getUrl());
            imageTechLogRepository.delete(techLogImage);
        }
        catch (Exception e) {
            log.warn("기존 이미지를 삭제하는 중 오류 발생 : {}", techLogImage.getUrl(), e);
        }
    }
    //본문에 삽입할 이미지
    private void saveContentImage(User user, String uploadedUrl) {
        saveImage(user, null, uploadedUrl, TechLogImageType.CONTENT);
    }
    //썸네일용 이미지
    private void saveImage(User user, TechLog techLog, String url, TechLogImageType imageType) {
        TechLogImage techLogImage = TechLogImage.builder()
                .user(user)
                .techLog(imageType == TechLogImageType.THUMBNAIL ? techLog : null)
                .url(url)
                .imageType(imageType)
                .build();
        imageTechLogRepository.save(techLogImage);
    }
}
