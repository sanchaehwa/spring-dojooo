package org.spring.dojooo.main.users.service;
import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.S3.S3FileService;
import org.spring.dojooo.global.exception.NotFoundException;
import org.spring.dojooo.main.users.domain.Profile;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.ProfileDetails;
import org.spring.dojooo.main.users.dto.ProfileEditRequest;
import org.spring.dojooo.main.users.dto.ProfileSaveRequest;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final S3FileService s3Uploader;

    // 프로필 조회
    @Transactional(readOnly = true)
    public ProfileDetails getProfile(Long userId, Authentication authentication) {
        Long currentUserId = getCurrentUserId(authentication);
        User user = findUserById(userId);
        boolean isOwner = currentUserId.equals(userId);
        return ProfileDetails.of(user, isOwner);
    }

    @Transactional
    public void editProfile(Long userId, ProfileEditRequest profileEditRequest, Authentication authentication) {
        Long currentUserId = getCurrentUserId(authentication);
        if (!userId.equals(currentUserId)) {
            throw new IllegalArgumentException("본인의 프로필만 수정할 수 있습니다.");
        }
        User user = findUserById(userId);
        Profile existingProfile = user.getProfile();

        String updatedImageUrl = null;
        String updatedIntro = null;

        if (existingProfile != null) {
            updatedImageUrl = existingProfile.getProfileImage();
            updatedIntro = existingProfile.getIntroduction();
        }

        MultipartFile newImage = profileEditRequest.getProfileImage();
        if (newImage != null && !newImage.isEmpty()) {
            if (existingProfile != null && existingProfile.getProfileImage() != null && !existingProfile.getProfileImage().isBlank()) {
                s3Uploader.deleteImageFromS3(existingProfile.getProfileImage());
            }
            updatedImageUrl = s3Uploader.upload(newImage, "profile");
        }

        String newIntro = profileEditRequest.getIntroduction();
        if (newIntro != null) {
            updatedIntro = newIntro;
        }

        Profile updatedProfile = Profile.builder()
                .profileImage(updatedImageUrl)
                .introduction(updatedIntro)
                .build();

        user.updateProfile(updatedProfile);
    }


    // 프로필 저장 (/save)
    @Transactional
    public void saveProfile(Long userId, ProfileSaveRequest profileSaveRequest, Authentication authentication) {
        Long currentUserId = getCurrentUserId(authentication);
        if (!userId.equals(currentUserId)) {
            throw new IllegalArgumentException("본인의 프로필만 저장할 수 있습니다");
        }

        User user = findUserById(userId);

        Profile profile = Profile.builder()
                .profileImage(profileSaveRequest.getProfileImageUrl())
                .introduction(profileSaveRequest.getIntroduction())
                .build();

        user.updateProfile(profile);
    }

    // 로그인한 사용자 ID 추출
    private Long getCurrentUserId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    // 사용자 조회
    private User findUserById(Long userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }
}
