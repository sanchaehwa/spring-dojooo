package org.spring.dojooo.main.contents.domain.Memo;

import jakarta.persistence.*;
import lombok.*;

//사용자가 추가하고 삭제할 수있도록 설계 하기위해
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class Tag {

    @Column(name="tag_name",nullable = false,length=45)
    private String tagName;

    @Column(name="show_on_profile")
    private boolean showOnProfile; //프로필에 표시할 테그

    public Tag withShowOnProfile(boolean showOnProfile) {
        return new Tag(this.tagName, showOnProfile);
    }


}
