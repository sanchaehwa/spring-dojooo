package org.spring.dojooo.main.contents.domain;
import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.main.contents.model.TodoState;
import org.spring.dojooo.main.users.domain.User;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="check_list")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="checklist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;


    @Column(nullable = false, length = 50)
    private String task;

    @Enumerated(EnumType.STRING)
    @Column
    private TodoState todoState;

    @Column
    private Date schedule;

    @Column
    private boolean isDeleted;

    @Builder
    public CheckList(User user, String task, TodoState todoState, Date schedule, Boolean isDeleted) {
        this.user = user;
        this.task = task;
        this.todoState = todoState != null ? todoState : TodoState.TODO;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.schedule = new Date();

    }


}
