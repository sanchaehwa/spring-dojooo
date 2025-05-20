package org.spring.dojooo.main.users.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.Redis.RedisUtil;
import org.spring.dojooo.auth.jwt.config.JWTUtil;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.users.domain.User;

import org.spring.dojooo.main.users.dto.UserSignUpRequest;
import org.spring.dojooo.main.users.dto.UserUpdateRequest;
import org.spring.dojooo.main.users.exception.DuplicateUserException;
import org.spring.dojooo.main.users.exception.ModificationTimeExceededException;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    @Transactional
    public Long saveUser(UserSignUpRequest userSignUpRequest) {
        //이미 존재하는 회원인지 아닌지 확인
        validateDuplicateUser(userSignUpRequest);

        User user = userSignUpRequest.toEntity();
        //비밀번호 암호화
        user.encodePassword(passwordEncoder);

        return userRepository.save(user).getId();
    }

    private void validateDuplicateUser(UserSignUpRequest userSignUpRequest) {
        if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
            throw new DuplicateUserException(ErrorCode.DUPLICATE_USER);
        }
    }

    //회원 조회(Id)
    @Transactional(readOnly = true) //조회만 할거라
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
    }

    //회원 정보 수정
    @Transactional
    public Long updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User user = findActiveUser(id);
        user.updateUser(userUpdateRequest);
        user.encodePassword(passwordEncoder);
        return user.getId();
    }
    //임시저장
    @Transactional
    public Map<String, Object> tempStoreUserInfo(Long id, UserUpdateRequest request) {
        redisUtil.tempUserInformation(id, request);

        Long ttl = redisUtil.getRemainingTTL(id);
        if (ttl == null || ttl <= 0) {
            throw new ModificationTimeExceededException(ErrorCode.UPDATE_TIMEOUT);
        }
        return Map.of(
                "id", id,
                "expiredTime", ttl
        );
    }
    //임시저장 -> 최종저장
    @Transactional
    public void finalizeUserUpdate(Long id){
        String key = "tempUserInformation:" + id;
        String json = redisTemplate.opsForValue().get(key); // 키 값으로 사용자의 수정 정보를 가지고 옴.
        if (json == null) {
            throw new RuntimeException("임시 저장된 정보가 없습니다. 수정 가능 시간이 만료되었을수도 있습니다.");
        }
        try {
            UserUpdateRequest userUpdateRequest = objectMapper.readValue(json, UserUpdateRequest.class);
            User user = findActiveUser(id);
            user.updateUser(userUpdateRequest);
            redisTemplate.delete(key);

        }catch (JsonProcessingException e){
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
    //DB에 존재하는 회원인지 아닌지 확인(회원 조회)
    @Transactional(readOnly = true)
    public User findActiveUser(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
    }







}
/*
로그인 -> 로그인 필터를 거쳐 -> 로그인 성공 / 실패 * 성공하면 AccessToken + RefreshToken 클라이언트 저장 - RefreshToken은 Redis에도 저장
로그인 완료 -> 로그인 해야지만 볼수 있는 서비스 (글쓰기, 글목록, 달력) 이런거 -> AccessToken 전달 * 사용자 유효한지 확인
AccessToken 만료 -> /login/refresh 요청
RefreshToken도 만료되면 -> /login 이동
로그아웃 -> RefreshToken 삭제
 */
