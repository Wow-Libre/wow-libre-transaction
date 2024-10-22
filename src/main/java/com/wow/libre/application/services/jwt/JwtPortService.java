package com.wow.libre.application.services.jwt;


import com.wow.libre.domain.port.in.jwt.*;
import com.wow.libre.infrastructure.conf.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.*;
import io.jsonwebtoken.security.*;
import lombok.extern.slf4j.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static com.wow.libre.domain.constant.Constants.CONSTANT_ROL_JWT_PROP;
import static com.wow.libre.domain.constant.Constants.HEADER_USER_ID;

@Component
@Slf4j
public class JwtPortService implements JwtPort {

    private final JwtProperties jwtProperties;

    public JwtPortService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get(HEADER_USER_ID, Long.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    @Override
    public boolean isTokenValid(String token) {
        return isSignatureValid(token) && !isTokenExpired(token);
    }


    private boolean isSignatureValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Error al validar la firma del token: {}", e.getMessage());
            return false; // La firma no es válida
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Collection<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        Collection<Map<String, String>> rolesAsMap = Optional.ofNullable(claims.get(CONSTANT_ROL_JWT_PROP))
                .filter(Collection.class::isInstance)
                .map(Collection.class::cast)
                .orElseGet(Collections::emptyList);

        return rolesAsMap.stream()
                .map(roleMap -> roleMap.get("authority"))
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
