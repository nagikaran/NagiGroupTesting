package com.NagiGroup.dto.load;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoadDto {
	
		private int load_id;
		private String load_number;
		private String source;
		private String destination;
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	    private LocalDateTime  shipping_date; // timestamp without time zone,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	    private LocalDateTime  delivery_date; // timestamp without time zone,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	    private LocalDateTime  eta; // timestamp without time zone,
		private int driver_id;
		private String driver_name;
		private double base_price;
		private double final_price;
		private String file_name;
		private Boolean detention;
		private double hours_of_detention;
		private Boolean lumper;
		private Double lumper_price;
		private Double detention_price;
		private Double scale_price;
		private double lumper_paid_by;
		private Integer assigned_driver_id;
		private String company_name;
		private int trailer_used;
		private int status_id;
		private int company_id;
		private Boolean tonu;
		
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	
	
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
	
	public int getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public double getBase_price() {
		return base_price;
	}
	public void setBase_price(double base_price) {
		this.base_price = base_price;
	}
	public double getFinal_price() {
		return final_price;
	}
	public void setFinal_price(double final_price) {
		this.final_price = final_price;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public Boolean getDetention() {
		return detention;
	}
	public void setDetention(Boolean detention) {
		this.detention = detention;
	}
	public double getHours_of_detention() {
		return hours_of_detention;
	}
	public void setHours_of_detention(double hours_of_detention) {
		this.hours_of_detention = hours_of_detention;
	}
	public Boolean getLumper() {
		return lumper;
	}
	public void setLumper(Boolean lumper) {
		this.lumper = lumper;
	}
	
	public double getLumper_paid_by() {
		return lumper_paid_by;
	}
	public void setLumper_paid_by(double lumper_paid_by) {
		this.lumper_paid_by = lumper_paid_by;
	}
	
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public int getTrailer_used() {
		return trailer_used;
	}
	public void setTrailer_used(int trailer_used) {
		this.trailer_used = trailer_used;
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
	public LocalDateTime getEta() {
		return eta;
	}
	public void setEta(LocalDateTime eta) {
		this.eta = eta;
	}
	

	public Integer getAssigned_driver_id() {
		
		return assigned_driver_id==null?0:assigned_driver_id;
	}
	public void setAssigned_driver_id(Integer assigned_driver_id) {
		this.assigned_driver_id = assigned_driver_id;
	}
	public int getStatus_id() {
		return status_id;
	}
	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}
	public Double getLumper_price() {
		return lumper_price==null?0:lumper_price;
	}
	public void setLumper_price(Double lumper_price) {
		this.lumper_price = lumper_price;
	}
	public Double getDetention_price() {
		return detention_price==null?0:detention_price;
	}
	public void setDetention_price(Double detention_price) {
		this.detention_price = detention_price;
	}
	public Double getScale_price() {
		return scale_price==null?0:scale_price;
	}
	public void setScale_price(Double scale_price) {
		this.scale_price = scale_price;
	}
	public Boolean getTonu() {
		return tonu;
	}
	public void setTonu(Boolean tonu) {
		this.tonu = tonu;
	}
	
	
	
}
