package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.CreditCardService;
import at.qe.skeleton.internal.services.UserxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;

@SpringBootTest
@WebAppConfiguration
public class CreditCardServiceTest {

    @Autowired
    private UserxService userxService;

    @Autowired
    private CreditCardService creditCardService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DirtiesContext
    public void testCreateAssignAndDeleteCreditCard() {
        //create and save user
        Userx user = new Userx();
        user.setUsername("testUserCredit");
        user.setPassword("testPassword");
        user.setRoles(Set.of(UserxRole.USER));

        userxService.saveUser(user);

        // create and save credit card
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber("1234567890123456");
        creditCard.setName("Test User");
        creditCard.setCvc("123");

        creditCardService.saveCreditCard(creditCard);

        // assign credit card to user
        user.setCreditCard(creditCard);
        userxService.saveUser(user);

        // check assignment
        Userx userWithCreditCard = userxService.loadUser("testUserCredit");
        Assertions.assertNotNull(userWithCreditCard.getCreditCard(), "Credit card not assigned to user");

        userxService.deleteUser(userWithCreditCard);

        // check cascading deletion
        CreditCard deletedCreditCard = creditCardService.loadCreditCard("1234567890123456");
        Assertions.assertNull(deletedCreditCard, "Credit card not deleted when associated user is deleted");
    }
}
