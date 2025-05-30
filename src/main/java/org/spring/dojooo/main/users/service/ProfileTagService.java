package org.spring.dojooo.main.users.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.domain.Tag;
import org.spring.dojooo.global.repository.TagRepository;
import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.*;
import org.spring.dojooo.main.users.exception.*;
import org.spring.dojooo.main.users.repository.ProfileTagRepository;
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
    private final ProfileTagRepository profileTagRepository;

    //조회
    @Transactional(readOnly = true)
    public TagDetailsList getProfileTag(Long userId,Authentication authentication) {
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
        User user = findCurrentUser(userId);
        List<ProfileTag> userprofileTags = profileTagRepository.findAllByUser(user);
        return TagDetailsList.from(user, userprofileTags);
    }
    //태그 저장(처음)
    @Transactional
    public ProfileTag saveTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        //권한 체크
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);

        //유저조회
        User user = findCurrentUser(userId);

        //중복확인 (같은 이름의 테그, 같은 유저, 이미 프로필 등록된 경우)
        duplicateTag(user,profileTagRequest);
        //테그 10자 제한
        checkTagLength(profileTagRequest.getTagName());
        //이미 있는 테그인지 아니면 생성
        Tag tag = tagRepository.findByTagNameAndUser(profileTagRequest.getTagName(), user)
                    .orElseGet(() -> tagRepository.save(new Tag(profileTagRequest.getTagName(), user)));
        ProfileTag profileTag =ProfileTag.builder()
                 .user(user)
                 .tag(tag)
                 .colorCode(profileTagRequest.getColorcode())
                 .showOnProfile(false)
                 .isDeleted(false)
                 .build();
        return profileTagRepository.save(profileTag);
    }
    //태그 수정
    @Transactional
    public ProfileTag updateTag(Long userId, ProfileTagUpdateRequest profileTagUpdateRequest, Authentication authentication) {
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);

        User user = findCurrentUser(userId);


        ProfileTag profileTag = profileTagRepository
                .findByUserAndTag_TagNameAndIsDeletedFalse(user, profileTagUpdateRequest.getOriginalTagName())
                .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));

        //테그 이름
        if (profileTagUpdateRequest.getNewTagName() != null && !profileTagUpdateRequest.getNewTagName().isBlank()) {
            checkTagLength(profileTagUpdateRequest.getNewTagName());
            duplicateTag(user,profileTagUpdateRequest);
            profileTag.getTag().updateTagName(profileTagUpdateRequest.getNewTagName());
        }
        //색상코드
        if(profileTagUpdateRequest.getNewColorCode() != null && !profileTagUpdateRequest.getNewColorCode().isBlank()) {
            profileTag.updateColorCode(profileTagUpdateRequest.getNewColorCode());
        }

        return profileTag;
    }

    //태그 삭제
    @Transactional
    public String deleteTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
        User user = findCurrentUser(userId);
        ProfileTag targetDeleteTag = findTargetTag(userId, profileTagRequest, authentication);
        Tag tag = targetDeleteTag.getTag();

        // 삭제 전에 이름 저장
        String deletedTagName = tag.getTagName();

        // 프로필 태그 삭제
        profileTagRepository.delete(targetDeleteTag);

        // 같은 유저의 다른 ProfileTag가 이 태그를 쓰고 있는지 확인
        boolean isUsedElsewhere = profileTagRepository.existsByUserIdAndTag(userId, tag);

        // 참조 안 되면 태그 삭제
        if (!isUsedElsewhere) {
            tagRepository.delete(tag);
        }

        return deletedTagName;
    }

    //테그 10자이상 작성시 Exception
    public void checkTagLength(String tagName) {
        if(tagName.length()>10){
            throw new MaxTegLengthException(ErrorCode.MAX_TAG_LENGTH_EXCEPTION);
        }
    }
    //테그 리스트중에서 조건에 맞는 테그 하나를 찾는 로직
    public ProfileTag findTargetTag(Long userId, ProfileTagRequest profileTagRequest, Authentication authentication) {
        userEqualsCurrentUser(getCurrentUserId(authentication), userId);
        User user = findCurrentUser(userId);
        return profileTagRepository.findByUserAndTag_TagNameAndIsDeletedFalse(user, profileTagRequest.getTagName())
                .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));
    }
    //현재 로그인한 사용자 정보
    public Long getCurrentUserId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    //로그인한 사용자와 정보가 같은지 확인
    public void userEqualsCurrentUser(Long userId, Long currentUserId) {
        if(!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
    }

    //유저 조회 로직
    public User findCurrentUser(Long userId) {
        User user  = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
        return user;
    }

    //중복 테그인지 아닌지 확인
    public void duplicateTag(User user, HasTagName tagRequest) {
        boolean alreadyRegistered = profileTagRepository.findAllByUser(user).stream()
                .anyMatch(pt -> pt.getTag().getTagName().equals(tagRequest.getTagName()));
        if (alreadyRegistered) {
            throw new DuplicateTagException(ErrorCode.DUPLICATE_TAG_EXCEPTION);
        }
    }


}
