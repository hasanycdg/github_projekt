package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.Token;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

/**
 * Service class for handling verification tokens.
 */
@Component
@Scope("application")
public class TokenService implements Serializable {

    @Autowired
    private TokenRepository tokenRepository;

    /**
     * Creates a new verification token for the specified user and persists it.
     *
     * @param user  The user for whom the token is created.
     * @param token The token string.
     */
    public void createVerificationToken (Userx user, String token){
        Token myToken = new Token(token, user);
        tokenRepository.save(myToken);
    }

    public Userx getUserByConfirmationToken(String token) {
        Token tokenEntity =  tokenRepository.findByTokenValue(token);
        if(tokenEntity != null){
            return tokenEntity.getUser();
        } else {
            return null;
        }
    }

    public void deleteToken(Token token){
        tokenRepository.delete(token);
    }

    public Token findByTokenString(String token){
        return tokenRepository.findByTokenValue(token);
    }

    public String generateTokenString() {
        return UUID.randomUUID().toString();
    }
}
