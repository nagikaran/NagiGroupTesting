package com.NagiGroup.service;

import com.NagiGroup.model.mail.InvoiceMailModel;

public interface MailService {

	boolean sendInvoiceEmailWithAttachment(InvoiceMailModel invoiceMailModel);
	


}
