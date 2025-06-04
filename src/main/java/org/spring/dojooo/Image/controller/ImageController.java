package org.spring.dojooo.Image.controller;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.Image.Service.ImageService;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "프로필 사진 업로드")
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(@RequestParam("file") MultipartFile file, Authentication auth) throws IOException {
        String email = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        log.info("프로필 이미지 업로드 - 사용자: {}", email);
        String url = imageService.uploadImage(file, email, "profile");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "프로필 사진 업로드가 완료되었습니다", url));
    }

    @Operation(summary = "기술일지 썸네일 업로드")
    @PostMapping(value = "/techlog/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadTechlogThumbnail(@RequestParam("file") MultipartFile file, Authentication auth) throws IOException {
        String email = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        log.info("기술일지 썸네일 업로드 - 사용자: {}", email);
        String url = imageService.uploadImage(file, email, "techlog-thumbnail");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "썸네일 업로드 완료", url));
    }

    @Operation(summary = "기술일지 본문 이미지 업로드")
    @PostMapping(value = "/techlog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadTechlogContentImage(@RequestParam("file") MultipartFile file, Authentication auth) {
        String email = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        log.info("기술일지 본문 이미지 업로드 - 사용자: {}", email);
        String url = imageService.uploadImage(file, email, "techlog");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "본문 이미지 업로드 완료", url));
    }
}
