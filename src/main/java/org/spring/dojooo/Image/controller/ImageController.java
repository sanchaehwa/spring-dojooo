package org.spring.dojooo.Image.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.Image.Service.ImageService;
import org.spring.dojooo.Image.model.TechLogImageType;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "프로필 사진 업로드")
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {

        String email = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();

        log.info("프로필 이미지 업로드 요청 - 사용자: {}", email);
        String url = imageService.uploadProfileImage(file, userId, email, auth);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "프로필 사진 업로드가 완료되었습니다", url));
    }

    @Operation(summary="썸네일용 사진 업로드")
    @PostMapping(value = "/techlog/thumbnail/{techLogId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadTechlogThumbnail(
            @RequestParam("file") MultipartFile file,
            @PathVariable("techLogId") Long techLogId,
            Authentication auth
    ) {
        return uploadTechlogThumbnail(file, auth, TechLogImageType.THUMBNAIL, techLogId, "썸네일 업로드 완료");
    }

    @Operation(summary = "기술일지 본문 이미지 업로드")
    @PostMapping(value = "/techlog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadTechlogContentImage(
            @RequestParam("file") MultipartFile file,
            Authentication auth
    ) {
        return uploadTechlogContent(file, auth, TechLogImageType.CONTENT, "본문 이미지 업로드 완료");
    }

    private ResponseEntity<ApiResponse<String>> uploadTechlogThumbnail(
            MultipartFile file,
            Authentication auth,
            TechLogImageType type,
            Long techLogId,
            String successMessage
    ) {
        String email = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();

        log.info("{} 업로드 요청 - 사용자: {}, techLogId: {}", type, email, techLogId);
        String url = imageService.uploadTechLogImage(file, userId, email, TechLogImageType.THUMBNAIL, techLogId, auth);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, successMessage, url));
    }

    private ResponseEntity<ApiResponse<String>> uploadTechlogContent(
            MultipartFile file,
            Authentication auth,
            TechLogImageType type,
            String successMessage
    ) {
        String email = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();

        log.info("{} 업로드 요청 - 사용자: {}", type, email);
        String url = imageService.uploadTechLogImage(file, userId, email, TechLogImageType.CONTENT, null, auth);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, successMessage, url));
    }
}
