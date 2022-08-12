package com.example.problemsolver.faker;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.entity.EntityAppUserDetails;
import net.datafaker.Faker;

public class DataFakerGenerator {

    private static DataFakerGenerator INSTANCE = new DataFakerGenerator();

    private DataFakerGenerator(){
        this.faker = Faker.instance();
    }

    public static DataFakerGenerator getInstance(){
        return INSTANCE;
    }

    private final Faker faker;


    public EntityAppUser generateEntityAppUser(){
        return new EntityAppUser(
                null,
                faker.name().username(),
                faker.internet().password(),
                null,
                true,
                null,
                null,
                null
        );
    }

    public EntityAppUserDetails generateEntityAppUserDetails(){
        return new EntityAppUserDetails(
                null,
                faker.internet().emailAddress(),
                faker.name().fullName(),
                faker.country().name(),
                null
        );

    }


}
