package org.spring.dojooo.main.contents.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.contents.dto.CheckListDetailsList;
import org.spring.dojooo.main.contents.dto.CheckListTagRequest;
import org.spring.dojooo.main.contents.dto.CheckListUpdateTagRequest;
import org.spring.dojooo.main.contents.repository.ChecklistTagRepository;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.global.domain.HasTagName;
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
        User user = getCurrentUserId(userId,authentication);
        checkListTagLength(checkListTagRequest.getTagName());
        duplicateTag(user,checkListTagRequest);
        CheckListTag checklistTag=  checklistTagRepository.findByUserAndTagNameAndIsDeletedFalse(user, checkListTagRequest.getTagName())
                .orElseGet(() -> checklistTagRepository.save(
                        CheckListTag.builder()
                                .tagName(checkListTagRequest.getTagName())
                                .colorCode(checkListTagRequest.getColorCode())
                                .isChecklistShow(false)
                                .user(user)
                                .build()
                ));

        return checklistTagRepository.save(checklistTag);
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

            checkListTagLength(checkListUpdateTagRequest.getNewCheckListColorCode());
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
            throw new MaxTegLengthException(ErrorCode.MAX_TAG_LENGTH_EXCEPTION);
        }
    }
    //중복 테그인지 확인
    public void duplicateTag(User user,HasTagName tagRequest ) {
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


