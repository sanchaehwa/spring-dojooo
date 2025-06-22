package org.spring.dojooo.main.follow.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.follow.domain.Follow;
import org.spring.dojooo.main.follow.dto.FollowerUserResponse;
import org.spring.dojooo.main.follow.dto.FollowingUserResponse;
import org.spring.dojooo.main.follow.exception.DuplicateFollowException;
import org.spring.dojooo.main.follow.exception.FollowInvalidRequestException;
import org.spring.dojooo.main.follow.repository.FollowRepository;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.NotUserEqualsCurrentUserException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    // 팔로우
    @Transactional
    public Follow addFollow(User toUser, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User fromUser = customUserDetails.getUser();

        if (fromUser.equals(toUser)) {
            throw new FollowInvalidRequestException(ErrorCode.FOLLOW_INVALID_REQUEST);
        }
        if (followRepository.findByFromUserAndToUser(fromUser, toUser).isPresent()) {
            throw new DuplicateFollowException(ErrorCode.FOLLOW_DUPLICATED);
        }

        Follow follow = Follow.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();

        return followRepository.save(follow);
    }

    // 팔로워 목록 조회
    @Transactional(readOnly = true)
    public List<FollowerUserResponse> getFollowerList(User selectedUser, User requestUser) {
        List<User> followers = followRepository.findByToUser(selectedUser).stream()
                .map(Follow::getFromUser)
                .toList();

        return followers.stream()
                .map(user -> {
                    boolean isFollowing = followRepository.findByFromUserAndToUser(requestUser, user).isPresent();
                    return FollowerUserResponse.from(user, isFollowing);
                })
                .toList();
    }

    // 팔로잉 목록 조회
    @Transactional(readOnly = true)
    public List<FollowingUserResponse> getFollowingList(User selectedUser, User requestUser) {
        List<User> following = followRepository.findByFromUser(selectedUser).stream()
                .map(Follow::getToUser)
                .toList();

        return following.stream()
                .map(user -> {
                    boolean isFollowing = followRepository.findByFromUserAndToUser(requestUser, user).isPresent();
                    return FollowingUserResponse.from(user, isFollowing);
                })
                .toList();
    }

    // 언팔로우
    @Transactional
    public void unfollow(User toUser,Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User fromUser = customUserDetails.getUser();
        Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new FollowInvalidRequestException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }

}
