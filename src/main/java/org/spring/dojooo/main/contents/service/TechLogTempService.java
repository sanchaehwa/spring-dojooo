package org.spring.dojooo.main.contents.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.Redis.RedisUtil;
import org.spring.dojooo.main.contents.dto.TechLogTempRedisDTO;
import org.spring.dojooo.main.contents.dto.TechLogTempRequest;
import org.spring.dojooo.main.contents.dto.TechLogTempResponse;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.exception.NotUserEqualsCurrentUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TechLogTempService {

    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public String saveTempLog(Long userId, TechLogTempRequest request, Authentication authentication) {
        String tempUuid = UUID.randomUUID().toString();

        TechLogTempRedisDTO dto = TechLogTempRedisDTO.builder()
                .tempId(tempUuid)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .savedAt(LocalDateTime.now())
                .build();

        redisUtil.saveTempLog(userId, tempUuid, dto);
        return tempUuid;
    }

    public TechLogTempResponse loadTempLog(Long userId, String tempId, Authentication authentication) {
        validateUser(userId, authentication);
        return redisUtil.loadTempLog(userId, tempId);
    }

    public void deleteTempLog(Long userId, String tempId, Authentication authentication) {
        validateUser(userId, authentication);
        redisUtil.deleteTempLog(userId, tempId);
    }

    private void validateUser(Long userId, Authentication authentication) {
        Long currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        if (!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        userRepository.findByIdAndIsDeletedFalse(currentUserId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
    }
}
