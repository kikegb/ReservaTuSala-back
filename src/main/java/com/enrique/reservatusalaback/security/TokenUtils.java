package com.enrique.reservatusalaback.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "S3CrEtK3Yh$$k3kjgk3j23jkgll.tt32h1gh21gh1gh1hjl1s&";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 365L * 24L * 60L * 60L;

    public static String createToken(String name, String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String,Object> extra = new HashMap<>();
        extra.put("name", name);

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

            if (claims.getExpiration().after(new Date())) {
                throw new ExpiredJwtException(null, claims, "Token is expired");
            }

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (Exception e) {
            return null;
        }
    }
}
