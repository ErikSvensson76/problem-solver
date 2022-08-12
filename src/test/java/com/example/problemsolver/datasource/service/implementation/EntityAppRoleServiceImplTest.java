package com.example.problemsolver.datasource.service.implementation;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.entity.UserRole;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@DirtiesContext
class EntityAppRoleServiceImplTest {

    private final DataFakerGenerator generator = DataFakerGenerator.getInstance();
    @Autowired
    private TestEntityManager em;
    @Autowired
    private EntityAppRoleServiceImpl testObject;

    private EntityAppRole persistedRole;

    @BeforeEach
    void setUp() {
        persistedRole = em.persist(
                new EntityAppRole(null, UserRole.APP_USER, null)
        );

        Set<EntityAppUser> entityAppUsers = Stream.generate(generator::generateEntityAppUser)
                .limit(3)
                .map(em::persist)
                .collect(Collectors.toSet());


        persistedRole.setAppUsers(entityAppUsers);
        persistedRole.getAppUsers()
                .forEach(entityAppUser -> entityAppUser.setAppRoles(new HashSet<>(List.of(persistedRole))));

        persistedRole = em.persistAndFlush(persistedRole);
    }

    @Test
    @DisplayName("Given new a EntityAppRole object will persist new EntityAppRole in database")
    void create() {
        EntityAppRole entityAppRole = new EntityAppRole(null, UserRole.APP_ADMIN, null);

        EntityAppRole result = testObject.create(entityAppRole);

        assertNotNull(result.getId());
        assertEquals(UserRole.APP_ADMIN, result.getUserRole());
        assertNotNull(result.getAppUsers());
    }

    @Test
    @DisplayName("Given id of persisted EntityAppRole object will successfully return the entity")
    void findById() {
        String id = persistedRole.getId();

        EntityAppRole result = testObject.findById(id);

        assertEquals(persistedRole, result);
    }

    @Test
    @DisplayName("Given wrong id throws AppResourceNotFoundException")
    void findByIdNotFound() {
        assertThrows(
                AppResourceNotFoundException.class,
                () -> testObject.findById("1234")
        );
    }

    @Test
    @DisplayName("Given updated EntityAppRole will change updated columns in database")
    void update() {
        EntityAppRole updatedEntityAppRole = new EntityAppRole(
                persistedRole.getId(),
                UserRole.APP_ADMIN,
                persistedRole.getAppUsers()
        );

        EntityAppRole result = testObject.update(updatedEntityAppRole);

        assertNotNull(result);
        assertEquals(persistedRole.getId(), result.getId());
        assertEquals(UserRole.APP_ADMIN, result.getUserRole());
        assertEquals(persistedRole.getAppUsers(), result.getAppUsers());
    }

    @Test
    @DisplayName("Given persisted entity will delete it from database and return 1")
    void remove() {
        EntityAppRole entityAppRole = em.find(EntityAppRole.class, persistedRole.getId());
        Integer expected = 1;

        Integer result = testObject.remove(entityAppRole);

        assertEquals(expected, result);
        assertNull(em.find(EntityAppRole.class, entityAppRole.getId()));
    }

    @Test
    @DisplayName("Given valid arguments will link EntityAppRole to EntityAppUser vice versa then save")
    void addAEntityAppUser() {
        EntityAppUser toAdd = em.persist(generator.generateEntityAppUser());

        EntityAppRole result = testObject.addAEntityAppUser(UserRole.APP_USER, toAdd.getId());

        assertTrue(result.getAppUsers().contains(toAdd), "EntityAppUser was not present in EntityAppRole");
        assertTrue(toAdd.getAppRoles().contains(result), "EntityAppRole was not present in EntityAppUser");
    }

    @Test
    @DisplayName("Given valid arguments will decouple EntityAppRole from EntityAppUser vice versa then save")
    void removeEntityAppUser() {
        EntityAppUser toRemove = persistedRole.getAppUsers().stream().findFirst().orElse(null);
        assertNotNull(toRemove, "EntityAppRole didnt contain any EntityAppUser(s)");
        String id = toRemove.getId();

        EntityAppRole result = testObject.removeEntityAppUser(UserRole.APP_USER, id);

        assertFalse(result.getAppUsers().contains(toRemove),"EntityAppRole was not removed from EntityAppUser");
        assertFalse(toRemove.getAppRoles().contains(persistedRole),"EntityAppRole was not removed from EntityAppUser");
    }
}