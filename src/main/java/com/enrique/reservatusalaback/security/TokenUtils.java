package com.enrique.reservatusalaback.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    private static String ACCESS_TOKEN_SECRET;
    private static Long ACCESS_TOKEN_VALIDITY_SECONDS;

    public TokenUtils( @Value("${jwt.secret}") String secret, @Value("${jwt.expiration-seconds}") Long seconds){
        ACCESS_TOKEN_SECRET = secret;
        ACCESS_TOKEN_VALIDITY_SECONDS = seconds;
    }

    public static String createToken(double id, String name, String email, String role) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String,Object> extra = new HashMap<>();
        extra.put("name", name);
        extra.put("id", id);
        extra.put("role", role);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            if (claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "Token is expired");
            }

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (Exception e) {
            return null;
        }
    }
}
