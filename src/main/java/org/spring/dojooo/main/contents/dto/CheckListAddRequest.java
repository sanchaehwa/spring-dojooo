package org.spring.dojooo.main.contents.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDate;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CheckListAddRequest {
    private Long checkListId;
    private Long userId;

    @NotBlank(message = "할일을 입력해주세요")
    private String task;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long tagId; //사용자가 고른 테그

    public CheckList toEntity(User user, CheckListTag tag) {
        return CheckList.builder()
                .task(task)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .checklistTag(tag)
                .build();
    }


}
