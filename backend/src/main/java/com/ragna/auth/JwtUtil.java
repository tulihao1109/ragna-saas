package com.ragna.auth;

import com.ragna.config.JwtProperties;
import com.ragna.tenant.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private final SecretKey key;

    private final long expireMillis;

    public JwtUtil(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
        this.expireMillis = props.expireHours() * 3600L * 1000L;
    }

    public String generate(LoginUser user) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(user.userId()))
                .claim("tenantId",user.tenantId())
                .claim("username",user.username())
                .issuedAt(now)
                .expiration(new Date(now.getTime() +  expireMillis))
                .signWith(key)
                .compact();
    }

    public LoginUser parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long userId = Long.parseLong(claims.getSubject());
        Long tenantId = Long.parseLong(claims.get("tenantId").toString());
        String username = claims.get("username").toString();
        return new LoginUser(tenantId,userId,username,"JWT");
    }
}
