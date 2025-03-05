package com.NagiGroup.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TenderInfo {
	 public Integer tender_submitted_check_list_id;
	    public Long tender_id;
	    public Integer brief_case_id;
	    public String document_name;
	    public String document_path;
	    public Short order_by;
	    public String user_ip_address;
	    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	    public LocalDateTime created_date_time;
	    public Integer created_by;
	    public String created_by_name;
	    public Integer folder_id;
	    public String folder_name;
	    public Integer tender_submitted_id;
	    public String download_document_path;
		public Integer getTender_submitted_check_list_id() {
			return tender_submitted_check_list_id;
		}
		public void setTender_submitted_check_list_id(Integer tender_submitted_check_list_id) {
			this.tender_submitted_check_list_id = tender_submitted_check_list_id;
		}
		public Long getTender_id() {
			return tender_id;
		}
		public void setTender_id(Long tender_id) {
			this.tender_id = tender_id;
		}
		public Integer getBrief_case_id() {
			return brief_case_id;
		}
		public void setBrief_case_id(Integer brief_case_id) {
			this.brief_case_id = brief_case_id;
		}
		public String getDocument_name() {
			return document_name;
		}
		public void setDocument_name(String document_name) {
			this.document_name = document_name;
		}
		public String getDocument_path() {
			return document_path;
		}
		public void setDocument_path(String document_path) {
			this.document_path = document_path;
		}
		public Short getOrder_by() {
			return order_by;
		}
		public void setOrder_by(Short order_by) {
			this.order_by = order_by;
		}
		public String getUser_ip_address() {
			return user_ip_address;
		}
		public void setUser_ip_address(String user_ip_address) {
			this.user_ip_address = user_ip_address;
		}
		public LocalDateTime getCreated_date_time() {
			return created_date_time;
		}
		public void setCreated_date_time(LocalDateTime created_date_time) {
			this.created_date_time = created_date_time;
		}
		public Integer getCreated_by() {
			return created_by;
		}
		public void setCreated_by(Integer created_by) {
			this.created_by = created_by;
		}
		public String getCreated_by_name() {
			return created_by_name;
		}
		public void setCreated_by_name(String created_by_name) {
			this.created_by_name = created_by_name;
		}
		public Integer getFolder_id() {
			return folder_id;
		}
		public void setFolder_id(Integer folder_id) {
			this.folder_id = folder_id;
		}
		public String getFolder_name() {
			return folder_name;
		}
		public void setFolder_name(String folder_name) {
			this.folder_name = folder_name;
		}
		public Integer getTender_submitted_id() {
			return tender_submitted_id;
		}
		public void setTender_submitted_id(Integer tender_submitted_id) {
			this.tender_submitted_id = tender_submitted_id;
		}
		public String getDownload_document_path() {
			return download_document_path;
		}
		public void setDownload_document_path(String download_document_path) {
			this.download_document_path = download_document_path;
		}
	    
	    
	    

}
