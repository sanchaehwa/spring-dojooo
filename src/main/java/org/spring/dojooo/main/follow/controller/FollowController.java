package org.spring.dojooo.main.follow.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.global.response.ApiResponse;
import org.spring.dojooo.main.follow.dto.FollowResponse;
import org.spring.dojooo.main.follow.dto.FollowerUserResponse;
import org.spring.dojooo.main.follow.dto.FollowingUserResponse;
import org.spring.dojooo.main.follow.service.FollowService;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @Operation(summary = "팔로우 신청", description = "본인을 제외한 다른 유저에게 팔로우 신청을 할 수 있습니다.")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<FollowResponse>> addFollow(@RequestParam Long toUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User toUser = userService.findUserById(toUserId);
        followService.addFollow(toUser,authentication);
        FollowResponse response = FollowResponse.from(toUser);

        return ResponseEntity.status(201).body(ApiResponse.created(response));
    }

    @Operation(summary = "언팔로우", description = "팔로우한 유저를 언팔로우합니다.")
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> removeFollow(
            @RequestParam Long toUserId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User toUser = userService.findUserById(toUserId);
        followService.unfollow(toUser,authentication);
        return ResponseEntity.ok(ApiResponse.ok("언팔로우 완료"));
    }

    @Operation(summary = "팔로워 목록 조회", description = "특정 유저를 팔로우하는 유저 목록을 조회합니다.")
    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<List<FollowerUserResponse>>> getFollowerList(
            @RequestParam Long userId,
            @AuthenticationPrincipal User currentUser) {

        User selectedUser = userService.findUserById(userId);
        List<FollowerUserResponse> response = followService.getFollowerList(selectedUser, currentUser);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "팔로잉 목록 조회", description = "특정 유저가 팔로우하는 유저 목록을 조회합니다.")
    @GetMapping("/followings")
    public ResponseEntity<ApiResponse<List<FollowingUserResponse>>> getFollowingList(
            @RequestParam Long userId,
            @AuthenticationPrincipal User currentUser) {

        User selectedUser = userService.findUserById(userId);
        List<FollowingUserResponse> response = followService.getFollowingList(selectedUser, currentUser);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

}
