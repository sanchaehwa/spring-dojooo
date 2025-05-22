package org.spring.dojooo.auth.mail.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.Redis.RedisUtil;
import org.spring.dojooo.auth.mail.dto.EmailMessage;
import org.spring.dojooo.auth.mail.dto.EmailPost;
import org.spring.dojooo.auth.mail.dto.EmailResponse;
import org.spring.dojooo.auth.mail.dto.EmailVerifyRequest;
import org.spring.dojooo.auth.mail.service.EmailService;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.ApiException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/send-mail")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;
    private final RedisUtil redisUtil;

    //이메일 인증을 위한 인증코드 발송 코드
    @Operation(summary = "이메일 인증 코드 전송", description = "5분간 유효한 이메일 인증 코드를 전송합니다.")
    @PostMapping("/email")
    public ResponseEntity sendJoinMail(@RequestBody EmailPost emailPost) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailPost.getEmail())
                .subject("[dojooo] 이메일 인증을 위한 인증 코드 발송")
                .build();
        String code = emailService.sendMail(emailMessage, "email");
        redisUtil.saveEmailCode(emailPost.getEmail(), code); //5분간 유효한

        EmailResponse emailResponse = EmailResponse.builder()
                .code(code)
                .message("인증코드 전송 완료")
                .build();
        return ResponseEntity.ok(emailResponse);
    }

    //인증 코드 검증
    @Operation(summary = "이메일 인증 코드 검증", description = "전송된 인증 코드와 입력값을 비교하여 인증합니다. 인증에 성공하면 10분 이내에 회원가입을 완료해야 합니다.")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerifyRequest request) {
        String emailCode = redisUtil.getEmailCode(request.getEmail());

        if (emailCode == null) {
            log.warn("인증 코드 없음 또는 만료됨 - email: {}", request.getEmail());
            throw new ApiException(ErrorCode.EMAIL_CODE_EXPIRED); //이메일 인증이 만료된경우 *5분
        }

        if (!emailCode.equals(request.getCode().trim())) {
            log.warn("인증 코드 불일치 - email: {}, 입력된 코드: {}", request.getEmail(), request.getCode());
            throw new ApiException(ErrorCode.EMAIL_CODE_NOT_MATCH); //이메일 인증 코드가 일치하지않는 경우
        }
        //인증 성공 -> 인증 완료 표시 저장
        try {
            // 인증 성공 -> 인증 완료 표시 저장
            redisUtil.saveVerifiedEmail(request.getEmail());
            // 인증 완료되면 코드 제거
            redisUtil.deleteEmailCode(request.getEmail());
        } catch (Exception e) {
            log.error("Redis 처리 중 오류 - email: {}", request.getEmail(), e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        log.info("이메일 인증 성공 - email: {}", request.getEmail());
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }
}
