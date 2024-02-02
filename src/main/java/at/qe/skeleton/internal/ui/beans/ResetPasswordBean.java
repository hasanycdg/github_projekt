package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.TokenService;
import at.qe.skeleton.internal.services.UserxService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Map;

/**
 * Managed bean for handling password reset functionality.
 */
@Named
@ViewScoped
public class ResetPasswordBean implements Serializable {

    private String token;
    private String newPassword;

    private boolean validToken = false;

    @Autowired
    UserxService userxService;

    @Autowired
    TokenService tokenservice;

    @Autowired
    SessionInfoBean sessionInfoBean;

    /**
     * Initializes the bean by extracting the token from the URL and checking its validity.
     */
    @PostConstruct
    public void init() {
        // Extract token from the URL
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        token = requestParameterMap.get("token");

        Userx userAssociatedWithToken = tokenservice.getUserByConfirmationToken(token);

        // Check if token is valid and the user associated with the token is the current user
        if(userAssociatedWithToken == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Invalid token"));
            setValidToken(false);
        } else {
            setValidToken(true);
        }
    }

    /**
     * Resets the password for the user associated with the token.
     */
    public void resetPassword() {
        Userx user = tokenservice.getUserByConfirmationToken(token);
        user.setPassword(newPassword);
        userxService.saveUser(user);

        //delete token after password change
        tokenservice.deleteToken(tokenservice.findByTokenString(token));
    }

    public boolean isValidToken() {
        return validToken;
    }

    public void setValidToken(boolean validToken) {
        this.validToken = validToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
