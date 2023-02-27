package pl.envelo.melo.authorization.mailtoken;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService {
    private final MailTokenRepository mailTokenRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;


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
    @Value("${mail.urlprefix}")
    private String urlPrefix;
    @Value("${mail.address}")
    public String email;


    public MailToken generateToken(int eventId, int personId) {
        MailToken token = new MailToken();
        token.setPerson(personRepository.findById(personId).get());
        token.setEvent(eventRepository.findById(eventId).get());
        mailTokenRepository.save(token);
        return token;
    }

    public boolean sendMailWithToken(Person person, Event event, Boolean mailType) {
        try {
            Session session = Session.getInstance(this.setProperties(), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, passwd);
                }
            });
            MailToken mailToken = new MailToken();
            mailToken.setEvent(event);
            mailToken.setPerson(person);
            mailTokenRepository.save(mailToken);

            String subject = "%s";
            String content;
            if (mailType) {
                subject = "Rejestracja na wydarzenie \"%s\"";
                content = "%s";
            } else {
                subject = "Informacje o wydarzeniu \"%s\"";
                content = "%s";
            }
            String link = urlPrefix + "events/participation?token=" + mailToken.getToken().toString();

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(person.getEmail()));
            message.setSubject(String.format(subject, event.getName()));
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(String.format(content, link), "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            return true;
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
