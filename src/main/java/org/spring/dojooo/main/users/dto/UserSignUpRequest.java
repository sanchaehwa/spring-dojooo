package org.spring.dojooo.main.users.dto;

import lombok.Data;
import lombok.Getter;
import org.spring.dojooo.main.users.domain.User;

@Getter
public class UserSignUpRequest {
    private String nickname;
    private String password;
    private String email;

    public User toEntity(){
        return User
                .builder()
                .nickname(nickname)
                .password(password)
                .email(email)
                .build();
    }



}
