package org.spring.dojooo.auth.mail.dto;

import lombok.*;
@Getter

public class EmailVerifyRequest {
    private String email;
    private String code;
}
