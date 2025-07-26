package com.NagiGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.NagiGroup.dto.companyDetails.CompanyNameDto;
import com.NagiGroup.dto.load.LoadAssignmentDocumentDto;
import com.NagiGroup.dto.load.LoadDto;
import com.NagiGroup.dto.load.LoadStatusSummaryDto;
import com.NagiGroup.model.load.CancelLoadModel;
import com.NagiGroup.model.load.LoadAdditionalCharges;
import com.NagiGroup.model.load.LoadCompletionModel;
import com.NagiGroup.model.load.LoadModel;
import com.NagiGroup.model.load.LoadStatusModel;
import com.NagiGroup.model.load.LoadUpdateModel;
import com.NagiGroup.utility.ApiResponse;


public interface LoadService {

	ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request);

	ApiResponse<Integer> loadUpdate(LoadUpdateModel loadUpdateModel, HttpServletRequest request);

	ApiResponse<List<LoadDto>> geAlltLoads();

	ApiResponse<LoadDto> getLoadById(int load_id);

	ApiResponse<Integer> assignLoad(LoadStatusModel loadStatusModel, HttpServletRequest request);

	ApiResponse<Integer> updateLoad(LoadUpdateModel loadUpdateModel, HttpServletRequest request);

	ApiResponse<Integer> markLoadInProgress(LoadStatusModel loadStatusModel, HttpServletRequest request);

	ApiResponse<Integer> markLoadComplete(LoadCompletionModel loadCompletionModel, HttpServletRequest request);

	ApiResponse<Integer> saveDocument(LoadCompletionModel loadCompletionModel, HttpServletRequest request);

	ApiResponse<List<LoadDto>> getLoadByStatusId(int status_id);

	ApiResponse<LoadStatusSummaryDto> getLoadCountAsPerTheStatus();

	ApiResponse<List<CompanyNameDto>> geAllCompanyName();

	ApiResponse<LoadAssignmentDocumentDto> getLoadDetailsForAssignment(int load_id, String load_number);

	ApiResponse<Integer> requestToInvoice(LoadAdditionalCharges loadAdditionalCharges, HttpServletRequest request);

	ApiResponse<Integer> cancelLoad(CancelLoadModel cancelLoadModel, HttpServletRequest request);

	ApiResponse<Integer> requestToInvoiceForTonu(CancelLoadModel cancelLoadModel, HttpServletRequest request);

	ApiResponse<Integer> requestToManageNoLogDoc(LoadCompletionModel loadCompletionModel, HttpServletRequest request);


}
