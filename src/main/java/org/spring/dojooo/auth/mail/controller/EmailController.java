package org.spring.dojooo.auth.mail.controller;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.Redis.RedisUtil;
import org.spring.dojooo.auth.mail.dto.EmailMessage;
import org.spring.dojooo.auth.mail.dto.EmailPost;
import org.spring.dojooo.auth.mail.dto.EmailResponse;
import org.spring.dojooo.auth.mail.dto.EmailVerifyRequest;
import org.spring.dojooo.auth.mail.service.EmailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/send-mail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    //이메일 인증을 위한 인증코드 발송 코드
    @PostMapping("/email")
    public ResponseEntity sendJoinMail(@RequestBody EmailPost emailPost) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailPost.getEmail())
                .subject("[dojooo] 이메일 인증을 위한 인증 코드 발송")
                .build();
        String code = emailService.sendMail(emailMessage, "email");
        redisUtil.saveEmailCode(emailPost.getEmail(), code);

        EmailResponse emailResponse = EmailResponse.builder()
                .code(code)
                .message("인증코드 전송 완료")
                .build();
        return ResponseEntity.ok(emailResponse);
    }
    //인증 코드 검증
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerifyRequest request) {
        String emailCode = redisUtil.getEmailCode(request.getEmail());
        if (emailCode == null) {
            return ResponseEntity.badRequest().body("인증번호가 만료되었거나 존재하지 않습니다.");
        }

        if (!emailCode.equals(request.getCode())) {
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
        }
        //인증 성공 -> 인증 완료 표시 저장
        redisUtil.saveVerifiedEmail(request.getEmail());

        // 인증 완료되면 제거
        redisUtil.deleteEmailCode(request.getEmail());

        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }
}
