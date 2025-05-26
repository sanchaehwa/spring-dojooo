package org.spring.dojooo.main.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import org.spring.dojooo.main.users.domain.User;

@Getter
public class UserSignUpRequest {

    @NotBlank(message = "닉네임을 입력헤주세요")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message ="이메일을 올바르게 입력해주세요")
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
