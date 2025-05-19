package org.spring.dojooo.main.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.spring.dojooo.main.users.model.Role;

@Getter
@AllArgsConstructor
@Builder
public class UserLoginResponse {
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;

    private String accessToken;
}
