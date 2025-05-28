package org.spring.dojooo.main.contents.domain;

import jakarta.persistence.*;
import org.spring.dojooo.global.domain.Tag;
import org.spring.dojooo.main.users.domain.User;

@Entity
public class ChecklistTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="checklist_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name="checklist_id")
    private CheckList checklist;

}
