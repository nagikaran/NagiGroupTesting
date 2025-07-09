package com.NagiGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.model.DriverDocumentManagementBulkInsertModel;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.model.driverDocumement.DriverDocumentDeleteModel;
import com.NagiGroup.utility.ApiResponse;


public interface DriverDocumentManagementService {

	ApiResponse<Integer> driverDocumentManagemeInsert(DriverDocumentManagementModel driverDocumentManagementModel,
			HttpServletRequest request);

	ApiResponse<List<DriverDocumentManagementDto>> getDriverDocuments();

	ApiResponse<List<SubFolderDto>> getsubFolderMaster();

	ApiResponse<Integer> driverDocumentManagemeUpdate(DriverDocumentManagementUpdateModel documentManagementUpdateModel,
			HttpServletRequest request);

	ApiResponse<DriverDocumentManagementDto> getDriverDocumentManagementById(int driver_documents_id);

	ApiResponse<Integer> driverDocumentManagemeInsertForSpecifedMonth(
			DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request);

	ApiResponse<Integer> driverDocumentManagemeBulkInsert(
			DriverDocumentManagementBulkInsertModel documentManagementBulkInsertModel, HttpServletRequest request);

	ApiResponse<Integer> deleteDocument(DriverDocumentDeleteModel documentDeleteModel, HttpServletRequest request);

}
