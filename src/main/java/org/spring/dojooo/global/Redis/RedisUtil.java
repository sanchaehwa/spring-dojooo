package org.spring.dojooo.global.Redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.main.contents.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

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
    // ======================== 임시 저장 관련 =========================
    public void saveTempLog(Long userId, String tempId, TechLogTempRedisDTO techLogTempRedisDTO) {
        String redisKey = buildTempLogKey(userId, tempId);
        try {
            techLogTempRedisDTO.updateSavedAt(LocalDateTime.now()); // 저장 시점 자동 설정
            String value = objectMapper.writeValueAsString(techLogTempRedisDTO);
            redisTemplate.opsForValue().set(redisKey, value, Duration.ofHours(24));
            log.info("임시 글 저장 - key: {}", redisKey);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("임시 저장 실패", e);
        }
    }

    // 자동 저장(업데이트 + TTL 연장 포함)
    public void updateTempLog(Long userId, String tempId, TechLogTempRedisDTO techLogTempRedisDTO) {
        String redisKey = buildTempLogKey(userId, tempId);
        try {
           techLogTempRedisDTO.updateSavedAt(LocalDateTime.now()); // 수정 시점 자동 갱신
            String value = objectMapper.writeValueAsString(techLogTempRedisDTO);
            redisTemplate.opsForValue().set(redisKey, value, Duration.ofHours(24));
            log.info("임시 글 자동 저장 - key: {}", redisKey);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("임시 저장 갱신 실패", e);
        }
    }

    public TechLogTempResponse loadTempLog(Long userId, String tempId) {
        String redisKey = buildTempLogKey(userId, tempId);
        String value = redisTemplate.opsForValue().get(redisKey);
        if (value == null) return null;

        try {
            TechLogTempRedisDTO request = objectMapper.readValue(value, TechLogTempRedisDTO.class);
            return TechLogTempResponse.builder()
                    .techLogId(request.getTechLogId())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .contentImageUrl(request.getContentImageUrl())
                    .savedAt(request.getSavedAt())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("임시 저장 불러오기 실패", e);
        }
    }

    public void deleteTempLog(Long userId, String tempId) {
        String redisKey = buildTempLogKey(userId, tempId);
        Boolean deleted = redisTemplate.delete(redisKey);
        log.info("임시 글 삭제 - key: {}, 삭제 결과: {}", redisKey, deleted);
    }
    private static final String TEMP_LOG_KEY_PREFIX = "tempLog";

    private String buildTempLogKey(Long userId, String tempId) {
        return String.format("%s:%d:%s", TEMP_LOG_KEY_PREFIX, userId, tempId);
    }
}



