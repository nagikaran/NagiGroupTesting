package com.NagiGroup.dto.driverSummary;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DriverMonthlySummaryDTO {
	private String load_number;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
	private LocalDate pickup_date;
	private String origin;
	private String destination;
	private Double rate;
	private String trailerUsed;
	private Double detention;
	private Double lumper;
	private Double scale;
	private Double trailerWash; // ✅ New field added
	private Double balance;
	private Double lumper_total;
	private Double detention_total;
	private Double  scale_total;
	private Double  trailer_wash_total; 
	private Double balance_total;
	@JsonProperty("is_detention")
    private boolean detentionApplicable;
	
//	public String getOrigin() {
//		return origin;
//	}
//	public void setOrigin(String origin) {
//		this.origin = origin;
//	}
//	public String getDestination() {
//		return destination;
//	}
//	public void setDestination(String destination) {
//		this.destination = destination;
//	}
//	public Double getRate() {
//		return rate;
//	}
//	public void setRate(Double rate) {
//		this.rate = rate;
//	}
//	public String getTrailerUsed() {
//		return trailerUsed;
//	}
//	public void setTrailerUsed(String trailerUsed) {
//		this.trailerUsed = trailerUsed;
//	}
//	public Double getDetention() {
//		return detention;
//	}
//	public void setDetention(Double detention) {
//		this.detention = detention;
//	}
//	public Double getLumper() {
//		return lumper;
//	}
//	public void setLumper(Double lumper) {
//		this.lumper = lumper;
//	}
//	public Double getScale() {
//		return scale;
//	}
//	public void setScale(Double scale) {
//		this.scale = scale;
//	}
//	public Double getTrailerWash() {
//		return trailerWash;
//	}
//	public void setTrailerWash(Double trailerWash) {
//		this.trailerWash = trailerWash;
//	}
//	public Double getBalance() {
//		return balance;
//	}
//	public void setBalance(Double balance) {
//		this.balance = balance;
//	}
//	public Double getLumper_total() {
//		return lumper_total;
//	}
//	public void setLumper_total(Double lumper_total) {
//		this.lumper_total = lumper_total;
//	}
//	public Double getDetention_total() {
//		return detention_total;
//	}
//	public void setDetention_total(Double detention_total) {
//		this.detention_total = detention_total;
//	}
//	public Double getScale_total() {
//		return scale_total;
//	}
//	public void setScale_total(Double scale_total) {
//		this.scale_total = scale_total;
//	}
//	public Double getTrailer_wash_total() {
//		return trailer_wash_total;
//	}
//	public void setTrailer_wash_total(Double trailer_wash_total) {
//		this.trailer_wash_total = trailer_wash_total;
//	}
//	public Double getBalance_total() {
//		return balance_total;
//	}
//	public void setBalance_total(Double balance_total) {
//		this.balance_total = balance_total;
//	}
//	public String getLoad_number() {
//		return load_number;
//	}
//	public void setLoad_number(String load_number) {
//		this.load_number = load_number;
//	}
//	public LocalDate getPickup_date() {
//		return pickup_date;
//	}
//	public void setPickup_date(LocalDate pickup_date) {
//		this.pickup_date = pickup_date;
//	}
//	
	
	
}
