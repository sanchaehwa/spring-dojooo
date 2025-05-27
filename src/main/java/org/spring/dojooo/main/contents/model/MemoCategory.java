package org.spring.dojooo.main.contents.model;

import lombok.*;

//고정된카테고리
@AllArgsConstructor
@Getter
public enum MemoCategory {
    DIARY("일기"),
    MEMO("메모"),
    TODO("할일");

    private final String displayName;

}
