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


}
/*
소셜 로그인 -> 소셜 로그인 타입 넣어줘야하고
회원 가입 -> SuperAdmin 이 Admin 부여 할수 있게할건데.
 */
