package at.qe.skeleton.internal.services.email;

import org.springframework.mail.SimpleMailMessage;

/**
 * Implementation of {@link EmailStrategy} for configuring password reset email messages.
 */
public class PasswordChangeMailStrategy implements EmailStrategy {
    @Override
    public void configureMail(SimpleMailMessage message, String token) {
        message.setSubject("Password change for the WeatherApp");
        message.setText("Please click this link to change your password:\n"
                + "http://localhost:8080/user/reset_password.xhtml?token=" + token);
    }
}
