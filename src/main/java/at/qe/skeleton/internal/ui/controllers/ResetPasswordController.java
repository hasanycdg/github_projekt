package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.PasswordValidationService;
import at.qe.skeleton.internal.services.TokenService;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.services.email.EmailService;
import at.qe.skeleton.internal.services.email.PasswordChangeMailStrategy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller handling the password reset functionality.
 */
@Controller
public class ResetPasswordController {
    @Autowired
    private UserxService userxService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordValidationService passwordValidationService;

    private String email;

    /**
     * Attribute to catch the confirmPassword input.
     */
    private String confirmPassword;

    /**
     * returns the ConfirmPassword-
     * @return
     */

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Initiates the password change process by sending a reset email to the user.
     */
    public void requestPasswordChange(){
        Userx user = userxService.getUserByEmail(this.email);

        //display error message if user not found or user has no email
        if (user == null || user.getEmail() == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("E-Mail not found"));
            return;
        }

        //create token and send email
        String token = tokenService.generateTokenString();
        tokenService.createVerificationToken(user, token);

        emailService.setEmailStrategy(new PasswordChangeMailStrategy());
        emailService.sendMail(user.getEmail(), token);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Reset email has been sent!"));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void checkPasswords(){
        passwordValidationService.validatePasswords(confirmPassword,userxService.getUserByEmail(this.email).getPassword(), "passwordChangeForm:confirmPassword");
    }
}
