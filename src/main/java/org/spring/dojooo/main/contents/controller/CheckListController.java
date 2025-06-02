package org.spring.dojooo.main.contents.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.global.response.ApiResponse;
import org.spring.dojooo.main.contents.dto.*;
import org.spring.dojooo.main.contents.service.ChecklistService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/checklist")
public class CheckListController {

    private final ChecklistService checklistService;

    @Operation(summary = "체크리스트 페이지 조회", description = "달력 및 특정 날짜의 체크리스트를 조회합니다")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CheckListPageResponse>> getCheckList(@PathVariable Long userId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {

        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        int year = targetDate.getYear();
        int month = targetDate.getMonthValue();

        List<CalendarChecklistSummary> calendarSummary = checklistService.getMonthlyChecklistSummary(userId, year, month, authentication);
        CheckListResponse todayCheckList = checklistService.getChecklistByDate(userId, targetDate, authentication);

        CheckListPageResponse response = CheckListPageResponse.builder()
                .calendarSummary(calendarSummary)
                .todayChecklist(todayCheckList)
                .build();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "특정 날짜의 할일 조회", description = "특정 날짜의 할일을 조회합니다.")
    @GetMapping("/{userId}/date")
    public ResponseEntity<ApiResponse<CheckListResponse>> getTasksByDate(@PathVariable Long userId, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {

        CheckListResponse result = checklistService.getChecklistByDate(userId, date, authentication);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "월별 할일 조회", description = "선택한 월의 할일을 조회합니다")
    @GetMapping("/{userId}/month")
    public ResponseEntity<ApiResponse<List<CheckListResponse>>> getTasksByMonth(@PathVariable Long userId, @RequestParam("year") int year, @RequestParam("month") int month,
            Authentication authentication) {

        List<CheckListResponse> result = checklistService.getCheckListByMonth(userId, year, month, authentication);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "할일 등록", description = "새로운 할일을 등록합니다")
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CheckListResponse>> addCheckList(
            @PathVariable Long userId,
            @RequestBody CheckListAddRequest checkListAddRequest,
            Authentication authentication) {

        // 서비스가 이미 CheckListResponse 반환하므로 그대로 받음
        CheckListResponse response = checklistService.saveTask(userId, checkListAddRequest, authentication);
        return ResponseEntity.ok(ApiResponse.of(200, "할일 등록이 완료되었습니다", response));
    }

    @Operation(summary = "할일 수정", description = "등록한 할일을 수정합니다")
    @PatchMapping("/{userId}/edit")
    public ResponseEntity<ApiResponse<CheckListResponse>> editCheckList(@PathVariable Long userId, @RequestBody CheckListUpdateRequest checkListUpdateRequest, Authentication authentication) {

        CheckListResponse response = checklistService.editTask(userId, checkListUpdateRequest, authentication);
        return ResponseEntity.ok(ApiResponse.of(200, "할일 수정이 완료되었습니다", response));
    }

    @Operation(summary = "할일 삭제", description = "등록된 할일을 삭제합니다")
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse<Long>> deleteCheckList(
            @PathVariable Long userId,
            @RequestBody CheckListDeleteRequest checkListDeleteRequest,
            Authentication authentication) {

        Long deletedTaskId = checklistService.deleteTask(userId, checkListDeleteRequest, authentication);
        return ResponseEntity.ok(ApiResponse.of(200, "할일 삭제가 완료되었습니다", deletedTaskId));
    }
}
