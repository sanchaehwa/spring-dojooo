package org.spring.dojooo.auth.Redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.main.users.dto.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        log.info("RedisUtil 초기화됨: RedisTemplate = {}", redisTemplate);
    }

    //이메일 인증 코드
    public void saveEmailCode(String email, String code) {
        log.info("Redis 저장 시도 - email: {}, code: {}", email,code);
        redisTemplate.opsForValue().set("emailCode:" + email,code,Duration.ofMinutes(5)); //5분 유효 (이메일을 5분안에 입력해야함.)
    }
    //유효 검증
    public void saveVerifiedEmail(String email) {
        redisTemplate.opsForValue().set("verified:" + email, "true", Duration.ofMinutes(10));  // 10분 유효 (10분안에 회원가입을 완료해야함)
    }

    public boolean isVerifiedEmail(String email) {
        String result = redisTemplate.opsForValue().get("verified:" + email);
        return "true".equals(result);
    }

    public String getEmailCode(String email) {
        return redisTemplate.opsForValue().get("emailCode:" + email);
    }

    public void deleteEmailCode(String email) {
        redisTemplate.delete("emailCode:"+ email);
    }

    public void deleteVerifiedEmail(String email) {
        redisTemplate.delete("verified:" + email);
    }

    public void saveRefreshToken(String email, String refreshToken) {
        log.info("Redis 저장 시도 - user: {}. refreshToken: {}", email, refreshToken);
        redisTemplate.opsForValue().set("refresh:" + email,refreshToken,Duration.ofMinutes(30)); //30분 RefreshToken 유효하게 설정
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get("refresh:" + email);
    }

    public void deleteRefreshToken(String email) {
        Boolean deleted = redisTemplate.delete("refresh:" + email);
        log.info("Redis 삭제 - user: {}. refreshToken: {}", email,deleted);
    }


}
