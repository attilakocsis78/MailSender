import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by ex63046 on 2017.04.12..
 */
public class SendMailSSL {

    private final static Logger logger = MailSender.getLogger();

    private Session session;
    private final String username;
    private final String password;
    private final String subject;
    private final String senderEmail;

    public SendMailSSL(Map<String, String> mailData) {
        subject = mailData.get("subject");
        senderEmail = mailData.get("senderEmail");
        username = mailData.get("username");
        password = mailData.get("password");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public void send(Map<String, String> mailData) throws Exception{

        final String body = mailData.get("body");
        final String recipientEmail = mailData.get("recipientEmail");



        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);



        logger.info("----------------------Email--------------------");
        logger.info("senderEmail :: " + senderEmail);
        logger.info("recipientEmail :: " + recipientEmail);
        logger.info("subject :: " + subject);
        logger.info("body :: " + body);


        Transport transport = session.getTransport("smtp");
        transport.connect(username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

        //Transport.send(message);

        logger.info("Email sent.");
        logger.info("----------------------End Email--------------------");
    }
}
