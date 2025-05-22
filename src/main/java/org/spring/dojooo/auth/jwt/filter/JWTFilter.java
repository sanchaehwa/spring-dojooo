package org.spring.dojooo.auth.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.jwt.config.JWTUtil;
import org.spring.dojooo.auth.jwt.exception.InvalidTokenException;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.auth.jwt.security.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.model.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request Authorization 헤더에 담음
        String AccessToken = request.getHeader(AUTH_HEADER);

        if (AccessToken == null || !AccessToken.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("authorization now");
        //순수토큰 획득(Barer 추출하고 순수 토큰만 사용)
        String accessToken = AccessToken.substring(BEARER.length());

        try{
            //서명 및 유효성 검증
            jwtUtil.validateToken(accessToken);
            //토큰에서 이메일 추출
            String email = jwtUtil.getUsermail(accessToken);
            //회원 조회 -- 존재하지않으면 예외 발생 (UserNameNotFoundException)
            CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
            //인증 객체 생성 및  Security Context 저장
            Authentication authtToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authtToken);

        }catch(UsernameNotFoundException e) {
            log.warn("User not Found for token email:{}",e.getMessage());
            throw new InvalidTokenException(ErrorCode.NOT_FOUND_USER);
        }
        catch (InvalidTokenException e) {
            throw e; //이미 JWT Util에 정의 해둠
        }
        catch (Exception e) {
            log.error("Unexpected error", e);
            throw new InvalidTokenException(ErrorCode.UNSUPPORTED_TOKEN);
        }
        filterChain.doFilter(request, response);
    }
}
