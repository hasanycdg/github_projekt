package at.qe.skeleton.tests;

import at.qe.skeleton.configs.WebSecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserxService;

import java.util.Collection;
import java.util.List;


/**
 * Some very basic tests for {@link UserxService}.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    UserxService userService;

    private final int anzUser = 13;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DirtiesContext
    public void testDatainitialization() {
        Assertions.assertEquals(anzUser, userService.getAllUsers().size(), "Insufficient amount of users initialized for test data source");
        List<Userx> allUsers = (List<Userx>) userService.getAllUsers();
        for (int i = 0; i < Math.min(4, allUsers.size()); i++) {
            Userx user = allUsers.get(i);
            if ("admin".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.ADMIN), "User \"" + user + "\" does not have role ADMIN");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else if ("manager".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.MANAGER), "User \"" + user + "\" does not have role MANAGER");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else if ("testManager".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.USER), "User \"" + user + "\" does not have role EMPLOYEE");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else if ("testUser".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.USER), "User \"" + user + "\" does not have role EMPLOYEE");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else {
                Assertions.fail("Unknown user \"" + user.getUsername() + "\" loaded from test data source via UserService.getAllUsers");
            }
        }
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DirtiesContext
    public void testDeleteUser() {
        String username = "user1";
        Userx adminUser = userService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeDeletedUser = userService.loadUser(username);
        Assertions.assertNotNull(toBeDeletedUser, "User \"" + username + "\" could not be loaded from test data source");

        userService.deleteUser(toBeDeletedUser);

        Assertions.assertEquals(anzUser - 1, userService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        Userx deletedUser = userService.loadUser(username);
        Assertions.assertNull(deletedUser, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

        for (Userx remainingUser : userService.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DirtiesContext
    public void testUpdateUser() {
        String username = "user1";
        Userx adminUser = userService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeSavedUser = userService.loadUser(username);
        Assertions.assertNotNull(toBeSavedUser, "User \"" + username + "\" could not be loaded from test data source");

        Assertions.assertNull(toBeSavedUser.getUpdateUser(), "User \"" + username + "\" has a updateUser defined");
        Assertions.assertNull(toBeSavedUser.getUpdateDate(), "User \"" + username + "\" has a updateDate defined");

        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeSavedUser);

        Userx freshlyLoadedUser = userService.loadUser("user1");
        Assertions.assertNotNull(freshlyLoadedUser, "User \"" + username + "\" could not be loaded from test data source after being saved");
        Assertions.assertNotNull(freshlyLoadedUser.getUpdateDate(), "User \"" + username + "\" does not have a updateDate defined after being saved");
        Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    @DirtiesContext
    public void testEditUser() {

        Userx toBeChangedUser = userService.loadUser("admin");
        String toBeChangedUserName = "admin";

        toBeChangedUser.setEmail("changed-email@whatever.wherever");
        toBeChangedUser.setLastName("NewLastname");
        userService.saveUser(toBeChangedUser);


        Userx freshlyLoadedUser = userService.loadUser("admin");
        Assertions.assertNotNull(freshlyLoadedUser, "User \"" + toBeChangedUserName + "\" could not be loaded from test data source after being saved");
        Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(), "User \"" + toBeChangedUserName + "\" does not have a the correct email attribute stored being saved");
        Assertions.assertEquals("NewLastname", freshlyLoadedUser.getLastName(), "User \"" + toBeChangedUserName + "\" does not have a the correct lastname attribute stored being saved");
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DirtiesContext
    public void testCreateUser() {
        Userx adminUser = userService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

        String username = "newuser";
        String password = "passwd";
        String fName = "New";
        String lName = "User";
        String email = "new-email@whatever.wherever";
        String phone = "+12 345 67890";
        Userx toBeCreatedUser = new Userx();
        toBeCreatedUser.setUsername(username);
        toBeCreatedUser.setPassword(password);
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName(fName);
        toBeCreatedUser.setLastName(lName);
        toBeCreatedUser.setEmail(email);
        toBeCreatedUser.setPhone(phone);
        toBeCreatedUser.setRoles(Sets.newSet(UserxRole.USER, UserxRole.MANAGER));
        userService.saveUser(toBeCreatedUser);

        Userx freshlyCreatedUser = userService.loadUser(username);
        Assertions.assertNotNull(freshlyCreatedUser, "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(username, freshlyCreatedUser.getUsername(), "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(fName, freshlyCreatedUser.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
        Assertions.assertEquals(lName, freshlyCreatedUser.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
        Assertions.assertEquals(email, freshlyCreatedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
        Assertions.assertEquals(phone, freshlyCreatedUser.getPhone(), "User \"" + username + "\" does not have a the correct phone attribute stored being saved");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserxRole.MANAGER), "User \"" + username + "\" does not have role MANAGER");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserxRole.USER), "User \"" + username + "\" does not have role EMPLOYEE");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DirtiesContext
    public void testExceptionForEmptyUsername() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Userx adminUser = userService.loadUser("admin");
            Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

            Userx toBeCreatedUser = new Userx();
            userService.saveUser(toBeCreatedUser);
        });
    }

    @Test
    @DirtiesContext
    public void testUnauthenticateddLoadUsers() {
        Assertions.assertThrows(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class, () -> {
            for (Userx user : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DirtiesContext
    public void testUnauthorizedLoadUsers() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            for (Userx user : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testAuthorizedLoadUser() {
        String username = "user1";
        Userx user = userService.loadUser(username);
        Assertions.assertEquals(username, user.getUsername(), "Call to userService.loadUser returned wrong user");
    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    @DirtiesContext
    public void testAuthorizedDeleteUser() {
        // Check that deleting a user by an unauthorized user does not throw an AccessDeniedException
        Assertions.assertDoesNotThrow(() -> {
            Userx user = userService.loadUser("user1");
            Assertions.assertEquals("user1", user.getUsername(), "Call to userService.loadUser returned wrong user");

            // Try to delete the user, should not throw an AccessDeniedException
            userService.deleteUser(user);
        });
    }

    @Test
    @WithMockUser(username = "user2", authorities = {"USER"})
    @DirtiesContext
    public void testUnauthorizedDeleteUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Userx user = userService.loadUser("user1");
            Assertions.assertEquals("user1", user.getUsername(), "Call to userService.loadUser returned wrong user");
            userService.deleteUser(user);
        });
    }
}