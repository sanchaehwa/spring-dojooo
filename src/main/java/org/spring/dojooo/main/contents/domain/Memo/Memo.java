package org.spring.dojooo.main.contents.domain.Memo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.main.contents.model.MemoCategory;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="memo") //UserId 로 각 User의 메모를 구분
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

public class Memo {
    @Id
    @Column(name="memo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemoCategory category;

    @Embedded
    private Tag tag;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false,columnDefinition = "TINYINT default 0") //깁
    private boolean isDeleted; //삭제 여부

    @Column(columnDefinition = "TINYINT default 0")
    private boolean isRead;  //boolean 자체가 null 안되니깐 nullable = false는 사실상 의미 없음 Boolean 으로 쓴다면 Null 허용이라 설정해줘야하고

    @Builder
    public Memo(User user, MemoCategory category, Tag tag, Boolean isDeleted, Boolean isRead, LocalDateTime createdAt) {
        this.user = user;
        this.category = category;
        this.tag = new Tag(tag.getTagName(), tag.isShowOnProfile());
        this.isDeleted = false;
        this.isRead = false;
        this.createdAt = createdAt;
    }



}
