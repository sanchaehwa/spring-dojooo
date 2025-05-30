package org.spring.dojooo.main.contents.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.model.TodoState;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
public class CheckListRequest {
    private Long userId;

    @NotBlank(message = "할일을 입력해주세요")
    private String task;

    private TodoState todoState;

    private LocalDate scheduleDate;

    private Long tagId; //사용자가 고른 테그

    CheckList toEntity(User user) {
        return CheckList.builder()
                .user(user)
                .task(task)
                .todoState(todoState)
                .isDeleted(false)
                .schedule(scheduleDate)
                .build();

    }
}
