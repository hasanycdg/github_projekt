package at.qe.skeleton.internal.services;

import at.qe.skeleton.configs.WebSecurityConfig;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servie for Password Validation
 */
@Service
public class PasswordValidationService {
    private final WebSecurityConfig webSecurityConfig;

    @Autowired
    public PasswordValidationService(WebSecurityConfig webSecurityConfig) {
        this.webSecurityConfig = webSecurityConfig;
    }

    public void validatePasswords(String confirmPassword, String password, String errorMessageClientId) {
        if (!webSecurityConfig.passwordEncoder().matches(confirmPassword, password)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(errorMessageClientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords do not match"));
        }
    }
}
