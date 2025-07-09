package com.NagiGroup.repository;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.InvoiceSummary.InvoiceSummaryDTO;
import com.NagiGroup.dto.LoadPendingCharges.LoadPendingChargesDTO;
import com.NagiGroup.dto.load.LoadDto;
import com.NagiGroup.model.invoiceSummary.UpdatePaymentStatusModel;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.utility.ApiResponse;

@Repository
public class InvoiceSummaryRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(DriverSummaryRepository.class);
	private DbContextService dbContextserviceBms;
	public InvoiceSummaryRepository(DbContextService dbContextserviceBms) {
		this.dbContextserviceBms = dbContextserviceBms;
		
	}

	public ApiResponse<List<InvoiceSummaryDTO>> getAllInvoiceStatus(boolean status) {
		List<InvoiceSummaryDTO> invoiceSummaryDTOs= null;
		try {
			logger.info("InvoiceSummaryRepository : getAllInvoiceStatus Start");
			Object param[] = { status };
			invoiceSummaryDTOs = dbContextserviceBms.QueryToListWithParam(QueryMaster.get_invoices_by_invoiced_status, param, InvoiceSummaryDTO.class);
			logger.info("InvoiceSummaryRepository : getAllInvoiceStatus end");
			return new ApiResponse<List<InvoiceSummaryDTO>>(true, "Total Record " + invoiceSummaryDTOs.size() + " ", true, invoiceSummaryDTOs,
					invoiceSummaryDTOs.size());

		} catch (Exception e) {
			logger.info("InvoiceSummaryRepository : Exception At : getAllInvoiceStatus :", e);
			e.printStackTrace();
			return new ApiResponse<List<InvoiceSummaryDTO>>(false, e.getMessage(), false, null, 0);
		}
	}

	public ApiResponse<List<InvoiceSummaryDTO>> getListOfPaidInvoices() {

		List<InvoiceSummaryDTO> invoiceSummaryDTOs = null;
		try {
			logger.info("InvoiceSummaryRepository : getListOfPaidInvoices Start");
			invoiceSummaryDTOs = dbContextserviceBms.QueryToList(QueryMaster.get_paid_invoices, InvoiceSummaryDTO.class);
			logger.info("InvoiceSummaryRepository : getListOfPaidInvoices Start");
			return new ApiResponse<List<InvoiceSummaryDTO>>(true, "Total Record " + invoiceSummaryDTOs.size() + " ", true,
					invoiceSummaryDTOs, invoiceSummaryDTOs.size());

		} catch (Exception e) {
			logger.info("InvoiceSummaryRepository : Exception At : getListOfPaidInvoices :", e);
			return new ApiResponse<List<InvoiceSummaryDTO>>(false, e.getMessage(), false, null, 0);
		}
	}

	public ApiResponse<LoadPendingChargesDTO> getPendingChargesAsPerLoadById(int load_id) {
		// TODO Auto-generated method stub
		logger.info("LoadRepository : getLoadById Start");
		LoadPendingChargesDTO loadPendingChargesDTO = null;
		try {
			Object params[] = { load_id };
			loadPendingChargesDTO = dbContextserviceBms.QueryToFirstWithParam(QueryMaster.get_pending_charges_by_load_id, params, LoadPendingChargesDTO.class);
			logger.info("InvoiceSummaryRepository : getPendingChargesAsPerLoadById End");
			return new ApiResponse<LoadPendingChargesDTO>(true, "Total Record " + 1 + " ", true, loadPendingChargesDTO, 1);
		} catch (Exception e) {
			logger.info("InvoiceSummaryRepository : Exception At : getPendingChargesAsPerLoadById :", e);
			return new ApiResponse<LoadPendingChargesDTO>(false, "No record found", false, null, 0);
		}
	}

	public ApiResponse<Integer> updatePaymentStatus(UpdatePaymentStatusModel updatePaymentStatusModel,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			System.out.println("UpdatePaymentStatusModel: " + updatePaymentStatusModel);
			logger.info("InvoiceSummaryRepository : updatePaymentStatus start");
			CommonController commonController = new CommonController();
			int updatedBy = commonController.getUserDtoDataFromToken(request);
			System.out.println("updatedBy: " + updatedBy);
			Object param[] = { updatePaymentStatusModel.getLoad_number(),
					           updatePaymentStatusModel.getPayment_recieved_date(),
					           updatePaymentStatusModel.getCheck_number(),
					           updatedBy};
			int id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_invoice_payment_status,
					param);

			

			if (id != 0) {
				logger.info("InvoiceSummaryRepository : updatePaymentStatus end : success");
				return new ApiResponse<Integer>(true,
						"Payment Status updated as Complete" + 1, true, 1, 1);
			} else {
				logger.info("InvoiceSummaryRepository : markLoadInProgress end : fail");
				return new ApiResponse<Integer>(false, "Something went wrong while updating status",
						false, 0, 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error in updatePaymentStatus", e);
			e.printStackTrace();
			return new ApiResponse<Integer>(false, "Error while updating status",
					false, 0, 0);
		}

	}

	

}
