package org.spring.dojooo.main.contents.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class CheckListTagRegisterInCategoryRequest {
    private List<String> tags;
    private boolean isChecklistTagShow;
}
