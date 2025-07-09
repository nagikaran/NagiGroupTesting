package com.NagiGroup.model.driverSummary;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverSummaryModel {
	
	private int driver_id;
	private String month_year;
	private double dispatch;
	private double eld_fee;
	private double parking;
	private double ifta_paid;
	private double trailer_used;
	private double insurance;
	private double lumper;	
	private double scale;
	private double trailer_used_by_company;
	private double total_before_misc;
	private double total_after_misc;
	private double paid_amount;
	private LocalDate paid_date;
	private String check_number;
	private double return_money_to_company;
	private double borrowed_amount;
}
