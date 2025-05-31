package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.model.TodoState;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckListResponse {

    private Long checklistId;
    private String task;
    private TodoState todoState;
    private boolean isDeleted;
    private Long tagId;
    private LocalDate scheduledDate;

    public static CheckListResponse from(CheckList checkList, Long tagId) {
        return CheckListResponse.builder()
                .checklistId(checkList.getId())
                .task(checkList.getTask())
                .todoState(checkList.getTodoState())
                .isDeleted(checkList.isDeleted())
                .scheduledDate(checkList.getSchedule())
                .tagId(tagId)
                .build();
    }
}



