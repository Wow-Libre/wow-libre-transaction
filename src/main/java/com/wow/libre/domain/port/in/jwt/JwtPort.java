package com.wow.libre.domain.port.in.jwt;


import org.springframework.security.core.*;

import java.util.*;

public interface JwtPort {

    String extractUsername(String token);

    Long extractUserId(String token);

    boolean isTokenValid(String token);

    Date extractExpiration(String token);

    Collection<GrantedAuthority> extractRoles(String token);

}
