package com.example.problemsolver.datasource.service.implementation;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.entity.EntityAppUserDetails;
import com.example.problemsolver.datasource.entity.UserRole;
import com.example.problemsolver.datasource.repository.EntityAppRoleRepository;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@DirtiesContext
class EntityAppUserServiceImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private EntityAppUserServiceImpl testObject;

    @Autowired
    private EntityAppRoleRepository appRoleRepository;

    private final DataFakerGenerator fakerGenerator = DataFakerGenerator.getInstance();

    private EntityAppUser testUser;

    public EntityAppUser setupUser(UserRole userRole){
        var appUserDetails = em.persist(
                fakerGenerator.generateEntityAppUserDetails()
        );
        var appUser = fakerGenerator.generateEntityAppUser();
        appUser.setAppUserDetails(appUserDetails);
        var role = appRoleRepository.findByUserRole(userRole).orElseThrow();
        appUser.setAppRoles(new HashSet<>(List.of(role)));
        return em.persist(appUser);
    }

    @BeforeEach
    void setUp() {
        Arrays.stream(UserRole.values()).forEach(role -> em.persist(new EntityAppRole(null, role, null)));
        testUser = setupUser(UserRole.APP_USER);
    }

    @Test
    @DisplayName("GIVEN new AppUserEntity WHEN create method is invoked THEN persist and return")
    void create() {
        String username = "nisse";
        String password = "nizze123";
        String email = "nizzeboi@gmail.com";
        String displayName = "Nisse Andersson";
        String country = "Sweden";
        ZoneId zoneId = ZoneId.of("Europe/Berlin");

        var newAppUser = new EntityAppUser();
        newAppUser.setUsername(username);
        newAppUser.setActive(true);
        newAppUser.setPassword(password);


        newAppUser.setAppUserDetails(new EntityAppUserDetails(
                null, email, displayName, country, zoneId
        ));

        var result = testObject.create(newAppUser);

        assertNotNull(newAppUser);
        assertNotNull(result.getId());
        assertEquals(username, result.getUsername());
        assertNotEquals(password, result.getPassword());
        assertNotNull(result.getRegistrationDate());
        assertTrue(result.isActive());
        assertNull(result.getSuspendedUntil());
        assertNotNull(result.getAppUserDetails());
        assertEquals(1, result.getAppRoles().size());

        var userDetails = result.getAppUserDetails();
        assertNotNull(userDetails.getId());
        assertEquals(email, userDetails.getEmail());
        assertEquals(displayName, userDetails.getDisplayName());
        assertEquals(country, userDetails.getCountry());
        assertEquals(zoneId, userDetails.getLocalZoneId());
    }

    @Test
    @DisplayName("GIVEN id WHEN findById method is called THEN return expected object")
    void findById() {
        String id = testUser.getId();

        var result = testObject.findById(id);

        assertEquals(id, result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("GIVEN invalid id WHEN findById method is called THEN expect AppResourceNotFoundException")
    void findById_throws_AppResourceNotFoundException() {
        assertThrows(
                AppResourceNotFoundException.class,
                () -> testObject.findById("invalid")
        );
    }

    @Test
    @DisplayName("GIVEN updated data WHEN update is called THEN expect result with new state")
    void update() {
        String username = "nisseboi666";
        var updatedEntityAppUser = new EntityAppUser(
                testUser.getId(),
                username,
                null,
                null,
                true,
                null,
                testUser.getAppRoles(),
                testUser.getAppUserDetails()
        );

        var result = testObject.update(updatedEntityAppUser);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertNotNull(result.getAppUserDetails());
    }

    @Test
    @DisplayName("GIVEN EntityAppUser WHEN remove is called THEN expect Integer 2")
    void remove() {
        Integer expected = 2;

        Integer actual = testObject.remove(testUser);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("GIVEN new EntityAppUser WHEN createAdmin is called THEN expect UserRole.APP_ADMIN")
    void createAdmin() {
        String username = "nisse";
        String password = "nizze123";
        String email = "nizzeboi@gmail.com";
        String displayName = "Nisse Andersson";
        String country = "Sweden";
        ZoneId zoneId = ZoneId.of("Europe/Berlin");

        var newAppUserAdmin = new EntityAppUser();
        newAppUserAdmin.setUsername(username);
        newAppUserAdmin.setActive(true);
        newAppUserAdmin.setPassword(password);


        newAppUserAdmin.setAppUserDetails(new EntityAppUserDetails(
                null, email, displayName, country, zoneId
        ));

        var result = testObject.createAdmin(newAppUserAdmin);

        assertNotNull(result);
        assertTrue(result.getAppRoles().stream().anyMatch(entityAppRole -> entityAppRole.getUserRole().equals(UserRole.APP_ADMIN)));
    }
}