package org.spring.dojooo.auth.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.spring.dojooo.auth.mail.dto.EmailMessage;
import org.spring.dojooo.main.users.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final SpringTemplateEngine templateEngine;

    public String sendMail(EmailMessage emailMessage, String type) {
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            if (type.equals("email")) { // 이메일 인증
                mimeMessage.setRecipients(MimeMessage.RecipientType.TO, emailMessage.getTo());
                mimeMessage.setSubject(emailMessage.getSubject());
                mimeMessage.setText("인증번호: " + authNum, "utf-8");
                javaMailSender.send(mimeMessage);

                // 인증번호 Redis에 저장 (5분 유효)
                redisTemplate.opsForValue().set(emailMessage.getTo(), authNum, Duration.ofMinutes(5));
            }
        } catch (MessagingException e) {
            log.error("메일 전송 실패", e);
            throw new RuntimeException("메일 전송 실패");
        }
        return authNum;
    }


    //인증번호 생성
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i=0; i < 8; i++){
            int index = random.nextInt(4);

            switch(index){
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));            }
        }

        return key.toString();
    }
    //thymeleaf를 통한 html적용
    public String setContext(String code, String type){
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }





}
