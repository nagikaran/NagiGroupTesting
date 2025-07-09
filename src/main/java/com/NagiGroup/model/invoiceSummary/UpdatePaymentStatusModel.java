package com.NagiGroup.model.invoiceSummary;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdatePaymentStatusModel {
	
	private String load_number;
	private LocalDate payment_recieved_date;
	private String check_number;

}
