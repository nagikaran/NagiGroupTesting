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
    private LocalDateTime  pick_up_date; // timestamp without time zone,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  delievery_date; // timestamp without time zone,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  earliest_time_arrival; // timestamp without time zone,
	private int driver_id;
	private String driver_name;
	private double base_price;
	private double final_price;
	private String file_name;
	private Boolean detention;
	private double hours_of_detention;
	private Boolean lumper;
	private double lumper_amount;
	private double lumper_paid_by;
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
	public LocalDateTime getPick_up_date() {
		return pick_up_date;
	}
	public void setPick_up_date(LocalDateTime pick_up_date) {
		this.pick_up_date = pick_up_date;
	}
	public LocalDateTime getDelievery_date() {
		return delievery_date;
	}
	public void setDelievery_date(LocalDateTime delievery_date) {
		this.delievery_date = delievery_date;
	}
	public LocalDateTime getEarliest_time_arrival() {
		return earliest_time_arrival;
	}
	public void setEarliest_time_arrival(LocalDateTime earliest_time_arrival) {
		this.earliest_time_arrival = earliest_time_arrival;
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
	public double getLumper_amount() {
		return lumper_amount;
	}
	public void setLumper_amount(double lumper_amount) {
		this.lumper_amount = lumper_amount;
	}
	public double getLumper_paid_by() {
		return lumper_paid_by;
	}
	public void setLumper_paid_by(double lumper_paid_by) {
		this.lumper_paid_by = lumper_paid_by;
	}
	
	
	
	

}
