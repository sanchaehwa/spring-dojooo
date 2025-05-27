package org.spring.dojooo.main.contents.domain.Memo.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.spring.dojooo.main.contents.domain.Memo.Memo;
import org.spring.dojooo.main.contents.model.TodoState;

@Entity
public class ChecklistMemo extends Memo {

    @Column(nullable = false)
    private TodoState todoState;

    @Column(nullable = false, length = 50)
    private String task;

    @Column(nullable=false)
    private Boolean completed;


}
