package com.NagiGroup.utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.search.*;

public class FuelTicketDownloader {

	public static void downloadFuelTickets(String email, String password) {
	    try {
	        System.out.println("Starting mail download process...");

	        Properties props = new Properties();
	        props.put("mail.store.protocol", "imaps");
	        Session session = Session.getDefaultInstance(props, null);
	        System.out.println("Mail session initialized");

	        Store store = session.getStore("imaps");
	        store.connect("imap.gmail.com", email, password);
	        System.out.println("Connected to Gmail IMAP");

	        Folder inbox = store.getFolder("INBOX");
	        inbox.open(Folder.READ_ONLY);
	        System.out.println("Inbox opened");

	     // Filter for May 2025
	        Calendar cal = Calendar.getInstance();
	        cal.set(2025, Calendar.MAY, 1, 0, 0, 0); // May 1, 2025
	        Date startDate = cal.getTime();

	        cal.set(2025, Calendar.JUNE, 1, 0, 0, 0); // June 1, 2025 (exclusive)
	        Date endDate = cal.getTime();

	        ReceivedDateTerm afterStart = new ReceivedDateTerm(ComparisonTerm.GE, startDate);
	        ReceivedDateTerm beforeEnd = new ReceivedDateTerm(ComparisonTerm.LT, endDate);

	        SearchTerm dateRange = new AndTerm(afterStart, beforeEnd);
	        Message[] mayMessages = inbox.search(dateRange);
	        System.out.println("Filtered May 2025: " + mayMessages.length + " messages");

	        FetchProfile fp = new FetchProfile();
	        fp.add(FetchProfile.Item.ENVELOPE); // fetch subject, from, date

	        String directoryPath = "D:/Nagi_Group/fuel_tickets/atwal/";
	        File directory = new File(directoryPath);
	        if (!directory.exists() && !directory.mkdirs()) {
	            System.out.println("Failed to create directory: " + directoryPath);
	            inbox.close(false);
	            store.close();
	            return;
	        }

	        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMMM_yyyy");
	        Map<String, Integer> fileCountMap = new HashMap<>();
	        int totalDownloaded = 0;

	        int batchSize = 500;
	        for (int i = 0; i < mayMessages.length; i += batchSize) {
	            int end = Math.min(i + batchSize, mayMessages.length);
	            Message[] batch = Arrays.copyOfRange(mayMessages, i, end);

	            inbox.fetch(batch, fp);

	            for (Message msg : batch) {
	                String subject = msg.getSubject() != null ? msg.getSubject().toLowerCase() : "";
	                Address[] froms = msg.getFrom();
	                String senderName = (froms != null && froms.length > 0)
	                        ? ((InternetAddress) froms[0]).getPersonal()
	                        : "";

	                if (subject.contains("fuel") && senderName != null &&
	                        senderName.toLowerCase().contains("Jaswant Atwal")) {

	                    Date sentDate = msg.getSentDate();
	                    String dateString = sentDate != null ? sdf.format(sentDate) : "unknown_date";

	                    Object content = msg.getContent();

	                    if (content instanceof Multipart) {
	                        totalDownloaded += handleMultipart((Multipart) content, directoryPath, dateString, fileCountMap);
	                    }
	                }
	            }
	        }

	        System.out.println("Downloaded PDF count: " + totalDownloaded);
	        inbox.close(false);
	        store.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private static int handleMultipart(Multipart multipart, String directoryPath, String dateString,
			Map<String, Integer> fileCountMap) throws Exception {
		int count = 0;
		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart part = multipart.getBodyPart(i);

			if (part.isMimeType("multipart/*")) {
				count += handleMultipart((Multipart) part.getContent(), directoryPath, dateString, fileCountMap);
			} else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
					|| (part.getFileName() != null && part.getFileName().toLowerCase().endsWith(".pdf"))) {

				int counter = fileCountMap.getOrDefault(dateString, 0) + 1;
				fileCountMap.put(dateString, counter);

				String newFileName = "Fuel_Ticket_" + dateString + "_" + counter + ".pdf";
				File file = new File(directoryPath + newFileName);
				((MimeBodyPart) part).saveFile(file);
				System.out.println("Downloaded: " + newFileName);
				count++;
			}
		}
		return count;
	}

	public static void main(String[] args) {
		String yourEmail = "nagigroup0076@gmail.com"; // your email
		String yourAppPassword = "tayy fiqr rhze yurb";
		downloadFuelTickets(yourEmail, yourAppPassword);
	}
}
