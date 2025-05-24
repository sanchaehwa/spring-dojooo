package org.spring.dojooo.main.users.domain;

import jakarta.persistence.*;
import lombok.*;


@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Profile {

    @Column(name ="profile_image")
    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String introduction;
}
