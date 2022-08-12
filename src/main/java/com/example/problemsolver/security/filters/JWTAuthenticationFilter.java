package com.example.problemsolver.security.filters;

import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.repository.EntityAppUserRepository;
import com.example.problemsolver.exception.AppResourceNotFoundException;
import com.example.problemsolver.model.input.UserLoginInput;
import com.example.problemsolver.model.response.User;
import com.example.problemsolver.model.response.UserAuthToken;
import com.example.problemsolver.model.response.UserDetails;
import com.example.problemsolver.model.response.UserResponse;
import com.example.problemsolver.security.JWTUtil;
import com.example.problemsolver.security.SecurityUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final EntityAppUserRepository repository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserLoginInput userLoginInput;
        try{
            userLoginInput = new ObjectMapper().readValue(request.getInputStream(), UserLoginInput.class);
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage(), e);
        }

        if(userLoginInput != null && userLoginInput.getUsername() != null && userLoginInput.getPassword() != null){
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginInput.getUsername(), userLoginInput.getPassword()
                    )
            );
        }else {
            throw new BadCredentialsException("Bad credentials");
        }

    }

    @Override
    @Transactional(readOnly = true)
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException
    {
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) authResult.getPrincipal();

        if(securityUserDetails != null){
            String token = jwtUtil.buildToken(securityUserDetails);
            EntityAppUser entityAppUser = repository.findByUsername(securityUserDetails.getUsername())
                    .orElseThrow(() -> new  AppResourceNotFoundException("Couln't find user with provided username"));
            User user = new User();
            user.setId(entityAppUser.getId());
            user.setUsername(entityAppUser.getUsername());
            OffsetDateTime dateTime = OffsetDateTime.of(entityAppUser.getRegistrationDate(), ZoneOffset.ofHours(2));
            user.setRegistrationDate(dateTime);

            user.setUserDetails(new UserDetails(
                    user.getUserDetails().getId(),
                    user.getUserDetails().getEmail(),
                    user.getUserDetails().getProfile(),
                    user.getUserDetails().getDisplayName()
            ));
            UserResponse userResponse = new UserResponse();
            userResponse.setUser(user);
            userResponse.setAuthToken(
                    new UserAuthToken(token, null)
            );

            ObjectMapper objectMapper = new ObjectMapper();

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userResponse);
            response.getWriter().write(json);
            chain.doFilter(request, response);
        }
    }
}
