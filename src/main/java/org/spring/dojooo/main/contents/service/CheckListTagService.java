package org.spring.dojooo.main.contents.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.contents.dto.*;
import org.spring.dojooo.main.contents.repository.ChecklistTagRepository;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.*;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckListTagService {
    private final UserRepository userRepository;
    private final ChecklistTagRepository checklistTagRepository;

    //태그 새로 저장
    @Transactional
    public CheckListTag saveCheckListTag(Long userId, CheckListTagRequest checkListTagRequest, Authentication authentication) {
        User user = getCurrentUserId(userId, authentication);
        checkListTagLength(checkListTagRequest.getTagName());
        duplicateTag(user, checkListTagRequest);

        CheckListTag newTag = CheckListTag.builder()
                .tagName(checkListTagRequest.getTagName())
                .colorCode(checkListTagRequest.getColorCode())
                .isChecklistTagShow(false)
                .user(user)
                .build();

        return checklistTagRepository.save(newTag);
    }

    //태그 수정
    @Transactional
    public CheckListTag updateCheckListTag(Long userId, CheckListUpdateTagRequest checkListUpdateTagRequest, Authentication authentication) {
        User user = getCurrentUserId(userId,authentication);

        CheckListTag checklistTag = checklistTagRepository.findByUserAndTagNameAndIsDeletedFalse(user,checkListUpdateTagRequest.getOriginalCheckListTagName())
                .orElseThrow(()->new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));
        if (checkListUpdateTagRequest.getNewCheckListTagName() != null &&
                !checkListUpdateTagRequest.getNewCheckListTagName().isBlank() &&
                !checkListUpdateTagRequest.getNewCheckListTagName().equals(checklistTag.getTagName())) {

            checkListTagLength(checkListUpdateTagRequest.getNewCheckListTagName());
            duplicateTag(user, checkListUpdateTagRequest);
            checklistTag.updateCheckListTagName(checkListUpdateTagRequest.getNewCheckListTagName());
        }

        if (checkListUpdateTagRequest.getNewCheckListColorCode() != null &&
                !checkListUpdateTagRequest.getNewCheckListColorCode().isBlank()) {
            checklistTag.updateColorCode(checkListUpdateTagRequest.getNewCheckListColorCode());
        }

        return checklistTag;
    }
    //태크 삭제
    @Transactional
    public String deleteCheckListTag(Long userId, CheckListTagRequest checkListTagRequest, Authentication authentication) {
        User user = getCurrentUserId(userId,authentication);
        //삭제할 테그
        CheckListTag targetDeleteTag = findTargetTag(userId,checkListTagRequest,authentication);
        checklistTagRepository.delete(targetDeleteTag);

        return targetDeleteTag.getTagName();
    }

    //태그 조회
    @Transactional(readOnly = true)
    public CheckListDetailsList getCheckListTag(Long userId, Authentication authentication) {
        User user = getCurrentUserId(userId,authentication);
        List<CheckListTag> checklistTags = checklistTagRepository.findAllByUser(user);
        return CheckListDetailsList.from(user,checklistTags);
    }

    // 태그-체크리스트 카테고리 5개 등록
    @Transactional
    public CheckListDetailsList CheckListTagRegisterInCategory(
            Long userId,
            CheckListTagRegisterInCategoryRequest request,
            Authentication authentication) {

        User user = getCurrentUserId(userId, authentication);
        List<String> tags = request.getTags();

        if (tags.size() > 5) {
            throw new MaxTagRegisterException(ErrorCode.MAX_REGISTER_TAG_EXCEPTION);
        }

        // 전체 태그를 비표시로 초기화
        List<CheckListTag> checklistTags = checklistTagRepository.findAllByUser(user);
        for (CheckListTag tag : checklistTags) {
            tag.setChecklistShow(false);
        }

        // 선택된 태그만 표시로 설정
        for (String tagName : tags) {
            CheckListTag matchingTag = checklistTags.stream()
                    .filter(t -> t.getTagName().equals(tagName))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));
            matchingTag.setChecklistShow(true);
        }

        // 표시된 태그만 DTO로 변환
        List<CheckListTagDetails> selectedTags = checklistTags.stream()
                .filter(CheckListTag::isChecklistTagShow)
                .map(tag -> CheckListTagDetails.from(user, tag))
                .toList();

        return CheckListDetailsList.from(user, checklistTags);
    }

    //현재 로그인한 사용자 정보 == 로그인한 사용자와 정보가 같은지 확인하고 유저 조회
    public User getCurrentUserId(Long userId,Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = customUserDetails.getId();
        if(!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(()-> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
        return user;

    }
    //태그 10자 이상 작성시 Exception
    public void checkListTagLength(String tagName){
        if (tagName.length() > 10){
            throw new MaxTagLengthException(ErrorCode.MAX_TAG_LENGTH_EXCEPTION);
        }
    }
    //중복 테그인지 확인
    public void duplicateTag(User user, org.spring.dojooo.global.domain.HasTagName tagRequest ) {
        boolean alreadyRegistered = checklistTagRepository.findAllByUser(user).stream()
                .anyMatch(ct -> ct.getTagName().equals(tagRequest.getTagName()));
        if (alreadyRegistered) {
            throw new DuplicateTagException(ErrorCode.DUPLICATE_TAG_EXCEPTION);
        }
    }
    //테그 리스트 중에서 조건에 맞는 태그 하나를 찾는 로직
    public CheckListTag findTargetTag(Long userId, CheckListTagRequest checkListTagRequest, Authentication authentication) {
        User user = getCurrentUserId(userId,authentication);
        return checklistTagRepository.findByUserAndTagNameAndIsDeletedFalse(user,checkListTagRequest.getTagName())
                .orElseThrow(()->new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));
    }
}


