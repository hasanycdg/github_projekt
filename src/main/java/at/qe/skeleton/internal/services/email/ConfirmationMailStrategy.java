package at.qe.skeleton.internal.services.email;

import org.springframework.mail.SimpleMailMessage;

/**
 * Implementation of {@link EmailStrategy} for configuring confirmation email messages.
 */
public class ConfirmationMailStrategy implements EmailStrategy {
    @Override
    public void configureMail(SimpleMailMessage message, String token) {
        message.setSubject("Confirm your email for the WeatherApp");
        message.setText("Please confirm your email by clicking on the link below:\n"
                + "http://localhost:8080/confirm?token=" + token);
    }
}
