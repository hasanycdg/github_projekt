package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.Token;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.TokenRepository;
import at.qe.skeleton.internal.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVerificationToken() {
        Userx user = new Userx();
        String tokenValue = "someToken";

        tokenService.createVerificationToken(user, tokenValue);

        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void getUserByConfirmationToken() {
        String tokenValue = "someToken";
        Token token = new Token(tokenValue, new Userx());

        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(token);

        Userx resultUser = tokenService.getUserByConfirmationToken(tokenValue);

        assertEquals(token.getUser(), resultUser);
    }

    @Test
    void getUserByInvalidConfirmationToken() {
        String invalidTokenValue = "invalidToken";

        when(tokenRepository.findByTokenValue(invalidTokenValue)).thenReturn(null);

        Userx resultUser = tokenService.getUserByConfirmationToken(invalidTokenValue);

        assertNull(resultUser);
    }

    @Test
    void deleteToken() {
        Token token = new Token("someToken", new Userx());

        tokenService.deleteToken(token);

        verify(tokenRepository, times(1)).delete(token);
    }

    @Test
    void findByTokenString() {
        String tokenValue = "someToken";
        Token expectedToken = new Token(tokenValue, new Userx());

        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(expectedToken);

        Token resultToken = tokenService.findByTokenString(tokenValue);

        assertEquals(expectedToken, resultToken);
    }

    @Test
    void findByInvalidTokenString() {
        String invalidTokenValue = "invalidToken";

        when(tokenRepository.findByTokenValue(invalidTokenValue)).thenReturn(null);

        Token resultToken = tokenService.findByTokenString(invalidTokenValue);

        assertNull(resultToken);
    }
}

