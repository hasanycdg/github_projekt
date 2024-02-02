package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.services.email.EmailService;
import at.qe.skeleton.internal.services.email.ConfirmationMailStrategy;
import at.qe.skeleton.internal.services.PasswordValidationService;
import at.qe.skeleton.internal.services.TokenService;
import at.qe.skeleton.internal.ui.controllers.UserDetailController;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class UserDetailControllerTest {

    @Autowired
    public UserxService userxService;

    @Mock
    private UserxService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordValidationService passwordValidationService;

    @InjectMocks
    private UserDetailController userDetailController;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    @DirtiesContext
    void testDoRegisterUser() {
        Userx user = new Userx();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUsername("TestUserRegister");
        user.setRoles(Collections.singleton(UserxRole.USER));
        userxService.saveUser(user);

        String token = "generatedToken";
        when(tokenService.generateTokenString()).thenReturn(token);

        userDetailController.setNewUser(user);
        String result = userDetailController.doRegisterUser();
        assertEquals("/login.xhtml?faces-redirect=true", result);

        verify(tokenService, times(1)).createVerificationToken(user, token);
        verify(emailService, times(1)).setEmailStrategy(any(ConfirmationMailStrategy.class));
        verify(emailService, times(1)).sendMail(user.getEmail(), token);
        verify(userService, times(1)).saveUser(user);
        Assertions.assertEquals(user, userDetailController.getNewUser());
    }

    @Test
    @DirtiesContext
    void testDoReloadUser() {
        Userx user = new Userx();
        user.setUsername("testUser");

        when(userService.loadUser(user.getUsername())).thenReturn(user);

        userDetailController.setUser(user);
        userDetailController.doReloadUser();

        assertEquals(user, userDetailController.getUser());
    }

    @Test
    @DirtiesContext
    void testDoDeleteUser() {
        Userx user = new Userx();
        user.setUsername("testUserDelete");
        userxService.saveUser(user);

        userDetailController.setUser(user);
        userDetailController.doDeleteUser();

        Assertions.assertNull(userDetailController.getUser());
    }

    @Test
    @DirtiesContext
    void testTogglePremium() {
        Userx user = new Userx();
        user.setUsername("testUserPrem");
        user.setPremium(false);
        userxService.saveUser(user);

        userDetailController.setUser(user);
        userDetailController.togglePremium();

        Assertions.assertFalse(user.isPremium());
    }

    @Test
    @DirtiesContext
    void testCheckPasswords() {
        Userx user = new Userx();
        user.setUsername("testUserCheckPrem");
        user.setPassword("password");
        user.setPremium(false);
        userxService.saveUser(user);

        userDetailController.setNewUser(user);
        userDetailController.setConfirmPassword("password");

        userDetailController.checkPasswords();

        Assertions.assertNull(userDetailController.getConfirmPassword());
    }

}
