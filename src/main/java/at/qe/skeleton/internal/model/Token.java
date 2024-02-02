package at.qe.skeleton.internal.model;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Entity representing a user token.
 * <p>
 * A token is used to verify a user's email address and further enable the account.
 * <p>
 * The token is generated when a new user is created and sent to the user's email address.
 * The user can then use the token to verify his email address.
 * <p>
 * The token is valid for 24 hours (fictional).
 */
@Entity
public class Token {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String tokenValue;

    @ManyToOne(targetEntity = Userx.class, fetch = FetchType.EAGER)
    Userx user;

    private Date expiryDate; //not used yet, we assume that the user performs the verification within 24 hours

    public Token(String tokenValue, Userx user) {
        this.tokenValue = tokenValue;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    public Token() {

    }

    private Date calculateExpiryDate() {
        Date now = new Date();
        long time = now.getTime();
        return new Date(time + EXPIRATION * 60 * 1000);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String token) {
        this.tokenValue = token;
    }

    public Userx getUser() {
        return user;
    }

    public void setUser(Userx user) {
        this.user = user;
    }
}
