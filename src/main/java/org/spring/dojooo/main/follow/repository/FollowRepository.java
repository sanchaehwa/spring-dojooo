package org.spring.dojooo.main.follow.repository;

import org.spring.dojooo.main.follow.domain.Follow;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 나를 팔로우하는 사람 수 (Follower 수)
    int countByToUser(User user);

    // 내가 팔로우하는 사람 수 (Following 수)
    int countByFromUser(User user);

    // 팔로워 리스트
    List<Follow> findByToUser(User user);

    // 팔로잉 리스트
    List<Follow> findByFromUser(User user);

    // 팔로우 관계 존재 여부
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);
}
