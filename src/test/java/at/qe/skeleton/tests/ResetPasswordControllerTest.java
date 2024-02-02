package at.qe.skeleton.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import at.qe.skeleton.internal.model.Userx;

import at.qe.skeleton.internal.services.TokenService;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.services.email.EmailService;
import at.qe.skeleton.internal.services.email.PasswordChangeMailStrategy;
import at.qe.skeleton.internal.ui.controllers.ResetPasswordController;
import jakarta.faces.annotation.SessionMap;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;


import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest
@WebAppConfiguration
class ResetPasswordControllerTest {

    @Mock
    private UserxService userxService;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ResetPasswordController resetPasswordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock FacesContext for testing
        // Utility class needed as the method setCurrentInstance is protected
        FacesContext facesContext = ContextMocker.mockFacesContext();
    }

    @Test
    void testRequestPasswordChange() {
        // Setup
        String userEmail = "test@example.com";
        Userx user = new Userx();
        user.setEmail(userEmail);

        // mock the needed methods
        when(userxService.getUserByEmail(userEmail)).thenReturn(user);
        when(tokenService.generateTokenString()).thenReturn("mocked-token");
        doNothing().when(tokenService).createVerificationToken(eq(user), anyString());

        // call controller methods
        resetPasswordController.setEmail(userEmail);
        resetPasswordController.requestPasswordChange();

        // verify that the methods were called
        verify(emailService, times(1)).setEmailStrategy(any(PasswordChangeMailStrategy.class));
        verify(emailService, times(1)).sendMail(user.getEmail(), "mocked-token");
        verify(tokenService, times(1)).createVerificationToken(eq(user), anyString());

        // reset mocks (not sure if needed)
        reset(userxService, tokenService, emailService);
    }
}
