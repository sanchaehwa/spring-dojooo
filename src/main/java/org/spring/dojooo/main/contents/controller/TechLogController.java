package org.spring.dojooo.main.contents.controller;

import com.amazonaws.ResponseMetadata;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.spring.dojooo.global.response.ApiResponse;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.contents.dto.*;
import org.spring.dojooo.main.contents.service.TechLogService;
import org.spring.dojooo.main.contents.service.TechLogTempService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/techLog")
public class TechLogController {
    private final TechLogService techLogService;
    private final TechLogTempService techLogTempService;

    @Operation(summary = "글 작성", description = "작성할 글을 작성합니다")
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<TechLogResponse>> saveTechLog(@PathVariable Long userId, String tempUuid, @Valid @RequestBody TechLogAddRequest techLogAddRequest, Authentication authentication) {
        TechLogWithUser saveTechLog = techLogService.techLogSave(userId, tempUuid,techLogAddRequest,authentication);
        TechLogResponse response =  TechLogResponse.from(saveTechLog.techLog(),saveTechLog.user());
        return ResponseEntity.ok(ApiResponse.of(200, "글이 저장되었습니다", response));
    }
    @Operation(summary = "작성 글 조회", description = "작성한 글을 조회합니다")
    @GetMapping("/{userId}/{techLogId}")
    public ResponseEntity<ApiResponse<TechLogResponse>> getTechLogDetail(@PathVariable Long userId, @PathVariable Long techLogId, Authentication authentication) {

        TechLog techLog = techLogService.loadTechLog(userId, techLogId, authentication);
        TechLogResponse response = TechLogResponse.from(techLog, techLog.getUser());

        return ResponseEntity.ok(ApiResponse.of(200, "글이 조회되었습니다", response));
    }


    @Operation(summary = "공개 여부 설정", description = "글의 공개 여부를 설정합니다")
    @PatchMapping("/{userId}/{techLogId}/visibility")
    public ResponseEntity<ApiResponse<String>> updateVisibility(@PathVariable Long userId, @PathVariable Long techLogId, @RequestParam(name = "isPublic") boolean isPublic, Authentication authentication) {
        techLogService.updateVisibility(userId, techLogId, isPublic, authentication);
        String visibilityStatus = isPublic ? "공개" : "비공개";
        return ResponseEntity.ok(ApiResponse.of(200, "공개 여부가 " + visibilityStatus + "으로 설정되었습니다", visibilityStatus));
    }
    @Operation(summary = "작성 글 임시저장")
    @PostMapping("/temp/{userId}")
    public ResponseEntity<ApiResponse<String>> saveTempLog(
            @PathVariable Long userId,
            @Valid @RequestBody TechLogTempRequest request,
            Authentication authentication) {

        String tempUuid = techLogTempService.saveTempLog(userId, request, authentication);
        return ResponseEntity.ok(ApiResponse.of(200, "임시저장 완료", tempUuid));
    }

    @Operation(summary = "임시저장 글 불러오기")
    @GetMapping("/temp/{userId}/{tempUuid}")
    public ResponseEntity<ApiResponse<TechLogTempResponse>> loadTempLog(@PathVariable Long userId, @PathVariable String tempUuid, Authentication authentication) {

        TechLogTempResponse response = techLogTempService.loadTempLog(userId, tempUuid, authentication);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "임시저장 글 삭제")
    @DeleteMapping("/temp/{userId}/{tempUuid}")
    public ResponseEntity<ApiResponse<Void>> deleteTempLog(@PathVariable Long userId, @PathVariable String tempUuid, Authentication authentication) {

        techLogTempService.deleteTempLog(userId, tempUuid, authentication);
        return ResponseEntity.ok(ApiResponse.of(200, "임시저장 삭제가 완료되었습니다", null));
    }
    @Operation(summary = "글 삭제",description = "작성한 글을 삭제합니다")
    @DeleteMapping("/{userId}/{techLogId}/delete")
    public ResponseEntity<ApiResponse<Void>>deleteTechLog(@PathVariable Long userId,  @PathVariable Long techLogId, @RequestBody TechLogDeleteRequest deleteRequest , Authentication authentication) {
        techLogService.deleteTechLog(userId,deleteRequest,authentication);
        return ResponseEntity.ok(ApiResponse.of(200, "글 삭제가 완료되었습니다", null));    }

}

