package org.spring.dojooo.main.contents.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.global.response.ApiResponse;
import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.contents.dto.*;
import org.spring.dojooo.main.contents.service.CheckListTagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/checklist/tags")
@Slf4j
public class ChecklistTagController {

    private final CheckListTagService checkListTagService;

    @Operation(
            summary = "카테고리 전체 조회",
            description = "사용자가 생성한 모든 체크리스트 카테고리를 조회합니다."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CheckListDetailsList>> getCheckListTags(@PathVariable Long userId, Authentication authentication){
        CheckListDetailsList checkListDetailsList = checkListTagService.getCheckListTag(userId, authentication);
        return ResponseEntity.ok(ApiResponse.ok(checkListDetailsList));
    }
    @Operation(summary = "카테고리 등록", description = "새로운 체크리스트 카테고리를 추가합니다.")
    @PostMapping("/save/{userId}")
    public ResponseEntity<ApiResponse<CheckListTagDetails>> saveCheckListTag(@PathVariable Long userId, @Valid @RequestBody CheckListTagRequest checkListTagRequest, Authentication authentication) {
        CheckListTag saveCheckListTag = checkListTagService.saveCheckListTag(userId, checkListTagRequest,authentication);
        CheckListTagDetails checkListTagDetails = CheckListTagDetails.from(saveCheckListTag.getUser(),saveCheckListTag);
        return ResponseEntity.ok(ApiResponse.ok(checkListTagDetails));
    }
    @Operation(summary = "카테고리 수정", description = "기존 카테고리의 이름 또는 색상을 수정합니다.")
    @PatchMapping("/edit/{userId}")
    public ResponseEntity<ApiResponse<CheckListTagDetails>> editCheckListTag(@PathVariable Long userId, @Valid @RequestBody CheckListUpdateTagRequest checkListUpdateTagRequest, Authentication authentication) {
        CheckListTag editCheckListTag = checkListTagService.updateCheckListTag(userId, checkListUpdateTagRequest,authentication);
        CheckListTagDetails checkListTagDetails = CheckListTagDetails.from(editCheckListTag.getUser(),editCheckListTag);
        return ResponseEntity.ok(ApiResponse.ok(checkListTagDetails));
    }
    @Operation(summary = "카테고리 삭제",description = "선택된 카테고리를 삭제합니다.")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteCheckListTag(@PathVariable Long userId,@Valid @RequestBody CheckListTagRequest checkListTagRequest, Authentication authentication) {
        String deletedCheckListTag = checkListTagService.deleteCheckListTag(userId, checkListTagRequest, authentication);
        return ResponseEntity.ok(ApiResponse.of(200,"카테고리 삭제가 완료되었습니다",deletedCheckListTag));
    }
}
