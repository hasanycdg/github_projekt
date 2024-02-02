package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.RolChangeLog;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.PasswordValidationService;
import at.qe.skeleton.internal.services.TokenService;
import at.qe.skeleton.internal.services.email.ConfirmationMailStrategy;
import at.qe.skeleton.internal.services.email.EmailService;
import at.qe.skeleton.internal.services.UserxService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import at.qe.skeleton.internal.services.email.PasswordChangeMailStrategy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class UserDetailController implements Serializable {

    @Autowired
    private UserxService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordValidationService passwordValidationService;
    private Set<UserxRole> newRoles;
    private Set<UserxRole> oldRoles;

    /**
     * Attribute to cache the currently displayed user
     */
    private Userx user;
    /**
     * Attribute to cache the newly registered user
     */
    private Userx newUser;

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
     * Returns the newUser which is created from the user class
     * @return
     */
    public Userx getNewUser() {
        if (newUser == null){
            newUser = new Userx();
            newUser.setRoles(Collections.singleton(UserxRole.USER));
        }
        return newUser;
    }

    /**
     * Sets the new user. This user is targeted by any further calls of
     * {@link #doRegisterUser()}.
     *
     * @param newUser
     */
    public void setNewUser(Userx newUser) {
        this.newUser = newUser;
    }

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()}, {@link #doDeleteUser()}.
     *
     * @param user
     */


    public void setUser(Userx user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public Userx getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {
        user = this.userService.saveUser(user);

    }

    public Userx doLoadUser(String username){
        return userService.loadUser(username);
    }

    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        this.userService.deleteUser(user);
        user = null;
    }
    /**
     * Action to register the user by the inout data of the Html file.
     */
    public String doRegisterUser(){
        newUser.setEnabled(false); //set to false until confirmed via email
        user = this.userService.saveUser(newUser);

        //logic for verification process
        String token = tokenService.generateTokenString();
        tokenService.createVerificationToken(newUser, token);

        //send confirmation mail
        emailService.setEmailStrategy(new ConfirmationMailStrategy());
        emailService.sendMail(newUser.getEmail(), token);

        return "/login.xhtml?faces-redirect=true";
    }

    /**
     * Toggles the change of the Premium Status.
     */
    public void togglePremium(){
        if(user != null){
            user.setPremium(!user.isPremium());
            userService.saveUser(user);
        }
    }

    /**
     * to preselect the checkpoxes
     * @return
     */
    public Set<UserxRole> getSelectedRoles() {
        oldRoles = new TreeSet<>(user.getRoles());
        return oldRoles;
    }

    public void setSelectedRoles(Set<UserxRole> selectedRoles) {
        this.newRoles = selectedRoles;
    }

    /**
     * just to display all role types
     * @return
     */
    public List<UserxRole> getAllRoles() {
        return Arrays.asList(UserxRole.USER, UserxRole.MANAGER, UserxRole.ADMIN);
    }

    /**
     * sets the new Roles to user and saves it.
     * if the roles changed it creates an rolChangeLog
     */
    public void saveUserPlusSaveRoles() {
        user.setRoles(new HashSet<>(newRoles));
        doSaveUser();

        if (newRoles.equals(oldRoles)) { //Rols didn't change
            return;
        }
        RolChangeLog rolChangeLog = new RolChangeLog();
        rolChangeLog.setUser(user);
        rolChangeLog.setNewRoles(newRoles.stream().toList());
        rolChangeLog.setOldRoles(oldRoles.stream().toList());
        rolChangeLog.setChanegeTime(LocalDateTime.now());
        userService.saveRolChangeLog(rolChangeLog);
    }

    public void resetPasswordEmail() {
        String token = tokenService.generateTokenString();
        tokenService.createVerificationToken(user, token);
        emailService.setEmailStrategy(new PasswordChangeMailStrategy());
        emailService.sendMail(user.getEmail(), token);
    }

    /**
     * Methode to check validate the Passwords.
     */
    public void checkPasswords() {
        if(newUser != null) {
            passwordValidationService.validatePasswords(confirmPassword, newUser.getPassword(), "register_form:confirmPassword");
        } else if (user != null) {
            passwordValidationService.validatePasswords(confirmPassword, user.getPassword(), "oneUserForm:confirmPassword");
        }
        confirmPassword = null;
    }

    /**
     * Methode to check if Username is still available
     */
    public void checkUsernameAvailability() {
        String username = newUser.getUsername();

        Userx existingUser = userService.loadUser(username);

        FacesMessage message;

        if (existingUser != null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username is already taken!");
            FacesContext.getCurrentInstance().addMessage("register_form:username", message);
        }
    }


}
