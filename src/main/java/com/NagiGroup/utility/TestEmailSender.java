package com.NagiGroup.utility;

import java.io.File;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class TestEmailSender {

    public static void main(String[] args) {
        // Sender email and password (for Gmail in this case)
        String fromEmail = "nagikaran7020@gmail.com"; // Replace with your email
        String password = "nsfp sizb jnhu oegi"; // Replace with your email password
        String toEmail = "nagikaran1111@gmail.com"; // Replace with recipient email
        String subject = "Test Email Subject";
        String body = "This is a test email sent using JavaMail API";
        
        
        
        // SMTP server details for Gmail
        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587;

        // Setting up the mail session properties
        
        
        
        
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        // Get the default Session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Create a multipart message for sending an attachment
            Multipart multipart = new MimeMultipart();

            // Adding text to the email
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            // Adding an attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            File file = new File("D:/NAGI_GROUP/INVOICES/LD999-999/LD999-999.pdf"); // Replace with actual file path
            attachmentPart.attachFile(file);
            multipart.addBodyPart(attachmentPart);

            // Set the complete message parts
            message.setContent(multipart);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}
