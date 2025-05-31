package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.users.domain.User;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CheckListDetailsList {
    private Long userId;
    private List<CheckListTagDetails> checkListTags;

    public static CheckListDetailsList from(User user, List<CheckListTag>checklistTags) {
        List<CheckListTagDetails> checkListDetailsList = checklistTags.stream()
                .map(checklistTag -> CheckListTagDetails.from(user, checklistTag))
                .toList();
        return CheckListDetailsList.builder()
                .userId(user.getId())
                .checkListTags(checkListDetailsList)
                .build();

    }

}
