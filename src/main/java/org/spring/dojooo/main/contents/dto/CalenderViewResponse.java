package org.spring.dojooo.main.contents.dto;


import org.spring.dojooo.main.contents.domain.Calendar;

import java.util.List;

public record CalenderViewResponse (
    Long userId,
    int year,
    int month,
    List<String> tags,
    List<Calendar> calendar
){
    public static CalenderViewResponse of(
            Long id,
            int year,
            int month,
            List<String>tags,
            List<Calendar> calendar
    ) {
        return new CalenderViewResponse(id, year, month, tags, calendar);
    }
}
