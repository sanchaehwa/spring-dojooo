package org.spring.dojooo.main.contents.dto;
//단일 테그
import lombok.*;
import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.users.domain.*;
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class CheckListTagDetails {
    private Long id;
    private String tagName;
    private String colorCode;
    private boolean isDeleted;
    private boolean isChecklistShow;

    public static CheckListTagDetails from(User user, CheckListTag checkListTag) {
        return CheckListTagDetails.builder()
                .id(checkListTag.getId())
                .tagName(checkListTag.getTagName())
                .colorCode(checkListTag.getColorCode())
                .isDeleted(checkListTag.isDeleted())
                .isChecklistShow(checkListTag.isChecklistShow())
                .build();
    }
}
