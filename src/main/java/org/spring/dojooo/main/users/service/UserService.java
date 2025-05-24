package org.spring.dojooo.main.users.service;


import lombok.RequiredArgsConstructor;
import org.spring.dojooo.global.Redis.RedisUtil;
import org.spring.dojooo.auth.jwt.security.CustomUserDetailsService;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.ApiException;
import org.spring.dojooo.main.users.domain.User;

import org.spring.dojooo.main.users.dto.UserSignUpRequest;
import org.spring.dojooo.main.users.dto.UserUpdateRequest;
import org.spring.dojooo.main.users.exception.DuplicateUserException;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public Long saveUser(UserSignUpRequest userSignUpRequest) {
        //이미 존재하는 회원인지 아닌지 확인
        validateDuplicateUser(userSignUpRequest); //이메일 중복 체크

        User user = userSignUpRequest.toEntity();

        String userEmail = user.getEmail();
        if (!redisUtil.isVerifiedEmail(userEmail)) { //10분간 유효 (인증완료된 상태임을 알려주는)
            throw new ApiException(ErrorCode.EMAIL_AUTH_TIMEOUT); //10분이 지나면 다시 인증
        }
        //회원가입 완료하면
        redisUtil.deleteVerifiedEmail(userEmail);
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
        if (userUpdateRequest.getOption().equals("email")) {
             String newEmail = userUpdateRequest.getValue();
             if (user.getEmail().equals(newEmail)) {
                 throw new DuplicateUserException(ErrorCode.DUPLICATE_EMAIL);
             }
             if(userRepository.findByEmail(newEmail).isPresent()) {
                 throw new DuplicateUserException(ErrorCode.DUPLICATE_EMAIL);
            }
             //이메일 인증 여부를 검증
            if (!redisUtil.isVerifiedEmail(newEmail)) {
                throw new ApiException(ErrorCode.EMAIL_BAD_REQUEST);
            }
            redisUtil.deleteVerifiedEmail(newEmail);
        }
        user.updateUser(userUpdateRequest);
        return user.getId();
    }
    //회원 탈퇴
    @Transactional
    public Long deleteUser(Long id) {
        User user = findActiveUser(id); //회원 조회
        //refresh Token 삭제
        redisUtil.deleteRefreshToken( user.getEmail());
        return user.getId();
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
