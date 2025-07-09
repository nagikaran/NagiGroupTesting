package com.NagiGroup.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.FolderClosedException;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.BodyTerm;
import jakarta.mail.search.OrTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;

public class GmailIMAPDownloader02 {

    public static void downloadPdfAttachmentsByLoadNumbers(String email, String appPassword, List<String> loadNumbers, String downloadDir) throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");

        // IMAP timeout settings
        props.put("mail.imap.connectiontimeout", "60000");
        props.put("mail.imap.timeout", "60000");
        props.put("mail.imap.writetimeout", "60000");

        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", email, appPassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        List<String> savedFiles = new ArrayList<>();

        for (String loadNumber : loadNumbers) {
            SearchTerm subjectTerm = new SubjectTerm(loadNumber);
            SearchTerm bodyTerm = new BodyTerm(loadNumber);
            SearchTerm searchTerm = new OrTerm(subjectTerm, bodyTerm);

            Message[] messages;
            try {
                messages = inbox.search(searchTerm);
            } catch (FolderClosedException e) {
                System.out.println("INBOX closed during search. Reopening...");
                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
                messages = inbox.search(searchTerm);	
            }

            System.out.println("Found " + messages.length + " messages for load number: " + loadNumber);

            for (Message message : messages) {
                try {
                    String subject = message.getSubject() != null ? message.getSubject().toLowerCase() : "";
                    Address[] fromAddresses = message.getFrom();
                    String fromEmail = (fromAddresses != null && fromAddresses.length > 0)
                            ? fromAddresses[0].toString().toLowerCase()
                            : "";

                    if (subject.contains("triumphpay") || fromEmail.contains("triumphpay")
                            || subject.contains("umair mubashir") || fromEmail.contains("umair mubashir")) {
                        System.out.println("Skipped email (TriumphPay or Umair Mubashir) for load number: " + loadNumber);
                        continue;
                    }

                    if (message.isMimeType("multipart/*")) {
                        Multipart multipart = (Multipart) message.getContent();
                        boolean pdfSavedForThisMessage = false;

                        for (int i = 0; i < multipart.getCount(); i++) {
                            if (pdfSavedForThisMessage) break;

                            BodyPart part = multipart.getBodyPart(i);

                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
                                    && part.getFileName().toLowerCase().endsWith(".pdf")) {

                                try (InputStream is = part.getInputStream()) {
                                    String fileName = loadNumber + "_roc.pdf";
                                    File file = new File(downloadDir + File.separator + fileName);

                                    try (FileOutputStream fos = new FileOutputStream(file)) {
                                        byte[] buf = new byte[4096];
                                        int bytesRead;
                                        while ((bytesRead = is.read(buf)) != -1) {
                                            fos.write(buf, 0, bytesRead);
                                        }
                                    }

                                    System.out.println("Saved attachment: " + file.getAbsolutePath());
                                    savedFiles.add(file.getAbsolutePath());
                                    pdfSavedForThisMessage = true;
                                }
                            }
                        }
                    }

                } catch (FolderClosedException e) {
                    System.out.println("Folder closed during message processing. Reopening...");
                    inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
                    // Optional: retry this message if needed
                } catch (Exception ex) {
                    System.err.println("Error processing message for load number " + loadNumber + ": " + ex.getMessage());
                }
            }

            // Optional: short pause between load numbers to reduce server pressure
            Thread.sleep(500);
        }

        // Close resources safely
        try {
            if (inbox != null && inbox.isOpen()) inbox.close(false);
        } catch (Exception e) {
            System.err.println("Failed to close inbox: " + e.getMessage());
        }
        try {
            if (store != null && store.isConnected()) store.close();
        } catch (Exception e) {
            System.err.println("Failed to close store: " + e.getMessage());
        }

        // Summary
        System.out.println("--------------------------------------------------");
        System.out.println("PDF Download Summary:");
        if (savedFiles.isEmpty()) {
            System.out.println("No PDF attachments were saved.");
        } else {
            for (String path : savedFiles) {
                System.out.println("✔ Saved: " + path);
            }
        }
        System.out.println("Total PDFs saved: " + savedFiles.size());
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) throws Exception {
//        String email = ""; // <-- Your Gmail address
//        String appPassword = ""; // <-- Your app-specific password from Gmail
    	String email = "nagigroup0076@gmail.com";
        String appPassword = "mnhl ivju dgdq rald";
        List<String> loadNumbers = List.of(
        		"6926472","513237093","3662517","2034163","3665817","514038346","513582133","8513772",
        		"10013283","3665907","6972868","6919925","514971438","3519106-1","514917371","3669507","515248413"

        	);


        String downloadDir = "D:/NAGI_GROUP/DOCUMENTS_2025/MAY/jimmy/DISPATCH RECORD";

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
