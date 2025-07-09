package com.NagiGroup.dto.InvoiceSummary;

import java.time.LocalDate;

import com.NagiGroup.dto.driverSummary.DriverMonthlySummaryDTO;
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
public class InvoiceSummaryDTO {
	private int load_id;
	private int company_id ;
	private int assigned_driver_id;
    private Long invoiceNumber;
    private String companyName;
    private String loadNumber;
    private Boolean isInvoiced;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    private LocalDate invoiceDate;
    private String driverName;
    private String paymentStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    private LocalDate paymentReceivedDate;
    private String check_number;

  
}

