import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by ex63046 on 2017.04.11..
 * Source: MKyong.com
 * https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
 */
public class SendMailTLS {

    public static void send() throws Exception{

        final String username = "username@gmail.com";
        final String password = "password";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("from-email@gmail.com"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse("to-email@gmail.com"));
        message.setSubject("Testing Subject");
        message.setText("Dear Mail Crawler,"
                + "\n\n No spam to my email, please!");

        Transport.send(message);

        System.out.println("Done");
    }

}
