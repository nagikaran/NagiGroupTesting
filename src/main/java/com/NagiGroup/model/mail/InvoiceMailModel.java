package com.NagiGroup.model.mail;

import java.io.File;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceMailModel {
	
	private String load_number;
	
	private String attachment_path;
	
	private String company_mail_id;
	
	private List<File> attachments;
	
	private boolean tonu;
	
	

	public String getLoad_number() {
		return load_number;
	}

	public void setLoad_number(String load_number) {
		this.load_number = load_number;
	}

	public String getAttachment_path() {
		return attachment_path;
	}

	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}

	public String getCompany_mail_id() {
		return company_mail_id;
	}

	public void setCompany_mail_id(String company_mail_id) {
		this.company_mail_id = company_mail_id;
	}

	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public boolean isTonu() {
		return tonu;
	}

	public void setTonu(boolean tonu) {
		this.tonu = tonu;
	}
	
	
	
	
	

}
