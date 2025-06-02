package org.spring.dojooo.main.contents.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.contents.model.TodoState;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CheckListUpdateRequest {
    private Long checkListId;
    private Long userId;

    @NotBlank(message = "할일을 입력해주세요")
    private String task;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long tagId; //사용자가 고른 테그
    private TodoState todoState;

    public CheckList toEntity(User user, CheckListTag tag) {
        return CheckList.builder()
                .task(task)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .todoState(todoState)
                .checklistTag(tag)
                .build();
    }
}
