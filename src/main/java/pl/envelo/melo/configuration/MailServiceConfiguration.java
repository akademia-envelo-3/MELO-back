package pl.envelo.melo.configuration;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

@AllArgsConstructor
public class MailServiceConfiguration {

    @Value("${mail.host}")
    private static String host;
    @Value("${mail.port}")
    private static String port;
    @Value("${mail.starttls}")
    private static boolean starttls;
    @Value("${mail.username}")
    private static String username;
    @Value("${mail.passwd}")
    private static String passwd;


    public static Properties setProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", starttls);
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
        return prop;
    }

    public static Session session = Session.getInstance(setProperties(), new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, passwd);
        }
    });


}
