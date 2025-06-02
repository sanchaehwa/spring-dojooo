package org.spring.dojooo.main.contents.domain;

import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.main.users.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckListTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="checklist_tag_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String tagName;

    @Column(name = "color_code")
    private String colorCode;

    @Column
    private boolean isDeleted;

    @Column
    private boolean isChecklistTagShow;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public CheckListTag(String tagName, String colorCode, Boolean isDeleted, Boolean isChecklistTagShow, User user) {
        this.tagName = tagName;
        this.colorCode = colorCode;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.isChecklistTagShow = isChecklistTagShow != null ? isChecklistTagShow : false;
        this.user = user;
    }
    public void updateCheckListTagName(String tagName) {
        if(tagName != null && !tagName.isEmpty()) {
            this.tagName = tagName;
        }
    }
    public void updateColorCode(String colorCode) {
        if (colorCode != null && !colorCode.isEmpty() && !colorCode.equals(this.colorCode)) {
            this.colorCode = colorCode;
        }
    }

    public void setChecklistShow(boolean isChecklistTagShow) {
        this.isChecklistTagShow = isChecklistTagShow;
    }
}
