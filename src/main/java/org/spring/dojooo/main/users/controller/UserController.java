package org.spring.dojooo.main.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.global.Redis.RedisUtil;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.response.ApiResponse;

import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.UserResponse;
import org.spring.dojooo.main.users.dto.UserSignUpRequest;
import org.spring.dojooo.main.users.dto.UserUpdateRequest;
import org.spring.dojooo.main.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final RedisUtil redisUtil;

    //회원가입 요청
    @Operation(summary = "회원가입", description = "회원가입은 이메일 인증 후 10분 이내에 완료되어야 하며, 이메일 인증 코드는 발송 후 5분 이내에 입력해야 유효합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> createUser(@Validated @RequestBody UserSignUpRequest userSignUpRequest) {
        log.info("회원가입 성공");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "회원가입이 완료되었습니다", userService.saveUser(userSignUpRequest)));
    }

    @Operation(summary = "회원 조회", description = "사용자의 ID를 전달받아 회원 조회를 합니다")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findUserById(@PathVariable Long id) { //@PathWariable 동적으로 URL에 정보를 담을 수 있음.
        User user = userService.findUserById(id);
        return ResponseEntity.ok(ApiResponse.ok(UserResponse.from(user)));
    }

    //회원 정보 수정
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정하려는 회원의 ID를 받아 회원정보 수정을 합니다")
    @PatchMapping("update/{id}")
    public ResponseEntity<ApiResponse<Long>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        return ResponseEntity.ok(ApiResponse.of(200, "회원 정보 수정이 완료되었습니다", userService.updateUser(id, userUpdateRequest)));

    }
    //로그아웃
    @Operation(summary = "로그아웃", description = "로그아웃 시, Refresh Token도 삭제합니다")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            String email = customUserDetails.getUsername();

            // Redis Refresh Token 삭제
            redisUtil.deleteRefreshToken(email);

            log.info("사용자 로그아웃 성공 - {}", email);

            // SecurityContext 초기화
            SecurityContextHolder.clearContext();


            return ResponseEntity.ok(ApiResponse.of(200, "로그아웃이 성공적으로 처리되었습니다", email));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.of(401, "인증되지 않은 사용자입니다.", null));    
    }
    //회원 탈퇴
    @Operation(summary = "회원 탈퇴",description = "회원 탈퇴하고자하는 사용자의 정보를 받아, 탈퇴처리합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(200,"회원 탈퇴가 완료되었습니다",userService.deleteUser(id)));
    }


}
