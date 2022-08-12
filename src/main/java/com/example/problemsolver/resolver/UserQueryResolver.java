package com.example.problemsolver.resolver;

import com.example.problemsolver.model.response.User;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

@Component
public class UserQueryResolver implements GraphQLQueryResolver {

    @QueryMapping
    public User me(String username){
        return new User();
    }

}
