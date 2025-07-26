package com.NagiGroup.serviceimpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

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
import com.NagiGroup.repository.LoadRepository;
import com.NagiGroup.service.LoadService;
import com.NagiGroup.utility.ApiResponse;

@Service
public class LoadServiceIMP implements LoadService {
	
	private LoadRepository loadRepository;
	
	public LoadServiceIMP(LoadRepository loadRepository) {
		this.loadRepository=loadRepository;
	}

	@Override
	public ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.loadInsert(loadModel, request);
	}

	@Override
	public ApiResponse<Integer> loadUpdate(LoadUpdateModel loadUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.loadUpdate(loadUpdateModel, request);
	}

	@Override
	public ApiResponse<List<LoadDto>> geAlltLoads() {
		// TODO Auto-generated method stub
		return loadRepository.geAlltLoads();
	}

	@Override
	public ApiResponse<LoadDto> getLoadById(int load_id) {
		// TODO Auto-generated method stub
		return loadRepository.getLoadById(load_id);
	}

	@Override
	public ApiResponse<Integer> assignLoad(LoadStatusModel loadStatusModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.assignLoad(loadStatusModel, request);
	}

	@Override
	public ApiResponse<Integer> updateLoad(LoadUpdateModel loadUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.updateLoad(loadUpdateModel, request);
	}

	@Override
	public ApiResponse<Integer> markLoadInProgress(LoadStatusModel loadStatusModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.markLoadInProgress(loadStatusModel, request);
	}

	@Override
	public ApiResponse<Integer> markLoadComplete(LoadCompletionModel loadCompletionModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		return loadRepository.markLoadComplete(loadCompletionModel, request);
	}

	@Override
	public ApiResponse<Integer> saveDocument(LoadCompletionModel loadCompletionModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.saveDocument(loadCompletionModel, request);
	}

	@Override
	public ApiResponse<List<LoadDto>> getLoadByStatusId(int status_id) {
		// TODO Auto-generated method stub
		return loadRepository.getLoadByStatusId( status_id) ;
	}

	@Override
	public ApiResponse<LoadStatusSummaryDto> getLoadCountAsPerTheStatus() {
		// TODO Auto-generated method stub
		return loadRepository.getLoadCountAsPerTheStatus();
	}

	@Override
	public ApiResponse<List<CompanyNameDto>> geAllCompanyName() {
		// TODO Auto-generated method stub
		return  loadRepository.getAllCompanyName();
	}

	@Override
	public ApiResponse<LoadAssignmentDocumentDto> getLoadDetailsForAssignment(int load_id, String load_number) {
		// TODO Auto-generated method stub
		return loadRepository.getLoadDetailsForAssignment(load_id,load_number);
	
	}

	@Override
	public ApiResponse<Integer> requestToInvoice(LoadAdditionalCharges loadAdditionalCharges, HttpServletRequest request) {
		        // TODO Auto-generated method stub
				return loadRepository.requestToInvoice(loadAdditionalCharges,request);			
	}

	@Override
	public ApiResponse<Integer> cancelLoad(CancelLoadModel cancelLoadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.cancelLoad(cancelLoadModel,request);
	}

	@Override
	public ApiResponse<Integer> requestToInvoiceForTonu(CancelLoadModel cancelLoadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.requestToInvoiceForTonu(cancelLoadModel,request);
	}

	@Override
	public ApiResponse<Integer> requestToManageNoLogDoc(LoadCompletionModel loadCompletionModel,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return loadRepository.managingNoLogDocument(loadCompletionModel,request);
	}

}
