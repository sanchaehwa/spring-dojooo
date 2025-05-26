package org.spring.dojooo.main.contents.domain.Memo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.main.contents.model.MemoCategory;
import org.spring.dojooo.main.users.domain.User;

@Getter
@Entity
@Table(name="memo") //UserId 로 각 User의 메모를 구분
@NoArgsConstructor
@AllArgsConstructor

public class Memo {
    @Id
    @Column(name="memo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column
    private MemoCategory category;

    @Column
    private String content;






}
