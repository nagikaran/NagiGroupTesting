package com.NagiGroup.config.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.NagiGroup.model.mail.MailModel;
import com.NagiGroup.utility.PropertiesReader;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

/**
 * This class is responsible for sending email functionality.
 */
@Component
public class SendMailUtility {

    private static final Logger infoLogger = LoggerFactory.getLogger(SendMailUtility.class);
    private static final Logger errorLogger = LoggerFactory.getLogger(SendMailUtility.class);

    public static String SMTP = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_SMTP");
    public static String USERNAME = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_USERNAME");
    public static String PASSWORD = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_PASSWORD");
    public static String PORT = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_SMTP_PORT");
    public static String AUTH = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_SMTP_AUTH");
    private static String MailSent = PropertiesReader.getProperty("constant", "MAIL_SENT");
    public static String FROM_EMAIL_ID = PropertiesReader.getProperty("constant", "FROM_EMAIL_ID");
    public static String STARTTLS_ENABLE = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_STARTTLS_ENABLE");
    public static String SSL_ENABLE = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_SSL_ENABLE");
    public static String SSL_TRUST = PropertiesReader.getProperty("constant", "LIVE_MAIL_SERVER_SSL_TRUST");

    public static boolean sendMail(MailModel mailModel) {
        infoLogger.info("mailModel: " + mailModel);
        boolean responseStatus = false;	

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP);
            props.put("mail.smtp.port", PORT);
            props.put("mail.smtp.ssl.enable", SSL_ENABLE);
            props.put("mail.smtp.auth", AUTH);
            props.put("mail.smtp.starttls.enable", STARTTLS_ENABLE);
            props.put("mail.smtp.ssl.trust", SSL_TRUST);
            
            //System.out.println("SMTP config: host=" + SMTP + ", port=" + PORT + ", ssl=" + SSL_ENABLE + ", starttls=" + STARTTLS_ENABLE);


            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            
            if (FROM_EMAIL_ID == null || FROM_EMAIL_ID.trim().isEmpty()) {
            	infoLogger.info("ERROR: FROM_EMAIL_ID is null or empty.");
            } else {
            	infoLogger.info("Setting From with: " + FROM_EMAIL_ID);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL_ID.trim(), "NagiGroup.inc"));
            if (mailModel.getRecipientEmailIds() != null && !mailModel.getRecipientEmailIds().trim().isEmpty()) {
            	infoLogger.info("SendMailUtility: send mail: To Mail Id: "+mailModel.getRecipientEmailIds());
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(mailModel.getRecipientEmailIds()));
            }

            if (mailModel.getAlternateCC() != null && !mailModel.getAlternateCC().trim().isEmpty()) {
            	
                message.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(mailModel.getAlternateCC()));
            }
            
            message.setSubject(mailModel.getSubject());
            
            // Body + attachments
            Multipart multipart = new MimeMultipart();
            
            MimeBodyPart bodyPart = new MimeBodyPart();
            
            if (mailModel.getIsHtml()) {
            	 
                bodyPart.setContent(mailModel.getBody(), "text/html; charset=utf-8");
            } else {
            	 
                bodyPart.setText(mailModel.getBody());
            }
            
            multipart.addBodyPart(bodyPart);
            
            // Attachments
            List<File> attachments = mailModel.getAttachments();
            if (attachments != null && !attachments.isEmpty()) {
            	 
                for (File file : attachments) {
                    if (file != null && file.exists()) {
                    	
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.attachFile(file);
                        multipart.addBodyPart(attachmentPart);
                      
                    }
                }
            }
           

            message.setContent(multipart);
            
            if (MailSent.equalsIgnoreCase("TRUE")) {
            	
                infoLogger.info("Sending mail...");
                
                Transport.send(message);
                infoLogger.info("Mail sent successfully.");
                
            } else {
            	 
            	infoLogger.info("Mail NOT sent because MailSent = " + MailSent);
                
            }
            
            responseStatus = true;
        } catch (MessagingException | IOException e) {
        	 
            e.printStackTrace();
            errorLogger.error("SendMailUtility : Error At : sendMail " + e.toString());
            throw new RuntimeException(e);
        }
       
        return responseStatus;
    }
   
}
