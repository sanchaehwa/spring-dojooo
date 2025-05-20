package org.spring.dojooo.main.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.Redis.RedisUtil;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.ApiException;
import org.spring.dojooo.global.response.ApiResponse;

import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.UserSignUpRequest;
import org.spring.dojooo.main.users.dto.UserUpdateRequest;
import org.spring.dojooo.main.users.service.UserService;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(ApiResponse.of(201,"회원가입이 완료되었습니다",userId));
    }

    @Operation(summary="회원 조회", description = "사용자의 ID를 전달받아 회원 조회를 합니다")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findUserById(@PathVariable Long id) { //@PathWariable 동적으로 URL에 정보를 담을 수 있음.
        User user = userService.findUserById(id);
        return ResponseEntity.ok(ApiResponse.ok(user));

    }
    //회원 정보 수정
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정하려는 회원의 ID를 받아 회원정보 수정을 합니다")
    @PatchMapping("update/{id}")
    public ResponseEntity<ApiResponse<Long>> updateUser (
            @PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        return ResponseEntity.ok(ApiResponse.of(200, "회원 정보 수정이 완료되었습니다", userService.updateUser(id,userUpdateRequest)));
    }



}
