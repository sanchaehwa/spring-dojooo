package org.spring.dojooo.main.contents.domain;
import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.main.contents.model.TodoState;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name="check_list")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="checklist_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String task;

    @Enumerated(EnumType.STRING)
    private TodoState todoState;

    @Column
    private LocalDate schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChecklistTag checklistTag;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private boolean isDeleted;

    @Builder
    public CheckList( String task, TodoState todoState, LocalDate schedule, ChecklistTag checklistTag, Boolean isDeleted,User user) {
        this.task = task;
        this.todoState = todoState != null ? todoState : TodoState.TODO;
        this.schedule = schedule;
        this.checklistTag = checklistTag;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.user = user;
    }


}
