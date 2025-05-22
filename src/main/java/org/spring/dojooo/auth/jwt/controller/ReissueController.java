package org.spring.dojooo.auth.jwt.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.Redis.RedisUtil;
import org.spring.dojooo.auth.jwt.config.JWTUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
//AccessToken이 만료되면
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Operation(summary = "AccessToken 재발급",description = "Access Token 만료시")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;

        //쿠키에서 Refresh Token 찾기 -> 사용자가 가지고 있는 Refresh Token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }
        if (refresh == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token is empty");
        }
        //Refresh Token 만료 -> 로그인으로 리다렉트 (REST API )
        try {
            jwtUtil.isExpired(refresh);
        }catch(ExpiredJwtException e){
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);

        }
        //refresh 토큰인지 확인
        String category = jwtUtil.getCategory(refresh);

        //refresh Token 이 아닌 경우
        if(!category.equals("refresh")) {
            return new ResponseEntity<>("refresh token is invalid", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getUsermail(refresh);
        String role = jwtUtil.getRole(refresh);

        String redisRefresh = redisUtil.getRefreshToken(email);
        //Redis(서버)에 저장되어 있는 refresh - 사용자가 가지고 있는 refresh 하고 다른 경우
        if(!refresh.equals(redisRefresh)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token is invalid");
        }
        //Redis == Refresh 재발급
        //재발급 받기위한 삭제 과정
        redisUtil.deleteRefreshToken(email);

        //Access 생성
        String newAccess = jwtUtil.createJwt("access", email, role, 600000L);
        //Refresh 생성 (Refresh Rotate)-Access +  Refresh 재발급
        String newRefresh = jwtUtil.createJwt("refresh", email, role, 1800000L); //30 -> JWT 내부의 만료시간

        redisUtil.saveRefreshToken(email, newRefresh);
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>("success", HttpStatus.OK);

    }
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
