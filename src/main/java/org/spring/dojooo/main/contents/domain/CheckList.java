package org.spring.dojooo.main.contents.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spring.dojooo.main.contents.model.TodoState;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDate;

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

    @Column(nullable = false, length = 50)
    private String task;

    @Enumerated(EnumType.STRING)
    private TodoState todoState;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_tag_id")
    private CheckListTag checklistTag;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private boolean isDeleted;

    @Builder
    public CheckList(String task, TodoState todoState, LocalDate startDate, LocalDate endDate, CheckListTag checklistTag, Boolean isDeleted, User user) {
        this.task = task;
        this.todoState = todoState != null ? todoState : TodoState.TODO;
        this.startDate = startDate;
        this.endDate = endDate;
        this.checklistTag = checklistTag;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.user = user;
    }
    public void updateTask(String task){
        this.task = task;
    }
    public void updateTodoState(TodoState todoState){
        this.todoState = todoState;
    }
    public void updateDateRange(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public void updateTag(CheckListTag tag) {
        this.checklistTag = tag;
    }

}
