package org.spring.dojooo.auth.mail.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class EmailMessage {
    private String to; //수신자
    private String subject; //메일 제목
    private String message; //메일 내용
}
