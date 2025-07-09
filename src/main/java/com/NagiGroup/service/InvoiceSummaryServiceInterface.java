package com.NagiGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.NagiGroup.dto.InvoiceSummary.InvoiceSummaryDTO;
import com.NagiGroup.dto.LoadPendingCharges.LoadPendingChargesDTO;
import com.NagiGroup.model.invoiceSummary.UpdatePaymentStatusModel;
import com.NagiGroup.utility.ApiResponse;

public interface InvoiceSummaryServiceInterface {

	ApiResponse<List<InvoiceSummaryDTO>> getAllInvoiceStatus(boolean status);

	ApiResponse<List<InvoiceSummaryDTO>> getListOfPaidInvoices();

	ApiResponse<LoadPendingChargesDTO> getPendingChargesAsPerLoadById(int load_id);

	ApiResponse<Integer> updatePaymentStatus(UpdatePaymentStatusModel updatePaymentStatusModel,
			HttpServletRequest request);

	
}
