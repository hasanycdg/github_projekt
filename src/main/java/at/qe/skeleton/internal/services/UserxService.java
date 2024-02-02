package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.RolChangeLog;
import at.qe.skeleton.internal.model.Userx;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.repositories.RolChangeLogRepository;
import at.qe.skeleton.internal.ui.controllers.PremiumStatusListener;
import at.qe.skeleton.internal.repositories.FavLocationRepository;
import at.qe.skeleton.internal.model.Token;
import at.qe.skeleton.internal.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import at.qe.skeleton.internal.repositories.UserxRepository;

/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("application")
public class UserxService implements Serializable {

    @Autowired
    private UserxRepository userRepository;

    @Autowired
    private FavLocationRepository locationRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PremiumHistoryService premiumHistoryService;

    @Autowired
    private PremiumStatusListener premiumStatusListener;

    @Autowired
    private RolChangeLogRepository rolChangeLogRepository;

    @Autowired
    private UserUpdater userUpdater;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')" )
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    public Userx loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link Userx#createDate} for new
     * entities or {@link Userx#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Userx#createDate}
     * or {@link Userx#updateUser} respectively.
     * Observer on the saveUser Methode. If the Premium Status is changed an PropertyChangeEvent is
     * triggered.
     *
     * @param user the user to save
     * @return the updated user
     */
    //Currently using this saveUser for the registration of a new User. Nobody logged in so no authority.
    // @PreAuthorize("hasAuthority('ADMIN')")
    public Userx saveUser(Userx user) {
        boolean oldPremiumStatus = userRepository.findById(user.getUsername()).map(Userx::isPremium).orElse(false);
        user = userRepository.save(user);
        boolean newPremiumStatus = user.isPremium();

        if (oldPremiumStatus != newPremiumStatus) {
            premiumStatusListener.propertyChange(new PropertyChangeEvent(user, "premium", oldPremiumStatus, newPremiumStatus));
        }
        return user;
    }

    public Userx getUserByConfirmationToken(String token) {
        Token tokenEntity =  tokenRepository.findByTokenValue(token);
        if(tokenEntity != null){
            return tokenEntity.getUser();
        } else {
            return null;
        }
    }

    public Userx getUserByEmail(String email){
        return userRepository.findFirstByEmail(email);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or principal.username == #user.id")
    public void deleteUser(Userx user) {
        userRepository.delete(user);
    }

    /**
     * remove Role from User
     * @param role the role that gets removed
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public void removeRole(Userx user, UserxRole role){
        Set<UserxRole> roles = user.getRoles();
        roles.remove(role);
        user.setRoles(roles);
    }

    /**
     * add Role to User
     * @param user user who gets the role
     * @param role role that is added to the user
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public void addRole(Userx user, UserxRole role){
        Set<UserxRole> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
    }

    /**
     * for test purposes
     */
    public void setUnauthenticatedUser() {
        SecurityContextHolder.clearContext();
    }

    /**
     * returns a Collection of all premium Users.
     * @return
     */
    @PreAuthorize("hasAuthority('MANAGER')" )
    public Collection<Userx> getPremiumUsers() {
        return userRepository.findByPremiumTrue();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveRolChangeLog(RolChangeLog rollChangeLog){
        rolChangeLogRepository.save(rollChangeLog);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Long> getIdInRoleChaneLog(){
        return rolChangeLogRepository.findAllId();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public RolChangeLog getByIdInRoleChaneLog(Long id){
        return rolChangeLogRepository.findAllById(id);
    }
}