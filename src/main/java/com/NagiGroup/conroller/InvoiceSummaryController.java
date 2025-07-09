package com.NagiGroup.conroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.dto.InvoiceSummary.InvoiceSummaryDTO;
import com.NagiGroup.dto.LoadPendingCharges.LoadPendingChargesDTO;
import com.NagiGroup.model.invoiceSummary.UpdatePaymentStatusModel;
import com.NagiGroup.service.InvoiceSummaryServiceInterface;
import com.NagiGroup.utility.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value="/api/invoice_summary")
public class InvoiceSummaryController {
	
	private static final Logger logger = LoggerFactory.getLogger(DriverSummaryController.class);
	 
	 @Autowired
	 public InvoiceSummaryServiceInterface invoiceSummaryServiceInterface;
	
	
	@GetMapping("/by-invoiced-status/{status}")
	@Operation(summary = "function = get_invoices_by_invoiced_status")
	public ApiResponse<List<InvoiceSummaryDTO>> getAllInvoiceStatus(
	        @PathVariable("status") boolean status) {
		return invoiceSummaryServiceInterface.getAllInvoiceStatus(status);
	}
	
	@GetMapping("/get_paid_invoices")
	@Operation(summary = "function = get_paid_invoices")
	public ApiResponse<List<InvoiceSummaryDTO>> getListOfPaidInvoices() {
		
		return invoiceSummaryServiceInterface.getListOfPaidInvoices();
	}
	
	@GetMapping("/get_pending_charges/{load_id}")
	@Operation(summary = "function = get_pending_charges_by_load_id")
	public ApiResponse<LoadPendingChargesDTO> getPendingChargesAsPerLoadById(@PathVariable int load_id) {
		return invoiceSummaryServiceInterface.getPendingChargesAsPerLoadById(load_id);
	}
	
	@PutMapping(value = "/update_payment_status")	
	@Operation(summary = "function = update_load")
	public ApiResponse<Integer>  updatePaymentStatus(@RequestBody UpdatePaymentStatusModel updatePaymentStatusModel,HttpServletRequest request) {
		return invoiceSummaryServiceInterface.updatePaymentStatus(updatePaymentStatusModel,request);		 
	
	}
	

}
