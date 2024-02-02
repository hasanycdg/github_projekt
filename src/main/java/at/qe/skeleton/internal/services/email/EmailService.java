package at.qe.skeleton.internal.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class EmailService implements Serializable {

    @Autowired
    private JavaMailSender javaMailSender;

    private EmailStrategy emailStrategy;


    public void sendMail(String to, String token){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply.weatherapp.uibk@gmail.com");
        message.setTo(to);

        //strategy pattern to configure the mail text
        if (emailStrategy != null) {
            emailStrategy.configureMail(message, token);
        } else {
            throw new IllegalStateException("Email strategy is not set.");
        }

        javaMailSender.send(message);
    }

    /**
     * Methode to send a simple email to a given user.
     * @param to
     * @param Subject
     * @param content
     */
    public void sendSimpleMail(String to, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply.weatherapp.uibk@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);
    }

    public void setEmailStrategy(EmailStrategy emailStrategy) {
        this.emailStrategy = emailStrategy;
    }

}
