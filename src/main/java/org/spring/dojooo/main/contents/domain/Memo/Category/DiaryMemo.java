package org.spring.dojooo.main.contents.domain.Memo.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.spring.dojooo.main.contents.domain.Memo.Memo;

@Entity
public class DiaryMemo extends Memo {
    //Weather

    @Column(columnDefinition = "TEXT")
    private String content;

}
