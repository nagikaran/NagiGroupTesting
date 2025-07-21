package com.NagiGroup.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.FromStringTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SentDateTerm;

public class TruckAndTrailerRecieptDownloader {

    public static void main(String[] args) {
        try {
        	String[] repairKeywords = {
        		    "repair", "truck repair", "trailer repair","trailer service", "fix", "maintenance", "service"
        		};
            downloadPDFs("nagigroup0076@gmail.com", "tayy fiqr rhze yurb", "manpreetlubana1988@gmail.com", repairKeywords, "C:/Jimmy/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadPDFs(String username, String password, String fromEmail, String[] repairKeywords, String outputFolder) throws Exception {
        System.out.println("Starting mail download process...");

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);

        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", username, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 7, 20);

        SearchTerm dateTerm = new AndTerm(
            new SentDateTerm(ComparisonTerm.GE, java.sql.Date.valueOf(start)),
            new SentDateTerm(ComparisonTerm.LT, java.sql.Date.valueOf(end))
        );

        SearchTerm fromTerm = new FromStringTerm(fromEmail);
        SearchTerm combined = new AndTerm(new SearchTerm[]{dateTerm, fromTerm});

        Message[] messages = inbox.search(combined);
        System.out.println("Filtered messages from " + fromEmail + ": " + messages.length);

        List<Message> repairMessages = new ArrayList<>();
        for (Message msg : messages) {
            String subject = msg.getSubject();
            if (subject != null) {
                String subjectLower = subject.toLowerCase();
                for (String keyword : repairKeywords) {
                    if (subjectLower.contains(keyword)) {
                        repairMessages.add(msg);
                        break;
                    }
                }
            }
        }

        int pdfCount = 0;
        for (Message message : repairMessages) {
            pdfCount += savePDFAttachments(message, outputFolder);
        }

        System.out.println("✅ Process complete. Total PDFs downloaded: " + pdfCount);

        inbox.close(false);
        store.close();
    }

    private static int savePDFAttachments(Message message, String outputFolder) throws Exception {
        int count = 0;

        if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                count += processPart(part, outputFolder, message); // ✅ call the improved method
            }
        }

        return count;
    }


    private static int processPart(BodyPart part, String outputFolder, Message message) throws Exception {
        int count = 0;

        if (part.isMimeType("multipart/*")) {
            Multipart nested = (Multipart) part.getContent();
            for (int i = 0; i < nested.getCount(); i++) {
                count += processPart(nested.getBodyPart(i), outputFolder, message); // recursive call
            }
        } else {
            String disposition = part.getDisposition();
            String fileName = part.getFileName();

            if (fileName != null) {
                fileName = MimeUtility.decodeText(fileName);
            }

            boolean isPDF = fileName != null && fileName.toLowerCase().endsWith(".pdf");
            boolean isAttachment = disposition == null ||
                                   disposition.equalsIgnoreCase(Part.ATTACHMENT) ||
                                   disposition.equalsIgnoreCase(Part.INLINE);

            if (isPDF && isAttachment) {
                // Get sent date
                Date sentDate = message.getSentDate();
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(sentDate);

                // Get sender email/name
                Address[] froms = message.getFrom();
                String sender = (froms != null && froms.length > 0) ? froms[0].toString() : "UnknownSender";
                sender = sender.replaceAll("[<>:\"/\\\\|?*]", "_"); // remove invalid chars
                String[] split = sender.split("@");
                String senderName = split[0];
                // Rename file
                String newFileName = "Truck_Repair_Receipt_" + formattedDate + "_" + senderName + ".pdf";

                // Save file
                InputStream is = part.getInputStream();
                Path outputPath = Paths.get(outputFolder, newFileName);
                Files.copy(is, outputPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("✅ Downloaded PDF: " + newFileName);
                count++;
            }
        }

        return count;
    }

}
