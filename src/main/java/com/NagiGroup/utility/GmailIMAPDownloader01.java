package com.NagiGroup.utility;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.search.BodyTerm;
import jakarta.mail.search.OrTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;

public class GmailIMAPDownloader01 {

    public static void downloadPdfAttachmentsByLoadNumbers(String email, String appPassword, List<String> loadNumbers, String downloadDir) throws Exception {
        // Setup Gmail IMAP properties
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");

        // Establish mail session and store connection
        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", email, appPassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        for (String loadNumber : loadNumbers) {
            SearchTerm subjectTerm = new SubjectTerm(loadNumber);
            SearchTerm bodyTerm = new BodyTerm(loadNumber);
            SearchTerm searchTerm = new OrTerm(subjectTerm, bodyTerm);

            Message[] messages = inbox.search(searchTerm);
            System.out.println("Found " + messages.length + " messages for load number: " + loadNumber);

            int fileCounter = 1;

            for (Message message : messages) {
                // Skip if subject or sender contains "TriumphPay"
                String subject = message.getSubject() != null ? message.getSubject().toLowerCase() : "";
                Address[] fromAddresses = message.getFrom();
                String fromEmail = (fromAddresses != null && fromAddresses.length > 0)
                        ? fromAddresses[0].toString().toLowerCase()
                        : "";

                if (subject.contains("triumphpay") || fromEmail.contains("triumphpay")) {
                    System.out.println("Skipped TriumphPay email for load number: " + loadNumber);
                    continue;
                }

                if (message.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) message.getContent();

                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart part = multipart.getBodyPart(i);

                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
                                && part.getFileName().toLowerCase().endsWith(".pdf")) {

                            InputStream is = part.getInputStream();

                            // Build file name
                            String fileName = loadNumber + "_roc";
                            if (fileCounter > 1) {
                                fileName += "_" + fileCounter;
                            }
                            fileName += ".pdf";

                            File file = new File(downloadDir + File.separator + fileName);
                            try (FileOutputStream fos = new FileOutputStream(file)) {
                                byte[] buf = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = is.read(buf)) != -1) {
                                    fos.write(buf, 0, bytesRead);
                                }
                            }

                            System.out.println("Saved attachment: " + file.getAbsolutePath());
                            fileCounter++;
                        }
                    }
                }
            }
        }

        inbox.close(false);
        store.close();
    }

    public static void main(String[] args) throws Exception {
//        String email = ""; // <-- Add your email here
//        String appPassword = ""; // <-- Add your app-specific password here
    	String email = "nagigroup0076@gmail.com";
        String appPassword = "mnhl ivju dgdq rald";
//        List<String> loadNumbers = List.of("3591611", "504308052");
        List<String> loadNumbers = List.of(
        	    "33149113",
        	    "141804889",
        	    "17335103",
        	    "17349619",
        	    "5032694",
        	    "78815606",
        	    "3667961",
        	    "32240129",
        	    "OHW507064",
        	    "5005546",
        	    "246064",
        	    "32240130",
        	    "31586-04397",
        	    "102356",
        	    "3000007261"
        	);

        String downloadDir = "D:/roc_docs01";
         downloadDir = "D:/NAGI_GROUP/DOCUMENTS_2025/MAY/Umair/DISPATCH RECORD";


        File directory = new File(downloadDir);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Created directory: " + directory.getAbsolutePath());
            } else {
                System.err.println("Failed to create directory: " + directory.getAbsolutePath());
                return;
            }
        }

        downloadPdfAttachmentsByLoadNumbers(email, appPassword, loadNumbers, downloadDir);
    }
}
