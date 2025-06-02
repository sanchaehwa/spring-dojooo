package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.global.domain.HasTagName;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class CheckListTagRequest implements HasTagName {
    private String tagName;

    private String colorCode;

    private String isChecklistTagShow;

    @Override
    public String getTagName() {
        return tagName;
    }

}
