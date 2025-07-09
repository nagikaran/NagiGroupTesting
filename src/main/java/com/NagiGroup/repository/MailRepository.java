package com.NagiGroup.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.NagiGroup.config.mail.SendMailUtility;
import com.NagiGroup.model.load.CancelLoadModel;
import com.NagiGroup.model.mail.InvoiceMailModel;
import com.NagiGroup.model.mail.MailModel;
import com.NagiGroup.service.MailService;
@Repository
public class MailRepository implements MailService {
	
	private SendMailUtility sendMailUtility;
	 private static final Logger infoLogger = LoggerFactory.getLogger(MailRepository.class);
	    private static final Logger errorLogger = LoggerFactory.getLogger(MailRepository.class);

	public MailRepository(SendMailUtility sendMailUtility) {
		this.sendMailUtility = sendMailUtility;
	}

	@Override
	public boolean sendInvoiceEmailWithAttachment(InvoiceMailModel invoiceMailModel) {
		boolean sendMail = false;
		try {
			infoLogger.info("MailRepository : sendInvoiceEmailWithAttachment start : " + invoiceMailModel);
			
			MailModel mailModel = new MailModel();
			String subject = "";
			infoLogger.info("MailRepository : Recipient mail id : " + invoiceMailModel.getCompany_mail_id());
			
			mailModel.setRecipientEmailIds(invoiceMailModel.getCompany_mail_id());
			mailModel.setAlternateCC("");
			if(invoiceMailModel.isTonu())
			   subject = "LOAD # "+invoiceMailModel.getLoad_number()+" INVOICE FOR THE TONU";
			else
				subject = "LOAD # "+invoiceMailModel.getLoad_number();
			
			
			mailModel.setSubject(subject);
			mailModel.setAttachments(invoiceMailModel.getAttachments());
			mailModel.setBody("Please find the attached invoice.");
			mailModel.setIsHtml(true);
			sendMail = SendMailUtility.sendMail(mailModel);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			errorLogger.error("MailRepository : Error At : sendInvoiceEmailWithAttachment : " + ex.getMessage());
			
		}
		
		
			infoLogger.info("MailRepository : sendInvoiceEmailWithAttachment end");
			return sendMail;
		
		
	}

}
