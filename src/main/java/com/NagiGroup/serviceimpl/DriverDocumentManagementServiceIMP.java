package com.NagiGroup.serviceimpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.model.DriverDocumentManagementBulkInsertModel;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.model.driverDocumement.DriverDocumentDeleteModel;
import com.NagiGroup.repository.DriverDocumentManagementRepository;
import com.NagiGroup.service.DriverDocumentManagementService;
import com.NagiGroup.utility.ApiResponse;

@Service
public class DriverDocumentManagementServiceIMP implements  DriverDocumentManagementService {

	private DriverDocumentManagementRepository driverDocumentManagementRepository;
	public DriverDocumentManagementServiceIMP(DriverDocumentManagementRepository driverDocumentManagementRepository) {
		this.driverDocumentManagementRepository=driverDocumentManagementRepository;
	}
	@Override
	public ApiResponse<Integer> driverDocumentManagemeInsert(
			DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return driverDocumentManagementRepository.driverDocumentManagemeInsert(driverDocumentManagementModel, request);
		
	}

	@Override
	public ApiResponse<List<DriverDocumentManagementDto>> getDriverDocuments() {
		// TODO Auto-generated method stub
		return driverDocumentManagementRepository.getDriverDocuments();
	}

	@Override
	public ApiResponse<List<SubFolderDto>> getsubFolderMaster() {
		// TODO Auto-generated method stub
		return driverDocumentManagementRepository.getsubFolderMaster();
	}

	@Override
	public ApiResponse<Integer> driverDocumentManagemeUpdate(
			DriverDocumentManagementUpdateModel documentManagementUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return driverDocumentManagementRepository.driverDocumentManagemeUpdate(documentManagementUpdateModel, request);
	}

	@Override
	public ApiResponse<DriverDocumentManagementDto> getDriverDocumentManagementById(int driver_documents_id) {
		// TODO Auto-generated method stub
		return driverDocumentManagementRepository.getDriverDocumentManagementById(driver_documents_id);
	}
	@Override
	public ApiResponse<Integer> driverDocumentManagemeInsertForSpecifedMonth(
			DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return driverDocumentManagementRepository.driverDocumentManagemeInsertForSpecifedMonth(driverDocumentManagementModel, request);
		
	}
	@Override
	public ApiResponse<Integer> driverDocumentManagemeBulkInsert(
			DriverDocumentManagementBulkInsertModel documentManagementBulkInsertModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return driverDocumentManagementRepository.driverDocumentManagemeBulkInsert(documentManagementBulkInsertModel, request);
		
	}
	@Override
	public ApiResponse<Integer> deleteDocument(DriverDocumentDeleteModel documentDeleteModel,
			HttpServletRequest request) {
		// TODO Auto-generated method stu
		return driverDocumentManagementRepository.deleteDocument(documentDeleteModel,request);
	}

}
