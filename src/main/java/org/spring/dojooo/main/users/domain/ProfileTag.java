package org.spring.dojooo.main.users.domain;

import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.global.domain.Tag;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ProfileTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_tag_id")
    private Long id;

    @Column
    private String colorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(columnDefinition = "TINYINT default 0",name="is_deleted")
    private boolean isDeleted;

    @Column(columnDefinition = "TINYINT default 0",name="show_on_profile")
    private boolean showOnProfile;

    @Builder
    public ProfileTag(String colorCode, Tag tag, User user, Boolean isDeleted, Boolean showOnProfile) {
        this.colorCode = colorCode;
        this.tag = tag;
        this.user = user;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.showOnProfile = showOnProfile != null ? showOnProfile : false;
    }
    private void updateTagName(String newTagName) {
        if(newTagName != null && !newTagName.isEmpty()) {
            this.tag.updateTagName(newTagName);
        }
    }


    public void updateColorCode(String colorCode){
        if(colorCode != null && !colorCode.isBlank()){
            this.colorCode = colorCode;
        }
    }
    public void deleteTag(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public void settingShowOnProfile(boolean showOnProfile){
        this.showOnProfile = showOnProfile;
    }





}
