package org.spring.dojooo.main.users.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.S3.S3FileService;
import org.spring.dojooo.global.exception.NotFoundException;
import org.spring.dojooo.main.contents.domain.Memo.Tag;
import org.spring.dojooo.main.users.domain.Profile;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.ProfileDetails;
import org.spring.dojooo.main.users.dto.ProfileUpdateRequest;
import org.spring.dojooo.main.users.dto.ProfileSaveRequest;
import org.spring.dojooo.main.users.exception.NotUserEqualsCurrentUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserRepository userRepository;
    private static final String DEFAULT_PROFILE_IMAGE = "https://dojooo.s3.ap-northeast-2.amazonaws.com/profile/80aefad7-3_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%91%E1%85%B5%E1%86%AF.jpg";
    private final S3FileService s3FileService;

    // 프로필 조회
    @Transactional(readOnly = true)
    public ProfileDetails getProfile(Long userId, Authentication authentication) {
        Long currentUserId = getCurrentUserId(authentication);
        User user = findUserById(userId);
        boolean isOwner = currentUserId.equals(userId);
        return ProfileDetails.of(user, isOwner);
    }
    //프로필 수정
    @Transactional
    public void editProfile(Long userId, ProfileUpdateRequest profileEditRequest, Authentication authentication) { Long currentUserId = getCurrentUserId(authentication);

        userEqualsCurrentUser(getCurrentUserId(authentication), userId);

        User user = findUserById(userId);
        Profile existingProfile = user.getProfile(); //기존(/users/profile/save) 프로필

        String getImageUrl = profileEditRequest.getProfileImageUrl();
        String getIntroduction = profileEditRequest.getIntroduction();

        //Null 허용 (사용자가 이미지만, 자기소개만 수정하고 싶을수도 있으니깐)
        String updatedImageUrl = (getImageUrl != null && !getImageUrl.isBlank())
                ? getImageUrl
                : (existingProfile != null && existingProfile.getProfileImage() != null && !existingProfile.getProfileImage().isBlank())
                ? existingProfile.getProfileImage()
                : DEFAULT_PROFILE_IMAGE;

        String updatedIntroduction = (getIntroduction != null) ? getIntroduction : (existingProfile != null ? existingProfile.getIntroduction() : null);

        //태그 보여줄 목록 갱신
        List<String> selectedTagNames = profileEditRequest.getTagNames();
        //0 ~ 5개 선택할수있음
        if(selectedTagNames != null && selectedTagNames.size() > 5) {
            throw new IllegalArgumentException("최대 5개의 테그만 선택할 수 있습니다. ");
        }
        List<Tag> updatedTags = user.getTags().stream()
                .map(tag -> tag.withShowOnProfile(
                        selectedTagNames != null && selectedTagNames.contains(tag.getTagName())
                ))
                .collect(Collectors.toList());
        user.replaceTags(updatedTags);


        Profile updatedprofile = Profile.builder()
                .profileImage(updatedImageUrl)
                .introduction(updatedIntroduction)
                .build();
        //수정한 내용 반영
        user.updateProfile(updatedprofile);

        log.info("User {} updated profile. New image: {}, New intro: {}", userId, updatedImageUrl, updatedIntroduction);
    }


    // 프로필 저장 (/save)
    @Transactional
    public void saveProfile(Long userId, ProfileSaveRequest profileSaveRequest, Authentication authentication) {

        userEqualsCurrentUser(getCurrentUserId(authentication), userId);

        User user = findUserById(userId);

        String imageUrl = profileSaveRequest.getProfileImageUrl();
        if (imageUrl == null || imageUrl.isBlank()) {
            imageUrl = DEFAULT_PROFILE_IMAGE;
        }
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

    //로그인한 사용자와 정보가 같은지 확인
    public Long userEqualsCurrentUser(Long userId, Long currentUserId) {
        if(!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        return userId;
    }

    // 사용자 조회
    private User findUserById(Long userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    //프로필 초기화
    @Transactional
    public void resetProfile(Long userId, Authentication authentication){
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
        User user = findUserById(userId);
        //기존 이미지가 Default 이미지(기본 이미지) 가 아닌 등록한 이미지인경우 S3에 등록이미지 제거
        String currentImageUrl = user.getProfile().getProfileImage();
        if (currentImageUrl != null && !DEFAULT_PROFILE_IMAGE.equals(currentImageUrl)) {
            try{
                s3FileService.deleteImageFromS3(currentImageUrl);
                log.info("User {} delete profile image from s3", user.getId());
            }catch(Exception e){
                log.warn("Failed to delete profile image from s3: {}", currentImageUrl);
            }
        }

        Profile resetProfile = Profile.builder()
                .profileImage(DEFAULT_PROFILE_IMAGE)
                .introduction(null)
                .build();
        user.updateProfile(resetProfile);
        log.info("User {} reset profile.", userId);


    }
}
