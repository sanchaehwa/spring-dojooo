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
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    @Builder
    public Follow(User fromUser, User toUser){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.createdAt = LocalDateTime.now();
    }
    public void setFromUser(User user) {
        this.fromUser = user;
    }

    public void setToUser(User user) {
        this.toUser = user;
    }


}
