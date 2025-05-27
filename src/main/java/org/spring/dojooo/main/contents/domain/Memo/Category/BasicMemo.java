package org.spring.dojooo.main.contents.domain.Memo.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.spring.dojooo.main.contents.domain.Memo.Memo;

//Memo의 서브 클래스 - StudyMemo
@Entity
public class BasicMemo extends Memo {
    @Column(length = 45)
    private String title;

    @Column(columnDefinition = "TEXT" )
    private String content;

    //summary

    @Column
    private String fileUrl;
}
