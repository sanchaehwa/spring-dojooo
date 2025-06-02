package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.domain.CheckListTag;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CheckListResponse {

    private final LocalDate date;
    private final List<ChecklistItem> checklists;

    public static CheckListResponse from(LocalDate date, List<CheckList> entities) {
        return CheckListResponse.builder()
                .date(date)
                .checklists(entities.stream()
                        .map(ChecklistItem::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public static CheckListResponse from(CheckList entity) {
        return CheckListResponse.builder()
                .date(entity.getStartDate())  // 혹은 entity.getEndDate(), 원하는 날짜 기준에 맞게
                .checklists(List.of(ChecklistItem.from(entity)))
                .build();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class ChecklistItem {
        private final Long checklistId;
        private final String task;
        private final String todoState;
        private final TagDto tag;

        public static ChecklistItem from(CheckList entity) {
            return ChecklistItem.builder()
                    .checklistId(entity.getId())
                    .task(entity.getTask())
                    .todoState(entity.getTodoState().name())
                    .tag(TagDto.from(entity.getChecklistTag()))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class TagDto {
        private final Long tagId;
        private final String tagName;
        private final String colorCode;

        public static TagDto from(CheckListTag tag) {
            if (tag == null) return null;
            return TagDto.builder()
                    .tagId(tag.getId())
                    .tagName(tag.getTagName())
                    .colorCode(tag.getColorCode())
                    .build();
        }
    }
}
