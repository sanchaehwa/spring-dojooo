package org.spring.dojooo.main.contents.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CheckListPageResponse {
    private List<CalendarChecklistSummary> calendarSummary; // 왼쪽 달력
    private CheckListResponse todayChecklist; // 오른쪽 오늘의 할 일
}
