package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.PremiumHistory;
import at.qe.skeleton.internal.model.Userx;

import java.io.Serializable;
import java.util.List;

/**
 * Repository for managing {@link PremiumHistory} entities.
 *
 */
public interface PremiumHistoryRepository extends AbstractRepository <PremiumHistory, Long>, Serializable {

    public PremiumHistory findByUser (Userx user);

    List<PremiumHistory> findAllByUserId(String userId);


}
