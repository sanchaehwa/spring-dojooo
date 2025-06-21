package org.spring.dojooo.main.follow.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.follow.domain.Follow;
import org.spring.dojooo.main.follow.dto.FollowerUserResponse;
import org.spring.dojooo.main.follow.dto.FollowingUserResponse;
import org.spring.dojooo.main.follow.exception.DuplicateFollowException;
import org.spring.dojooo.main.follow.exception.FollowInvalidRequestException;
import org.spring.dojooo.main.follow.repository.FollowRepository;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    //팔로우
    @Transactional
    public Follow addFollow(User follower, User following) {
        //본인은 팔로우 못함
        if (follower.equals(following)) {
            throw new FollowInvalidRequestException(ErrorCode.FOLLOW_INVALID_REQUEST);
        }
        //중복으로 팔로우 하는 경우
        if (followRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            throw new DuplicateFollowException(ErrorCode.FOLLOW_DUPLICATED);
        }
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        return followRepository.save(follow);
    }
    //팔로워 조회
    @Transactional(readOnly = true)
    public List<FollowerUserResponse> getFollowerList(User targetUser, User currentUser) {
        List<User> followers = followRepository.findByFollowing(targetUser).stream()
                .map(Follow::getFollower)
                .toList();

        return followers.stream()
                .map(user -> {
                    boolean isFollowing = followRepository.findByFollowerAndFollowing(currentUser, user).isPresent();
                    return FollowerUserResponse.from(user, isFollowing);
                })
                .toList();
    }
    //팔로잉 조회
    @Transactional(readOnly = true)
    public List<FollowingUserResponse> getFollowingList(User targetUser, User currentUser) {
        List<User> following = followRepository.findByFollower(targetUser).stream()
                .map(Follow::getFollowing)
                .toList();

        return following.stream()
                .map(user -> {
                    boolean isFollowing = followRepository.findByFollowerAndFollowing(currentUser, user).isPresent();
                    return FollowingUserResponse.from(user, isFollowing);
                })
                .toList();
    }

    //팔로잉 삭제 -> 이건 본인만 가능




}
