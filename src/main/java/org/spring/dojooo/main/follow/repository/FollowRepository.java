package org.spring.dojooo.main.follow.repository;

import org.spring.dojooo.main.follow.domain.Follow;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 나를 팔로우하는 사람 수 (Follower 수)
    int countByFollowing(User user);
    // 내가 팔로우하는 사람 수 (Following 수)
    int countByFollower(User user);
    // 팔로워 리스트
    List<Follow> findByFollowing(User user);
    // 팔로잉 리스트
    List<Follow> findByFollower(User user);
    //팔로우 찾기
//    @Query("select f from Follow f where f.follower = :from and f.following = :to")
//    Optional<Follow> findFollow(@Param("from")User follower, @Param("to")User following );
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

}
