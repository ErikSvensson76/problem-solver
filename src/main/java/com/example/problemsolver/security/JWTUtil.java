package com.example.problemsolver.security;

import com.example.problemsolver.datasource.entity.EntityAppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final SecurityConstants securityConstants;

    public Claims parseClaims(String jwt){
        SecretKey key = Keys.hmacShaKeyFor(securityConstants.getJwt_key().getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }



    public String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority : authorities){
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    public SecurityUserDetails fromClaims(Claims claims){
        SecurityUserDetails securityUserDetails = new SecurityUserDetails();

        securityUserDetails.setUsername(claims.getSubject());
        securityUserDetails.setId(claims.get(securityConstants.getId(), String.class));
        securityUserDetails.setUserDetailsId(claims.get(securityConstants.getUserDetailsId(), String.class));
        securityUserDetails.setEmail(claims.get(securityConstants.getEmail(), String.class));
        String authorities = claims.get(securityConstants.getAuthorities(), String.class);

        securityUserDetails.setAuthorities(
                Arrays.stream(authorities.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );

        return securityUserDetails;
    }

    public String buildToken(SecurityUserDetails securityUserDetails){
        SecretKey key = Keys.hmacShaKeyFor(securityConstants.getJwt_key().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("problem-solver")
                .setHeaderParam("typ", "JWT")
                .setSubject(securityUserDetails.getUsername())
                .claim(securityConstants.getAuthorities(), populateAuthorities(securityUserDetails.getAuthorities()))
                .claim(securityConstants.getId(), securityUserDetails.getId())
                .claim(securityConstants.getUserDetailsId(), securityUserDetails.getUserDetailsId())
                .claim(securityConstants.getEmail(), securityUserDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(key, SignatureAlgorithm.ES512).compact();
    }

    public String populateAuthorities(EntityAppUser appUser){
        Set<String> authorities = new HashSet<>();
        for(var appRole : appUser.getAppRoles()){
            authorities.add(appRole.getUserRole().name());
        }
        return String.join(",", authorities);
    }

    public String buildToken(EntityAppUser entityAppUser){
        SecretKey key = Keys.hmacShaKeyFor(securityConstants.getJwt_key().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("problem-solver")
                .setHeaderParam("typ", "JWT")
                .setSubject(entityAppUser.getUsername())
                .claim(securityConstants.getAuthorities(), populateAuthorities(entityAppUser))
                .claim(securityConstants.getId(), entityAppUser.getId())
                .claim(securityConstants.getUserDetailsId(), entityAppUser.getAppUserDetails().getId())
                .claim(securityConstants.getEmail(), entityAppUser.getAppUserDetails().getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + 3_600_000
                ))
                .signWith(key, SignatureAlgorithm.ES512).compact();
    }

}
