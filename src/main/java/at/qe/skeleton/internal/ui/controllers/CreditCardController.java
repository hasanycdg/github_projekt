package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Random;


/**
 *Controller for CreditCard Information.
 *
 */
@Component
@Scope("view")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserDetailController userDetailController;

    private Random random = new Random();

    /**
     * Attribute to cache the CreditCard Information.
     */
    private CreditCard creditCard;


    /**
     * Attribute to cache the newly added CreditCard.
     */
    private CreditCard newCreditCard;

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * returns the newCreditCard created from the creditCard class.
     * @return
     */
    public CreditCard getNewCreditCard() {
        if(newCreditCard == null){
            newCreditCard = new CreditCard();
        }
        return newCreditCard;
    }

    /**
     * Sets the new CreditCard. This is targeted by any further calls of
     * {@Link #doAddCreditCard()}.
     *
     * @param newCreditCard
     */
    public void setNewCreditCard(CreditCard newCreditCard) {
        this.newCreditCard = newCreditCard;
    }

    /**
     * Action to save the currently displayed creditCard.
     */
    public void doSaveCreditCard() {
        newCreditCard.setUser(userDetailController.getUser());
        newCreditCard.setBalance(random.nextInt(51));
        userDetailController.getUser().setCreditCard(newCreditCard);

        creditCardService.saveCreditCard(newCreditCard);
        userDetailController.doSaveUser();
    }

    /**
     * Action to Load the creditCard given a certain User.
     * @param user
     * @return
     */

    public CreditCard doLoadCreditCard(Userx user){
        return creditCardService.findByUser(user);
    }

}
