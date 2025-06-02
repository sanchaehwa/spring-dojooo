package org.spring.dojooo.main.contents.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class CalendarChecklistSummary {
    private final LocalDate date;
    private final int totalCount;
    private final int doneCount;

}
