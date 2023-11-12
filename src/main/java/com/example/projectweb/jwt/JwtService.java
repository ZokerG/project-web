package com.example.projectweb.jwt;


import com.example.projectweb.model.Client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${SECRET_KEY}")
    String secretKey;

    public String getToken(Client user) {
        return getToken(new HashMap<>(), user.getUsername());
    }

    private String getToken(Map<String, Object> extraClaims, String document) {

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(document)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    private byte[] getKey() {
        return Decoders.BASE64.decode(secretKey);
    }

    public String getDocumentFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, Client user) {
        final String document = getDocumentFromToken(token);
        return (document.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
