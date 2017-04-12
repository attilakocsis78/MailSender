import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by ex63046 on 2017.04.11..
 * Source: MKyong.com
 * https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
 */
public class SendMailTLS {

    private final static Logger logger = MailSender.getLogger();


    public static void send(Map<String, String> mailData) throws Exception{

        final String body = mailData.get("body");
        final String recipientEmail = mailData.get("recipientEmail");
        final String subject = mailData.get("subject");
        final String senderEmail = mailData.get("senderEmail");
        final String username = mailData.get("username");
        final String password = mailData.get("password");


        logger.info("----------------------Email--------------------");
        logger.info("senderEmail :: " + senderEmail);
        logger.info("recipientEmail :: " + recipientEmail);
        logger.info("subject :: " + subject);
        logger.info("body :: " + body);

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
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);

        logger.info("Email sent.");
        logger.info("----------------------End Email--------------------");
    }

}
