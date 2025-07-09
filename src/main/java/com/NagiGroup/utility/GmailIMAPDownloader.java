package com.NagiGroup.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.BodyTerm;
import jakarta.mail.search.OrTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;

public class GmailIMAPDownloader {

    public static void downloadPdfAttachmentsByLoadNumbers(String email, String appPassword, List<String> loadNumbers, String downloadDir) throws Exception {
        // Setup properties for Gmail IMAP with SSL
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");

        // Get session and connect store
        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", email, appPassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        for (String loadNumber : loadNumbers) {
            // Create search terms for this load number
            SearchTerm subjectTerm = new SubjectTerm(loadNumber);
            SearchTerm bodyTerm = new BodyTerm(loadNumber);
            SearchTerm searchTerm = new OrTerm(subjectTerm, bodyTerm);

            Message[] messages = inbox.search(searchTerm);

            System.out.println("Found " + messages.length + " messages for load number: " + loadNumber);

            int fileCounter = 1; // To avoid overwriting files

            for (Message message : messages) {
                if (message.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) message.getContent();

                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart part = multipart.getBodyPart(i);

                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) 
                            && part.getFileName().toLowerCase().endsWith(".pdf")) {

                            InputStream is = part.getInputStream();

                            // Build file name with counter to avoid overwrites
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
        String email = "nagigroup0076@gmail.com";
        String appPassword = "mnhl ivju dgdq rald";
        List<String> loadNumbers = List.of("3591611", "504308052");
        String downloadDir = "D:/roc_docs";
        File directory = new File(downloadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created directory: " + directory.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + directory.getAbsolutePath());
                // Handle failure here if needed
            }
        }

// working fine
        
        downloadPdfAttachmentsByLoadNumbers(email, appPassword, loadNumbers, downloadDir);
    }
}
