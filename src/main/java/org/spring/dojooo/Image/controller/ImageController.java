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
@RequestMapping("/upload") //1. POST /upload/images -> MulipartFile * S3에 이미지 저장만 하고 URl 넘김
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "프로필 사진 업로드", description = "사용자가 마이페이지에서 프로필 사진을 등록할 수 있습니다")
    @PostMapping(value = "/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //consumers : form-data 형식으로 데이터를 보내야만 이 매서드 실행
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); //현재 로그인한 사용자의 정보 추출
        String email = userDetails.getUsername();

        log.info("프로필 이미지 업로드 - 사용자: {}", email);
        String url = imageService.uploadProfileImage(file, email);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "프로필 사진 업로드가 완료되었습니다", url)); //프로필 이미지 업로드 -> S3 업로드 -> url(image url Return)
    }

}
