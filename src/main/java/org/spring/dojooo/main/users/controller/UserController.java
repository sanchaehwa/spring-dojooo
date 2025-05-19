package org.spring.dojooo.main.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.Redis.RedisUtil;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.ApiException;
import org.spring.dojooo.global.response.ApiResponse;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.UserLoginRequest;
import org.spring.dojooo.main.users.dto.UserLoginResponse;
import org.spring.dojooo.main.users.dto.UserSignUpRequest;
import org.spring.dojooo.main.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final RedisUtil redisUtil;

    //회원가입 요청
    @Operation(summary = "회원가입", description = "회원 가입 정보를 전달받아 회원 가입 합니다")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> createUser(@Validated @RequestBody UserSignUpRequest userSignUpRequest) {
       String email = userSignUpRequest.getEmail();
        if (!redisUtil.isVerifiedEmail(email)) {
            throw new ApiException(ErrorCode.EMAIL_BAD_REQUEST);
        }
        Long userId = userService.saveUser(userSignUpRequest);

        redisUtil.deleteEmailCode("verified:" + email);
        log.info("회원가입 성공, userId = {}", userId);
        return ResponseEntity.ok(ApiResponse.of(userId));
    }


}
/*
소셜 로그인 -> 소셜 로그인 타입 넣어줘야하고
회원 가입 -> SuperAdmin 이 Admin 부여 할수 있게할건데.
 */
