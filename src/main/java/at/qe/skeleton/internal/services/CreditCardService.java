package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Service for accessing and manipulating credit card data.
 */
@Service
public class CreditCardService implements Serializable {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCard saveCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    public CreditCard loadCreditCard(String number){
        return creditCardRepository.findFirstByNumber(number);
    }

    public void deleteCreditCard(CreditCard creditCard) {
        creditCardRepository.delete(creditCard);
    }

    public CreditCard findByUser(Userx user) {return creditCardRepository.findByUser(user);}
}
