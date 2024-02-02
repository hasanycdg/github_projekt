package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.controllers.ConfirmationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ConfirmationControllerTest {

    @Mock
    private UserxService userxService;

    @Mock
    private Userx mockedUser;

    @InjectMocks
    private ConfirmationController confirmationController;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConfirmRegistration_Success() {
        String token = "validToken";

        // Mock the behavior of the userxService
        Mockito.when(userxService.getUserByConfirmationToken(token)).thenReturn(mockedUser);

        // Invoke the controller method
        String result = confirmationController.confirmRegistration(token);

        // Verify the expected behavior
        assertEquals("redirect:/verification/success.xhtml", result);

        // Verify that user.setEnabled(true) and userxService.saveUser(user) were called
        Mockito.verify(mockedUser, Mockito.times(1)).setEnabled(true);
        Mockito.verify(userxService, Mockito.times(1)).saveUser(mockedUser);
    }

    @Test
    public void testConfirmRegistration_Failure() {
        String token = "invalidToken";

        // Mock the behavior of the userxService when the token is invalid
        Mockito.when(userxService.getUserByConfirmationToken(token)).thenReturn(null);

        // Invoke the controller method
        String result = confirmationController.confirmRegistration(token);

        // Verify the expected behavior
        assertEquals("redirect:/verification/failure.xhtml", result);

        // Verify that userxService.saveUser(user) was never called
        Mockito.verify(userxService, Mockito.never()).saveUser(any(Userx.class));
    }
}
