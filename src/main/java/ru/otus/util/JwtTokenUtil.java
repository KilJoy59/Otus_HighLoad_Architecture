package ru.otus.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.util.dto.UserSecurityDto;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

    private static final String JWT_HEADER_TYPE_TOKEN = "typ";
    private static final String JWT_HEADER_ALG_TOKEN = "alg";
    private static final String JWT_CLAIM_USER_ID = "userId";
    private static final String JWT_CLAIM_TOKEN_EXPIRED = "exp";

    private static final String JWT = "JWT";
    private static final String ALG = "HS256";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long expired;

    Map<String, Date> blackListToken = new HashMap<>();

    public String generateToken(Long userId) {
        return doGenerateToken(userId);
    }

    private String doGenerateToken(Long userId) {

        Map<String, Object> headers = new HashMap<>();
        headers.put(JWT_HEADER_TYPE_TOKEN, Base64.getUrlEncoder().withoutPadding().encodeToString(JWT.getBytes()));
        headers.put(JWT_HEADER_ALG_TOKEN, Base64.getUrlEncoder().withoutPadding().encodeToString(ALG.getBytes()));

        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_CLAIM_USER_ID, Base64.getUrlEncoder().withoutPadding().encodeToString(userId.toString().getBytes()));
        claims.put(JWT_CLAIM_TOKEN_EXPIRED, Base64.getUrlEncoder().withoutPadding().encodeToString(expired.toString().getBytes()));

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expired * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getIdFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        byte[] decodedBytes = Base64.getUrlDecoder().decode(claims.get(JWT_CLAIM_USER_ID, String.class));
        return new String(decodedBytes);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token, UserSecurityDto user) {
        final String id = getIdFromToken(token);
        return (
                id.equals(user.getUserId().toString())
                        && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

}
