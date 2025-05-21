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

    public void saveEmailCode(String email, String code) {
        log.info("Redis 저장 시도 - email: {}, code: {}", email,code);
    }
    //유효 검증
    public void saveVerifiedEmail(String email) {
        redisTemplate.opsForValue().set("verified:" + email, "true", Duration.ofMinutes(10));  // 10분 유효
    }

    public boolean isVerifiedEmail(String email) {
        String result = redisTemplate.opsForValue().get("verified:" + email);
        return "true".equals(result);
    }


    public String getEmailCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void deleteEmailCode(String email) {
        redisTemplate.delete(email);
    }
    public void saveRefreshToken(String username, String refreshToken) {
        log.info("Redis 저장 시도 - user: {}. refreshToken: {}", username, refreshToken);
        redisTemplate.opsForValue().set("refresh:" + username,refreshToken);
    }
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("refresh:" + username);
    }
    public void deleteRefreshToken(String username) {
        redisTemplate.delete("refresh:" + username);
    }


}
