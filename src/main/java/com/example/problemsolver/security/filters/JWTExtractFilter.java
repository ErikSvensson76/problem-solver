package com.example.problemsolver.security.filters;


import com.example.problemsolver.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class JWTExtractFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final Pattern BEARER = Pattern.compile("^Bearer (.+?)$");
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        getToken(request)
                .map(jwtUtil::parseClaims)
                .map(jwtUtil::fromClaims)
                .ifPresent(principal -> SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(principal, null, principal.getAuthorities())
                ));
        filterChain.doFilter(request, response);
    }

    private Optional<String> getToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(Predicate.not(String::isEmpty))
                .map(BEARER::matcher)
                .filter(Matcher::find)
                .map(matcher -> matcher.group(1));
    }

}
