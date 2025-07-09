package com.NagiGroup.serviceimpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.NagiGroup.dto.InvoiceSummary.InvoiceSummaryDTO;
import com.NagiGroup.dto.LoadPendingCharges.LoadPendingChargesDTO;
import com.NagiGroup.model.invoiceSummary.UpdatePaymentStatusModel;
import com.NagiGroup.repository.InvoiceSummaryRepository;
import com.NagiGroup.service.InvoiceSummaryServiceInterface;
import com.NagiGroup.utility.ApiResponse;
@Service
public class InvoiceSummaryServiceIMP implements InvoiceSummaryServiceInterface {

	private InvoiceSummaryRepository invoiceSummaryRepository;
	
	public InvoiceSummaryServiceIMP(InvoiceSummaryRepository invoiceSummaryRepository) {
		this.invoiceSummaryRepository=invoiceSummaryRepository;
	}

	@Override
	public ApiResponse<List<InvoiceSummaryDTO>> getAllInvoiceStatus(boolean status) {
		// TODO Auto-generated method stub
		return invoiceSummaryRepository.getAllInvoiceStatus(status);
	}

	@Override
	public ApiResponse<List<InvoiceSummaryDTO>> getListOfPaidInvoices() {
		return invoiceSummaryRepository.getListOfPaidInvoices();
	}

	@Override
	public ApiResponse<LoadPendingChargesDTO> getPendingChargesAsPerLoadById(int load_id) {
		// TODO Auto-generated method stub
		return invoiceSummaryRepository.getPendingChargesAsPerLoadById(load_id);
	}

	@Override
	public ApiResponse<Integer> updatePaymentStatus(UpdatePaymentStatusModel updatePaymentStatusModel,
			HttpServletRequest request) {
		// TODO Auto-generated method stu
		return invoiceSummaryRepository.updatePaymentStatus(updatePaymentStatusModel,request);
	}

	
}
