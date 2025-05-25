package org.spring.dojooo.main.users.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.ApiException;
import org.spring.dojooo.global.response.ApiResponse;
import org.spring.dojooo.main.users.dto.ProfileDetails;
import org.spring.dojooo.main.users.dto.ProfileEditRequest;
import org.spring.dojooo.main.users.dto.ProfileSaveRequest;
import org.spring.dojooo.main.users.exception.WrongUserEditException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.spring.dojooo.main.users.service.UserProfileService;
import org.spring.dojooo.main.users.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.annotation.Repeatable;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserService userService;

    //아무것도 등록안한 상태이면 -> (기본)프로필 사진 , 회원가입시 등록했던 이름, (빈칸)자기소개
    @Operation(summary = "유저 페이지 프로필 조회", description = "해당 유저의 프로필 정보를 조회합니다")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ProfileDetails>> userProfile(@PathVariable Long userId, Authentication authentication) {
        ProfileDetails profileDetails = userProfileService.getProfile(userId,authentication); //Id로 프로필 조회 - 현재 인증된 사용자 정보 전달
        return ResponseEntity.ok(ApiResponse.ok(profileDetails));
    }

    @Operation(summary = "유저 페이지 프로필 수정", description = "해당 유저의 자기소개를 수정합니다")
    @PatchMapping(value = "/edit/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileDetails>> editProfile(
            @PathVariable Long userId,
            @RequestPart(value = "introduction", required = false) String introduction,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            Authentication authentication) {

        ProfileEditRequest request = new ProfileEditRequest(introduction, profileImage);
        userProfileService.editProfile(userId, request, authentication);
        ProfileDetails updated = userProfileService.getProfile(userId, authentication);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @Operation(summary = "유저 페이지 프로필 저장", description = "S3에서 업로드된 이미지 URL과 자기소개를 저장합니다")
    @PostMapping("/save/{userId}")
    public ResponseEntity<ApiResponse<ProfileDetails>> saveProfile(
            @PathVariable Long userId,
            @RequestBody ProfileSaveRequest profileSaveRequest,
            Authentication authentication) {

        userProfileService.saveProfile(userId, profileSaveRequest, authentication);
        ProfileDetails profileDetails = userProfileService.getProfile(userId, authentication);
        return ResponseEntity.ok(ApiResponse.ok(profileDetails));
    }
}
