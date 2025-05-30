package org.spring.dojooo.global.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.main.users.domain.User;

//사용자가 추가하고 삭제할 수있도록 설계 하기위해
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false,length=10)
    private String tagName;

    public Tag(String tagName, User user) {
        this.tagName = tagName;
        this.user = user;
    }

    public void updateTagName(String tagName) {
        this.tagName = tagName;
    }
}

