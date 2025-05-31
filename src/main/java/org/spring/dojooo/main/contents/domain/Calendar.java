package org.spring.dojooo.main.contents.domain;

import java.time.LocalDate;

public record Calendar (
    LocalDate date,
    long checklistCount
){
    public static Calendar of(LocalDate date,long checklistCount) {
        return new Calendar(date,checklistCount);
    }//of : 직접 인자를 받고, 새 인스턴스를 만든다는것.
}
