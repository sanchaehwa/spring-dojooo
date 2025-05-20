package org.spring.dojooo.auth.jwt.config;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey;

    //비밀키 사용해서 비밀키 객체를 생성
    //비밀키: 터미널에서 생성 ( openssl rand -hex 64)
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    //토큰에서 Email 추출
    public String getUsermail(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }
    //토큰에서 (권한) Role 추출
    public String getRole(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
    //Token 만료 여부
    public Boolean isExpired(String token) {
        return Jwts.parser().
                verifyWith(secretKey).
                build()
                .parseSignedClaims(token).
                getPayload()
                .getExpiration() //토큰 만료 날짜를 가지고옴
                .before(new Date());
    }

    //RefreshToken 인지 Access Token
    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

   //JWT 생성
   public String createJwt(String category, String email, String role, Long exp) {

       return Jwts.builder()
               .claim("category", category)
               .claim("email", email)
               .claim("role", role)
               .issuedAt(new Date()) //발급 시간은 현재 시간
               .expiration(new Date(System.currentTimeMillis() + exp)) //만료시간 + 만료기간
               .signWith(secretKey)
               .compact();
   }
}
