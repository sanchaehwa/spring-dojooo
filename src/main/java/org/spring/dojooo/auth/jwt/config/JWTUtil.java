package org.spring.dojooo.auth.jwt.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecretKeyBuilder;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.jwt.exception.InvalidTokenException;
import org.spring.dojooo.global.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Slf4j
@Component
public class JWTUtil {
    private SecretKey secretKey;

    //비밀키 사용해서 비밀키 객체를 생성
    //비밀키: 터미널에서 생성 ( openssl rand -hex 64)
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ((SecretKey)((SecretKeyBuilder) Jwts.SIG.HS256.key()).build()).getAlgorithm());
    }
    //토큰에서 Email 추출
    public String getUsermail(String token) {
        try {
            return (String)((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).get("email", String.class);
        } catch (JwtException var3) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }
    public String getRole(String token) {
        try {
            return (String)((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).get("role", String.class);
        } catch (JwtException var3) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getCategory(String token) {
        return (String)((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).get("category", String.class);
    }

    //Token 만료 여부
    public boolean isExpired(String token) {
        try {
            Date expiration = ((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException var3) {
            throw new InvalidTokenException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException var4) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

   //JWT 생성
   public String createJwt(String category, String email, String role, Long expiredMs) {
       return Jwts.builder().subject(email).claim("category",category).claim("email", email).claim("role", role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(this.secretKey).compact();
   }
}
