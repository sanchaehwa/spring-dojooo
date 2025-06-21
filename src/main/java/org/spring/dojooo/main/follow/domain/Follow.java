package org.spring.dojooo.main.follow.domain;

import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.main.users.domain.User;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "follower_id", nullable = false)
    private User follower;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public void addFollower(User follower) {
        this.follower = follower;
    }
    public void addFollowing(User following) {
        this.following = following;
    }


}
