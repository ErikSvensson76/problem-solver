package com.example.problemsolver.security.filters;

import com.example.problemsolver.security.JWTUtil;
import com.example.problemsolver.security.SecurityConstants;
import com.example.problemsolver.security.SecurityUserDetails;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final SecurityConstants securityConstants;

    public JWTAuthorizationFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil,
            SecurityConstants securityConstants
    ) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.securityConstants = securityConstants;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String jwt = request.getHeader(securityConstants.getAuthorization());
        if(jwt != null){
            try{
                if(!jwt.startsWith(securityConstants.getBearer())){
                    throw new BadCredentialsException("Invalid token");
                }
                jwt = jwt.substring(securityConstants.getBearer().length());
                Claims claims = jwtUtil.parseClaims(jwt);
                SecurityUserDetails securityUserDetails = jwtUtil.fromClaims(claims);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        securityUserDetails, null, securityUserDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                throw new BadCredentialsException(e.getMessage(), e);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/login");
    }
}
