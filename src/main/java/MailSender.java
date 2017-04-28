import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.stringtemplate.v4.*;

/**
 * Created by ex63046 on 2017.04.11..
 */
public class MailSender {

    private final static Logger logger = Logger.getLogger(MailSender.class.getName());
    static FileHandler logFileHandler;

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args){

        Map<String, String> emailProperties;
        Map<String, String> accountProperties;

        try{
            // logger
            Util.initLogger(Paths.get(".", "MailSender.log").toAbsolutePath().toString());

            // properties
            emailProperties = Util.parseProperties(Paths.get(".", "email.properties").toAbsolutePath().toString());
            accountProperties = Util.parseProperties(Paths.get(".", "email_account.properties").toAbsolutePath().toString());

            Util.printProperties(emailProperties);
            Util.printProperties(accountProperties);

            // recipients
            List<String> recipients = Util.parseRecipients(emailProperties.get("recipientsSource"));
            Util.printRecipients(recipients, "Recipients");

            // already sent recipients
            List<String> alreadySentRecipients = Util.parseAlreadySentRecipients(emailProperties.get("alreadySentRecipientsSource"));
            Util.printRecipients(alreadySentRecipients, "Already sent recipients");
            List<String> newAlreadySentRecipients = new ArrayList<>();

            try{
                Util.sendEmails(emailProperties, accountProperties, recipients, alreadySentRecipients, newAlreadySentRecipients);
            } catch (Exception e) {
                throw  e;
            } finally {
                // write alreadySentEmails
                if (newAlreadySentRecipients.size() > 0) {
                    Util.writeToFile(newAlreadySentRecipients, Paths.get(".", emailProperties.get("alreadySentRecipientsSource")));
                }
            }



        } catch (Exception e) {
            e.printStackTrace();

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            logger.severe(e.getMessage() + " - "  + errors);
        }
    }


    // UTIL
    public static class Util {

        static Function<String, String> transforRecipientEmailToName = (email) ->  email.substring(0, email.indexOf('@'));

        public static void sendEmails(Map<String, String> emailProperties, Map<String, String> accountProperties, List<String> recipients, List<String> alreadySentRecipients, List<String> newAlreadySentRecipients) throws Exception{
            // params to email body template
            String senderName = emailProperties.get("senderName");

            int counter = 0;

            STGroup templateGrp = new STGroupDir(Paths.get("." ).normalize().toAbsolutePath().toString(), "UTF-8", '{', '}');

            try {

                for (String recipientEmail : recipients) {

                    if (alreadySentRecipients.contains(recipientEmail)) {
                        logger.info("Mail already sent to " + recipientEmail + ". Don't send it again.");
                        continue;
                    }

                    try {

                        String tplName = emailProperties.get("emailBodyTemplate");
                        ST emailBodyTemplate = templateGrp.getInstanceOf(tplName);
                        emailBodyTemplate.add("senderName", senderName);
                        emailBodyTemplate.add("recipientName", transforRecipientEmailToName.apply(recipientEmail));
                        String parsedBody = emailBodyTemplate.render();


                        Map<String, String> mailData = new HashMap<>();
                        mailData.put("body", parsedBody);
                        mailData.put("recipientEmail", recipientEmail);
                        mailData.put("subject", emailProperties.get("subject"));
                        mailData.put("senderEmail", accountProperties.get("email"));
                        mailData.put("username", accountProperties.get("username"));
                        mailData.put("password", accountProperties.get("password"));

                        SendMailSSL.send(mailData);

                        // add to sent mails
                        newAlreadySentRecipients.add(recipientEmail);

                        counter++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            } catch (Exception e) {
                throw e;
            } finally  {
                logger.info("Sent messages (pieces) : " + counter);
            }

        }

        public static void initLogger(String fileName) throws  Exception{

            // This block configure the logger with handler and formatter
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s %5$s %6$s%n");

            logFileHandler = new FileHandler(fileName);
            logger.addHandler(logFileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            logFileHandler.setFormatter(formatter);
        }

        public static Map<String, String> parseProperties(String propertyName) throws Exception{
            Properties properties = new Properties();
            Map<String, String> map = new HashMap<>();

            try( InputStream inputstream = new FileInputStream(propertyName) ) {

                properties.load(inputstream);

            } catch (Exception e) {
                throw  e;
            }

            for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
                map.put((String) entry.getKey(), (String) entry.getValue());
            }

            return map;
        }

        public static void printProperties(Map<String, String> properties) throws  Exception {
            logger.info("----------------------Properties--------------------");
            for ( Map.Entry<String, String>  entry : properties.entrySet()) {
                logger.info(entry.getKey() + " :: " +  entry.getValue());
            }
            logger.info("----------------------End Properties--------------------");
        }

        public static void printRecipients(List<String> recipients, String title) throws  Exception {
            logger.info("----------------------" + title + "--------------------");
            for ( String recipient : recipients) {
                logger.info(recipient);
            }
            logger.info("----------------------End " + title + "--------------------");
        }

        public static List<String> parseRecipients(String fileName) throws Exception{
            return parseEmailAdressesFromFile(fileName);
        }

        public static List<String> parseAlreadySentRecipients(String fileName) throws Exception{
            return parseEmailAdressesFromFile(fileName);
        }


        public static List<String> parseEmailAdressesFromFile(String fileName) throws Exception{
            logger.info(fileName);
            Path path = Paths.get(".", fileName);

            try(Stream<String> lines = Files.lines(path)){

            } catch (IOException ioe) {
                return new ArrayList<>();
            }

            List list;
            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

                list = stream
                        .map(String :: trim)
                        .collect(Collectors.toList());

            } catch (IOException e) {
                throw e;
            }

            return list;
        }

        public static void writeToFile(List<String> lines, Path path) throws Exception{
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }

    }

}
