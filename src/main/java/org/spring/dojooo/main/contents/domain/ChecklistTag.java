package org.spring.dojooo.main.contents.domain;

import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.main.users.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChecklistTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="checklist_tag_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String tagName;

    private String colorCode;

    private boolean isChecklistShow;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public ChecklistTag(String tagName, String colorCode, Boolean isChecklistShow,User user) {
        this.tagName = tagName;
        this.colorCode = colorCode;
        this.isChecklistShow = isChecklistShow != null ? isChecklistShow : false;
        this.user = user;
    }

}
