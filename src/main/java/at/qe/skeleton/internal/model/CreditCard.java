package at.qe.skeleton.internal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.io.Serializable;


/**
 * Represents a credit card entity associated with a user in the system.
 * The credit card is identified by its number, and it includes information such as the cardholder's name,
 * CVC (Card Verification Code), expiration date, and the current balance.
 * Additionally, it establishes a one-to-one relationship with a user entity.
 *
 * @Entity Indicates that this class is a JPA entity.
 * @Id Specifies the primary key attribute, which is the credit card number in this case.
 * @OneToOne Establishes a bidirectional one-to-one relationship with the Userx entity, where this CreditCard
 *           is the owning side of the relationship (mappedBy attribute points to the "creditCard" field in Userx).
 */
@Entity
public class CreditCard implements Serializable {

    @Id
    private String number;
    private String name;
    private String cvc;
    private String expirationDate;

    private double balance;
    @OneToOne(mappedBy = "creditCard")
    private Userx user;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Userx getUser() {
        return user;
    }

    public void setUser(Userx user) {
        this.user = user;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}