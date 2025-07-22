package com.NagiGroup.dto.driverDocument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverDocumentDto {
	
	private int driver_documents_id;
	private int year;
	private String month;
	
	public int getDriver_documents_id() {
		return driver_documents_id;
	}
	public void setDriver_documents_id(int driver_documents_id) {
		this.driver_documents_id = driver_documents_id;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	

}
