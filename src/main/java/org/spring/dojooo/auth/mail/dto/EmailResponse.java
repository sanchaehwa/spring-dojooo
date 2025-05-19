package org.spring.dojooo.auth.mail.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class EmailResponse {
    private String code;
    private String message;
}
