package com.example.problemsolver.datasource.service.implementation;

import com.example.problemsolver.datasource.entity.EntityAppUserDetails;
import com.example.problemsolver.exception.AppResourceNotFoundException;
import com.example.problemsolver.faker.DataFakerGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@DirtiesContext
class EntityAppUserDetailsServiceImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private EntityAppUserDetailsServiceImpl testObject;

    private final DataFakerGenerator fakerGenerator = DataFakerGenerator.getInstance();

    private EntityAppUserDetails persistedObj;

    @BeforeEach
    void setUp() {
        persistedObj = em.persist(
                fakerGenerator.generateEntityAppUserDetails()
        );
    }

    @Test
    @DisplayName("Given new EntityAppUserDetails will persist and be returned")
    void create() {
        var newObject = new EntityAppUserDetails(
                null,
                "test@test.com",
                "Test Testsson",
                "Sweden",
                ZoneId.of("Europe/Berlin")
        );

        var result = testObject.create(newObject);
        assertNotNull(result);
        assertNotNull(result.getId(), "EntityAppUserDetails didn't get a uuid");
        assertEquals(newObject.getEmail(), result.getEmail());
        assertEquals(newObject.getCountry(), result.getCountry());
        assertEquals(newObject.getDisplayName(), result.getDisplayName());
        assertEquals(newObject.getLocalZoneId(), result.getLocalZoneId());

    }

    @Test
    @DisplayName("When given valid id, method will find EntityAppUserDetails")
    void findById() {
        String id = persistedObj.getId();
        assertNotNull(testObject.findById(id));
    }

    @Test
    @DisplayName("When given invalid id, method will not find object and throw AppResourceNotFoundException")
    void findById_throws_AppResourceNotFoundException() {
        assertThrows(
                AppResourceNotFoundException.class,
                () -> testObject.findById("123")
        );
    }

    @Test
    @DisplayName("When given updated object, method will save updates to database")
    void update() {
        var updatedObj = new EntityAppUserDetails();
        updatedObj.setId(persistedObj.getId());
        updatedObj.setDisplayName(persistedObj.getDisplayName());
        updatedObj.setEmail(persistedObj.getEmail());
        updatedObj.setLocalZoneId(persistedObj.getLocalZoneId());
        updatedObj.setCountry("Denmark");

        var result = testObject.update(updatedObj);
        assertEquals(persistedObj.getId(), result.getId());
        assertEquals(persistedObj.getCountry(), "Denmark");
    }

    @Test
    @DisplayName("When given object with conflicting email field then method will throw IllegalArgumentException")
    void update_throws_IllegalArgumentException() {
        var conflictingObj = em.persist(
                new EntityAppUserDetails(null, "foo@gmail.com", null, null, null)
        );

        var updatedObj = new EntityAppUserDetails(
                persistedObj.getId(),
                conflictingObj.getEmail(),
                persistedObj.getDisplayName(),
                persistedObj.getCountry(),
                persistedObj.getLocalZoneId()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> testObject.update(updatedObj)
        );
    }

    @Test
    @DisplayName("GIVEN persisted object WHEN remove is called THEN persisted object will be deleted and returns 1")
    void remove() {
        Integer expected = 1;

        Integer actual = testObject.remove(persistedObj);

        assertEquals(expected, actual);
        assertNull(em.find(EntityAppUserDetails.class, persistedObj.getId()));
    }
}