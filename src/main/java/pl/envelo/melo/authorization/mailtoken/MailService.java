package pl.envelo.melo.authorization.mailtoken;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.configuration.MailConfiguration;
import pl.envelo.melo.domain.event.EventRepository;

import java.util.Properties;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailTokenRepository mailTokenRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    private final MailConfiguration mailConfiguration;

    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.starttls}")
    private String starttls;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.passwd}")
    private String passwd;
    @Value("${mail.address}")
    public String email;

    public MailToken generateToken(int eventId, int personId) {
        MailToken token = new MailToken();
        token.setPerson(personRepository.findById(personId).get());
        token.setEvent(eventRepository.findById(eventId).get());
        mailTokenRepository.save(token);
        return token;
    }

    public boolean sendMail(String personEmail, String eventName, String msg ){
        try {
            Session session = Session.getInstance(this.setProperties(), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, passwd);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(personEmail));
            message.setSubject("Rejestracja na event \""+eventName+"\"");
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            return  true;
        } catch (MessagingException e) {
            return false;
        }
    }
    public Properties setProperties() {

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", starttls);
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);
        return prop;
    }


}
