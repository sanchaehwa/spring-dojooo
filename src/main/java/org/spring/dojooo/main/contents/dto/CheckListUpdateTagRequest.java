package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.global.domain.HasTagName;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckListUpdateTagRequest implements HasTagName {
    private Long userId;

    private String originalCheckListTagName;
    private String newCheckListTagName;

    private String originalCheckListColorCode;
    private String newCheckListColorCode;

    @Override
    public String getTagName() {
        return newCheckListTagName != null ? newCheckListTagName : originalCheckListTagName;
    }

}
