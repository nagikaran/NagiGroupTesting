package com.NagiGroup.dto.load;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MyLoadDTO {
	
    private int load_id;
    private String load_number;
    private String source;
    private String destination;
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    private LocalDateTime shipping_date;
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    private LocalDateTime delivery_date;
    private double base_price;
    private Double final_price;
    private int status_id;
    private String driver_name;
    private String status_name;
    private int company_id;
    private int assigned_driver_id;
    private String load_type;
	public int getLoad_id() {
		return load_id;
	}
	public void setLoad_id(int load_id) {
		this.load_id = load_id;
	}
	public String getLoad_number() {
		return load_number;
	}
	public void setLoad_number(String load_number) {
		this.load_number = load_number;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public LocalDateTime getShipping_date() {
		return shipping_date;
	}
	public void setShipping_date(LocalDateTime shipping_date) {
		this.shipping_date = shipping_date;
	}
	public LocalDateTime getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(LocalDateTime delivery_date) {
		this.delivery_date = delivery_date;
	}
	public double getBase_price() {
		return base_price;
	}
	public void setBase_price(double base_price) {
		this.base_price = base_price;
	}
	public Double getFinal_price() {
		return final_price;
	}
	public void setFinal_price(Double final_price) {
		this.final_price = final_price;
	}
	public int getStatus_id() {
		return status_id;
	}
	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getStatus_name() {
		return status_name;
	}
	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public int getAssigned_driver_id() {
		return assigned_driver_id;
	}
	public void setAssigned_driver_id(int assigned_driver_id) {
		this.assigned_driver_id = assigned_driver_id;
	}
	public String getLoad_type() {
		return load_type;
	}
	public void setLoad_type(String load_type) {
		this.load_type = load_type;
	}
    
    
    
        
}