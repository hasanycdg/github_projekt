package at.qe.skeleton.internal.services.email;

import org.springframework.mail.SimpleMailMessage;

import java.io.Serializable;

/**
 * Interface for configuring email messages with a specific strategy.
 */
public interface EmailStrategy extends Serializable {
    void configureMail(SimpleMailMessage message, String token);
}
