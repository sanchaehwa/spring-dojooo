package org.spring.dojooo.main.users.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.domain.Tag;
import org.spring.dojooo.global.repository.TagRepository;
import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.*;
import org.spring.dojooo.main.users.exception.DuplicateTagException;
import org.spring.dojooo.main.users.exception.NotFoundTagException;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.exception.NotUserEqualsCurrentUserException;
import org.spring.dojooo.main.users.repository.ProfileTagReposiotry;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileTagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ProfileTagReposiotry profileTagReposiotry;

    //조회
    @Transactional(readOnly = true)
    public TagDetailsList getProfileTag(Long userId,Authentication authentication) {
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
        List<ProfileTag> userprofileTags = profileTagReposiotry.findAllByUser(user);
        return TagDetailsList.from(user, userprofileTags);
    }
    //테그 저장(처음)
    @Transactional
    public ProfileTag saveTag(Long userId, ProfileTagRequest profileTagUpdateRequest, Authentication authentication) {
        //권한 체크
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);

        //유저조회
        User user  = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        //중복확인 (같은 이름의 테그, 같은 유저, 이미 프로필 등록된 경우)
        duplicateTag(userId,profileTagUpdateRequest, authentication);
        //테그 10자 제한
        checkTagLength(profileTagUpdateRequest.getTagName());
        //이미 있는 테그인지 아니면 생성
        Tag tag = tagRepository.findByTagNameAndUser(profileTagUpdateRequest.getTagName(), user)
                    .orElseGet(() -> tagRepository.save(new Tag(profileTagUpdateRequest.getTagName(), user)));
        ProfileTag profileTag =ProfileTag.builder()
                 .user(user)
                 .tag(tag)
                 .colorCode(profileTagUpdateRequest.getColorcode())
                 .showOnProfile(false)
                 .isDeleted(false)
                 .build();
        return profileTagReposiotry.save(profileTag);
    }
//    //태그 수정
//    @Transactional
//    public ProfileTag updateTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
//        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
//
//        User user  = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
//
//
//
//        ProfileTag profileTag = profileTagReposiotry
//                .findByUserAndTag_TagNameAndIsDeletedFalse(user, profileTagRequest.getOriginalTagName())
//                .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));
//        checkTagLength(profileTagRequest.getTagName());
//
//        profileTag.getTag().updateTagName(profileTagRequest.getTagName());
//
//        profileTag.updateColorCode(profileTagRequest.getColorcode());
//
//        return profileTag;
//    }

    //태그 삭제
    @Transactional
    public String deleteTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        ProfileTag targetDeleteTag = findTargetTag(userId,profileTagRequest,authentication);
        profileTagReposiotry.delete(targetDeleteTag);
        return targetDeleteTag.getTag().getTagName();
    }

    //테그 10자이상 작성시 Exception
    public void checkTagLength(String tagName) {
        if(tagName.length()>10){
            throw new IllegalArgumentException("테그는 10자 이내로 작성가능합니다");
        }
    }
    //테그 리스트중에서 조건에 맞는 테그 하나를 찾는 로직
    public ProfileTag findTargetTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        return profileTagReposiotry.findByUserAndTag_TagNameAndIsDeletedFalse(user, profileTagRequest.getTagName())
                .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));
    }
    //현재 로그인한 사용자 정보
    public Long getCurrentUserId(Authentication authentication) {
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
    //중복 테그인지 아닌지 확인
    public void duplicateTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        boolean alreadyRegistered = profileTagReposiotry.findAllByUser(user).stream()
                .anyMatch(pt -> pt.getTag().getTagName().equals(profileTagRequest.getTagName()));
        if (alreadyRegistered) {
            throw new DuplicateTagException(ErrorCode.DUPLICATE_TAG_EXCEPTION);
        }
    }



}
