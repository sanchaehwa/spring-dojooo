package org.spring.dojooo.auth.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.dojooo.auth.jwt.config.JWTUtil;
import org.spring.dojooo.auth.jwt.exception.InvalidTokenException;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.auth.jwt.security.CustomUserDetailsService;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[JWTFilter] 요청 URI: {}", request.getRequestURI());
        String authorization = request.getHeader(AUTH_HEADER);

        if (authorization == null || !authorization.startsWith(BEARER)) {
            log.debug("Authorization header is missing or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(BEARER.length());

        if (jwtUtil.isExpired(token)) {
            log.debug("JWT token is expired");
            throw new InvalidTokenException(ErrorCode.TOKEN_EXPIRED);
        }

        try {
            String email = jwtUtil.getUsermail(token);

            User user = User.builder().email(email).build();

            CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            log.error("JWT token parsing failed", e);
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

}
