package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.global.domain.HasTagName;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class CheckListTagRequest implements HasTagName {
    private String tagName;

    private String colorCode;

    private String isChecklistShow;

    @Override
    public String getTagName() {
        return tagName;
    }

}
