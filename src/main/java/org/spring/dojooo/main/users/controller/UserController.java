package org.spring.dojooo.main.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.spring.dojooo.global.response.ApiResponse;
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
public class UserController {
    private final UserService userService;
    //회원가입 요청
    @Operation(summary = "회원가입", description = "회원 가입 정보를 전달받아 회원 가입 합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createUser(@Validated @RequestBody UserSignUpRequest userSignUpRequest) {
        return ResponseEntity.ok(ApiResponse.of(userService.saveUser(userSignUpRequest)));
    }
}
/*
소셜 로그인 -> 소셜 로그인 타입 넣어줘야하고
회원 가입 -> SuperAdmin 이 Admin 부여 할수 있게할건데.
 */
