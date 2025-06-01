package org.spring.dojooo.main.users.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.users.domain.*;
import org.spring.dojooo.main.users.dto.*;
import org.spring.dojooo.main.users.exception.*;
import org.spring.dojooo.main.users.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileTagService {

    private final UserRepository userRepository;
    private final ProfileTagRepository profileTagRepository;

    //조회
    @Transactional(readOnly = true)
    public TagDetailsList getProfileTag(Long userId,Authentication authentication) {
        getCurrentUserIdAndUserEqualsCurrentUser(userId, authentication);
        User user = findCurrentUser(userId);
        List<ProfileTag> userprofileTags = profileTagRepository.findAllByUser(user);
        return TagDetailsList.from(user, userprofileTags);
    }
    //태그 저장(처음)
    @Transactional
    public ProfileTag saveTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        // 권한 체크
        getCurrentUserIdAndUserEqualsCurrentUser(userId, authentication);
        // 유저 조회
        User user = findCurrentUser(userId);
        // 태그 10자 제한
        checkTagLength(profileTagRequest.getTagName());
        // 중복 확인
        duplicateTag(user, profileTagRequest);

        // 이미 존재하는 태그 찾기
        return profileTagRepository.findByUserAndTagNameAndIsDeletedFalse(user, profileTagRequest.getTagName())
                .orElseGet(() -> {
                    // 새 태그 생성
                    ProfileTag profileTag = ProfileTag.builder()
                            .tagName(profileTagRequest.getTagName())
                            .colorCode(profileTagRequest.getColorcode())
                            .isDeleted(false)
                            .showOnProfile(false)
                            .build();

                    // 연관관계 설정 (양방향)
                    user.addProfileTag(profileTag);

                    // 저장
                    return profileTagRepository.save(profileTag);
                });
    }
    //태그 수정
    @Transactional
    public ProfileTag updateTag(Long userId, ProfileTagUpdateRequest profileTagUpdateRequest, Authentication authentication) {
        getCurrentUserIdAndUserEqualsCurrentUser(userId, authentication);
        User user = findCurrentUser(userId);
        ProfileTag profileTag = profileTagRepository
                .findByUserAndTagNameAndIsDeletedFalse(user, profileTagUpdateRequest.getOriginalTagName())
                .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));

        //테그 이름
        if (profileTagUpdateRequest.getNewTagName() != null &&
                !profileTagUpdateRequest.getNewTagName().isBlank() &&
                !profileTagUpdateRequest.getNewTagName().equals(profileTag.getTagName())){

            checkTagLength(profileTagUpdateRequest.getNewTagName());
            duplicateTag(user,profileTagUpdateRequest);
            profileTag.updateTagName(profileTagUpdateRequest.getNewTagName());
        }
        //색상코드
        if(profileTagUpdateRequest.getNewColorCode() != null && !profileTagUpdateRequest.getNewColorCode().isBlank()) {
            checkTagLength(profileTagUpdateRequest.getNewColorCode());
            profileTag.updateColorCode(profileTagUpdateRequest.getNewColorCode());
        }
        return profileTag;
    }

    //태그 삭제
    @Transactional
    public String deleteTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        getCurrentUserIdAndUserEqualsCurrentUser(userId, authentication);
        ProfileTag targetDeleteTag = findTargetTag(userId, profileTagRequest, authentication);
        // 프로필 태그 삭제
        profileTagRepository.delete(targetDeleteTag);

        return targetDeleteTag.getTagName();
    }

    //테그 10자이상 작성시 Exception
    public void checkTagLength(String tagName) {
        if(tagName.length()>10){
            throw new MaxTagLengthException(ErrorCode.MAX_TAG_LENGTH_EXCEPTION);
        }
    }
    //테그 리스트중에서 조건에 맞는 테그 하나를 찾는 로직
    public ProfileTag findTargetTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        getCurrentUserIdAndUserEqualsCurrentUser(userId, authentication);
        User user = findCurrentUser(userId);
        return profileTagRepository.findByUserAndTagNameAndIsDeletedFalse(user, profileTagRequest.getTagName())
                .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));
    }
    //현재 로그인한 사용자 정보
    public void getCurrentUserIdAndUserEqualsCurrentUser(Long userId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(!userId.equals(userDetails.getId())){
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
    }

    //유저 조회 로직
    public User findCurrentUser(Long userId) {
        User user  = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
        return user;
    }

    //중복 테그인지 아닌지 확인
    public void duplicateTag(User user, org.spring.dojooo.global.domain.HasTagName tagRequest) {
        boolean alreadyRegistered = profileTagRepository.findAllByUser(user).stream()
                .anyMatch(pt -> pt.getTagName().equals(tagRequest.getTagName()));
        if (alreadyRegistered) {
            throw new DuplicateTagException(ErrorCode.DUPLICATE_TAG_EXCEPTION);
        }
    }


}
