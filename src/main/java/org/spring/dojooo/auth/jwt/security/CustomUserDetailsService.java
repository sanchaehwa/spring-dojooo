package org.spring.dojooo.auth.jwt.security;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Spring Security가 인증 시 호출하는 메서드
     * 이메일을 기준으로 사용자 정보를 조회
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        return new CustomUserDetails(user);
    }

}
