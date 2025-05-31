package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.global.domain.HasTagName;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
public class CheckListTagRequest implements HasTagName {
    private String tagName;

    private String colorCode;

    private String isChecklistShow;

    @Builder
    public CheckListTagRequest(String tagName, String colorCode, String isChecklistShow) {
        this.tagName = tagName;
        this.colorCode = colorCode;
        this.isChecklistShow = isChecklistShow;
    }
    @Override
    public String getTagName() {
        return tagName;
    }

}
