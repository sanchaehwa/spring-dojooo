package org.spring.dojooo.main.users.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Column
    private String tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(columnDefinition = "TINYINT default 0",name="is_deleted")
    private boolean isDeleted;

    @Column(columnDefinition = "TINYINT default 0",name="show_on_profile")
    private boolean showOnProfile;

    @Builder
    public ProfileTag(String colorCode, String tagName, User user, Boolean isDeleted, Boolean showOnProfile) {
        this.colorCode = colorCode;
        this.tagName = tagName;
        this.user = user;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.showOnProfile = showOnProfile != null ? showOnProfile : false;
    }

    public void updateColorCode(String colorCode){
        if(colorCode != null && !colorCode.isBlank()){
            this.colorCode = colorCode;
        }
    }
    public void updateTagName(String tagName){
        if(tagName != null && !tagName.isBlank()){
            this.tagName = tagName;
        }
    }
    public void settingShowOnProfile(boolean showOnProfile){
        this.showOnProfile = showOnProfile;
    }

    public void setUserInternal(User user) {
        this.user = user;
    }



}
