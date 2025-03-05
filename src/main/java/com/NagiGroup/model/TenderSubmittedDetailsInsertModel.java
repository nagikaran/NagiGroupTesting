package com.NagiGroup.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TenderSubmittedDetailsInsertModel {
	
	 private Short response_type_id;
	    private String document_name;
	    private String documnet_path;
	    private String remarks;
	    private int index_start_no;
	    private Boolean include_index;
	    private List<TenderInfo> json_list; // TenderInfo class is now imported
	    private String state_name;
	    private String city_name;
	    private String submission_date;
	    private String abbreviation_name;
	    private Long tender_id;
		public Short getResponse_type_id() {
			return response_type_id;
		}
		public void setResponse_type_id(Short response_type_id) {
			this.response_type_id = response_type_id;
		}
		public String getDocument_name() {
			return document_name;
		}
		public void setDocument_name(String document_name) {
			this.document_name = document_name;
		}
		public String getDocumnet_path() {
			return documnet_path;
		}
		public void setDocumnet_path(String documnet_path) {
			this.documnet_path = documnet_path;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		public int getIndex_start_no() {
			return index_start_no;
		}
		public void setIndex_start_no(int index_start_no) {
			this.index_start_no = index_start_no;
		}
		public Boolean getInclude_index() {
			return include_index;
		}
		public void setInclude_index(Boolean include_index) {
			this.include_index = include_index;
		}
		public List<TenderInfo> getJson_list() {
			return json_list;
		}
		public void setJson_list(List<TenderInfo> json_list) {
			this.json_list = json_list;
		}
		public String getState_name() {
			return state_name;
		}
		public void setState_name(String state_name) {
			this.state_name = state_name;
		}
		public String getCity_name() {
			return city_name;
		}
		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}
		public String getSubmission_date() {
			return submission_date;
		}
		public void setSubmission_date(String submission_date) {
			this.submission_date = submission_date;
		}
		public String getAbbreviation_name() {
			return abbreviation_name;
		}
		public void setAbbreviation_name(String abbreviation_name) {
			this.abbreviation_name = abbreviation_name;
		}
		public Long getTender_id() {
			return tender_id;
		}
		public void setTender_id(Long tender_id) {
			this.tender_id = tender_id;
		}
	    
	    
	    

}
