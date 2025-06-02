package org.spring.dojooo.main.contents.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.contents.domain.*;
import org.spring.dojooo.main.contents.dto.*;
import org.spring.dojooo.main.contents.exception.*;
import org.spring.dojooo.main.contents.model.TodoState;
import org.spring.dojooo.main.contents.repository.*;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.*;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final UserRepository userRepository;
    private final CheckListRepository checkListRepository;
    private final ChecklistTagRepository checklistTagRepository;

    @Transactional // 할일 등록
    public CheckListResponse saveTask(Long userId, CheckListAddRequest checklistAddRequest, Authentication authentication) {
        User user = getValidatedUser(userId, authentication);
        CheckListTag tag = getChecklistTagById(checklistAddRequest);
        CheckList checkListSaveTask = checklistAddRequest.toEntity(user, tag);
        // 사용자가 보내는 날짜를 기준 - 현재날짜가 Default 아님
        checkListSaveTask.updateDateRange(
                checklistAddRequest.getStartDate(),
                checklistAddRequest.getEndDate()
        );
        checkListRepository.save(checkListSaveTask);

        return CheckListResponse.from(checkListSaveTask.getStartDate(), List.of(checkListSaveTask));
    }

    // 해당 일의 할일 조회
    @Transactional(readOnly = true)
    public CheckListResponse getChecklistByDate(Long userId, LocalDate date, Authentication authentication) {
        User user = getValidatedUser(userId, authentication);
        // 해당 날짜의 할일 목록 가져옴
        List<CheckList> checkLists = checkListRepository.findByUserIdAndDate(user.getId(), date);
        // 카테고리 기준 정렬
        List<CheckList> sorted = checkLists.stream()
                .sorted(Comparator.comparing(checkList -> checkList.getChecklistTag().getTagName()))
                .collect(Collectors.toList());
        return CheckListResponse.from(date, sorted);
    }

    // 해당 월의 할일 조회
    @Transactional(readOnly = true)
    public List<CheckListResponse> getCheckListByMonth(Long userId, int year, int month, Authentication authentication) {
        User user = getValidatedUser(userId, authentication);

        LocalDate startDate = LocalDate.of(year, month, 1);//2025 - 06 - 01
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());//2025 - 06 - 30

        List<CheckList> checkLists = checkListRepository.findByUserAndEndDateBetweenAndIsDeletedFalse(user, startDate, endDate);
        return checkLists.stream()
                .collect(Collectors.groupingBy(CheckList::getStartDate))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> CheckListResponse.from(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // 할일 수정
    @Transactional
    public CheckListResponse editTask(Long userId, CheckListUpdateRequest checkListUpdateRequest, Authentication authentication) {
        User user = getValidatedUser(userId, authentication);

        // 사용자 인증
        if (!checkListUpdateRequest.getUserId().equals(user.getId())) {
            throw new WrongEditChecklistException(ErrorCode.WRONG_EDIT_CHECKLIST_EXCEPTION);
        }

        // 기존 할일 조회
        CheckList existingTask = checkListRepository.findById(checkListUpdateRequest.getCheckListId())
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.NOT_FOUND_TASK_EXCEPTION));

        // 태그가 변경되었는지 확인하고 업데이트
        if (checkListUpdateRequest.getTagId() != null &&
                !checkListUpdateRequest.getTagId().equals(existingTask.getChecklistTag().getId())) {

            CheckListTag newTag = checklistTagRepository.findById(checkListUpdateRequest.getTagId())
                    .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));

            existingTask.updateTag(newTag);
        }

        // 기타 정보 업데이트
        existingTask.updateTask(checkListUpdateRequest.getTask());
        existingTask.updateDateRange(checkListUpdateRequest.getStartDate(), checkListUpdateRequest.getEndDate());

        if (checkListUpdateRequest.getTodoState() != null &&
                !checkListUpdateRequest.getTodoState().equals(existingTask.getTodoState())) {
            existingTask.updateTodoState(checkListUpdateRequest.getTodoState());
        }

        return CheckListResponse.from(existingTask.getStartDate(), List.of(existingTask));
    }

    // 할일 삭제
    @Transactional
    public Long deleteTask(Long userId, CheckListDeleteRequest checkListDeleteRequest, Authentication authentication) {
        User user = getValidatedUser(userId, authentication);
        CheckList checkListDeleteTask = checkListRepository.findById(checkListDeleteRequest.getCheckListId())
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.NOT_FOUND_TASK_EXCEPTION));
        // 삭제 권한 체크 (요청한 유저와 할일 소유자 일치 여부)
        if (!checkListDeleteTask.getUser().getId().equals(user.getId())) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        // soft Delete
        checkListDeleteTask.updateIsDeleted(true);
        return checkListDeleteRequest.getCheckListId();
    }

    // 할일 개수
    public List<CalendarChecklistSummary> getMonthlyChecklistSummary(Long userId, int year, int month, Authentication authentication) {
        User user = getValidatedUser(userId, authentication);
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<CheckList> checkLists = checkListRepository.findByUserAndEndDateBetweenAndIsDeletedFalse(user, start, end);

        return checkLists.stream()
                .collect(Collectors.groupingBy(CheckList::getStartDate))
                .entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<CheckList> tasks = entry.getValue();
                    int total = tasks.size();
                    int done = (int) tasks.stream().filter(t -> t.getTodoState() == TodoState.DONE).count();
                    return CalendarChecklistSummary.builder()
                            .date(date)
                            .totalCount(total)
                            .doneCount(done)
                            .build();
                })
                .sorted(Comparator.comparing(CalendarChecklistSummary::getDate))
                .collect(Collectors.toList());
    }

    // 테그 조회
    public CheckListTag getChecklistTagById(CheckListAddRequest checkListAddRequest) {

        CheckListTag tag = checklistTagRepository.findById(checkListAddRequest.getTagId())
                .orElseThrow(() -> new NotFoundTagException(ErrorCode.NOTFOUND_TAG_EXCEPTION));

        return tag;
    }

    // 인증된 사용자 ID 가져오기
    private Long getAuthenticatedUserId(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getId();
    }

    // 인증된 사용자 객체 가져오기 및 검증
    private User getValidatedUser(Long userId, Authentication authentication) {
        Long currentUserId = getAuthenticatedUserId(authentication);
        if (!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
    }

}
