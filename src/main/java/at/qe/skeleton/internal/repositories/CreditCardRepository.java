package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.model.Userx;

import java.io.Serializable;

/**
 * Repository for managing {@link CreditCard} entities.
 *
 */
public interface CreditCardRepository extends AbstractRepository<CreditCard, String>, Serializable {
    public CreditCard findFirstByNumber(String number);

    public Boolean existsByUser (Userx user);

    public CreditCard findByUser (Userx user);
}
