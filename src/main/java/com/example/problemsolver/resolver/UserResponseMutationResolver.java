package com.example.problemsolver.resolver;

import com.example.problemsolver.datasource.service.interfaces.EntityAppUserService;
import com.example.problemsolver.model.input.UserCreateInput;
import com.example.problemsolver.model.response.UserResponse;
import com.example.problemsolver.security.JWTUtil;
import com.example.problemsolver.util.EntityConversionService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class UserResponseMutationResolver implements GraphQLMutationResolver {
    private final EntityAppUserService userService;
    private final AuthenticationProvider authenticationProvider;
    private final JWTUtil jwtUtil;
    private final EntityConversionService conversionService;

    @SchemaMapping(typeName = "Mutation", value = "userCreate")
    @PreAuthorize("isAnonymous() || hasRole('APP_ADMIN')")
    public UserResponse userCreate(@Argument(name = "user") UserCreateInput user) {

        var entity = conversionService.fromUserCreateInput(user);
        var result = conversionService.fromEntityAppUser(
                userService.create(entity)
        );
        return new UserResponse(
                result, ""
        );
    }

}
