package com.NagiGroup.model.mail;

import java.io.File;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MailModel {
	
	private String recipientEmailIds;
	private String alternateCC; 
	private String fromEmailId;
	private String subject;
	private String body;
	private Boolean isHtml;
	private List<File> attachments;
	public String getRecipientEmailIds() {
		return recipientEmailIds;
	}
	public void setRecipientEmailIds(String recipientEmailIds) {
		this.recipientEmailIds = recipientEmailIds;
	}
	public String getAlternateCC() {
		return alternateCC;
	}
	public void setAlternateCC(String alternateCC) {
		this.alternateCC = alternateCC;
	}
	public String getFromEmailId() {
		return fromEmailId;
	}
	public void setFromEmailId(String fromEmailId) {
		this.fromEmailId = fromEmailId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Boolean getIsHtml() {
		return isHtml;
	}
	public void setIsHtml(Boolean isHtml) {
		this.isHtml = isHtml;
	}
	public List<File> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	
}
