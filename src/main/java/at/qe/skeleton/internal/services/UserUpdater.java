package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Class to save the User. Needed for the PremiumStatusListener.
 */
@Component
public class UserUpdater implements Serializable {

    @Autowired
    private UserxRepository userxRepository;

    public void updateUser(Userx user) {
        userxRepository.save(user);
    }
}

