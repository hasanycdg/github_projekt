package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.PremiumHistory;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.PremiumHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for accessing and  manipulating Premium History data.
 */
@Component
@Scope("application")
public class PremiumHistoryService implements Serializable {

    @Autowired
    private PremiumHistoryRepository premiumHistoryRepository;

    /**
     * Builds and saves a new Premium-History with the given Parameters.
     * The time will be indicated with the actual timestamp.
     *
     * @param user to set the user for whom the change occurred
     * @param newPremiumStatus to set the new Status of Premium.
     *
     */
    public void savePremiumHistory(Userx user, boolean newPremiumStatus) {
        PremiumHistory premiumHistory = new PremiumHistory();
        premiumHistory.setUser(user);
        premiumHistory.setChangeDate(LocalDateTime.now());
        premiumHistory.setNewPremiumStatus(newPremiumStatus);
        premiumHistoryRepository.save(premiumHistory);
    }

    /**
     * Return a list with all the Premium-History entries for a certain user.
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasAuthority('MANAGER')" )
    public List<PremiumHistory> getPremiumChangedByName(String user) {
        return premiumHistoryRepository.findAllByUserId(user);
    }

    /**
     * finds and returns a list of all the dates where a change of the premium status occurred for all users
     * @return
     */
    @PreAuthorize("hasAuthority('MANAGER')" )
    public List<PremiumHistory> getPremiumChanged() {
        return premiumHistoryRepository.findAll();
    }

}