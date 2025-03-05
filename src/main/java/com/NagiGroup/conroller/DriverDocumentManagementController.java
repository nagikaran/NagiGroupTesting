package com.NagiGroup.conroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.service.DriverDocumentManagementService;
import com.NagiGroup.utility.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value="/api/driver_document_management")
public class DriverDocumentManagementController {
//	private Logger infoLogger = Logger.getLogger("info");
	 private static final Logger logger = LoggerFactory.getLogger(DriverDocumentManagementController.class);
	
	 public DriverDocumentManagementService driverDocumentManagementService;
	
	public DriverDocumentManagementController(DriverDocumentManagementService driverDocumentManagementService) {
		this.driverDocumentManagementService=driverDocumentManagementService;
	}
	
	@PostMapping(value = "/insert" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = insert_driver_document")
	public ApiResponse<Integer>  driverDocumentManagemeInsert(@ModelAttribute DriverDocumentManagementModel driverDocumentManagementModel,HttpServletRequest request) {
		
		return driverDocumentManagementService.driverDocumentManagemeInsert(driverDocumentManagementModel,request);
	
	}
	
	@PutMapping(value = "/update" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = insert_driver_document")
	public ApiResponse<Integer>  driverDocumentManagemeUpdate(@ModelAttribute DriverDocumentManagementUpdateModel documentManagementUpdateModel,HttpServletRequest request) {
		
		return driverDocumentManagementService.driverDocumentManagemeUpdate(documentManagementUpdateModel,request);
	
	}
	
	
	@GetMapping("/get")
	@Operation(summary = "function = get_all_driver_documents")
	public ApiResponse<List<DriverDocumentManagementDto>> getDriverDocuments() {
		
		return driverDocumentManagementService.getDriverDocuments();
	}
	
	@GetMapping("/get/sub_folder_master")
	@Operation(summary = "function = get_all_sub_folders")
	public ApiResponse<List<SubFolderDto>> getsubFolderMaster() {
		
		return driverDocumentManagementService.getsubFolderMaster();
	}
	
	 
    @GetMapping("/get/id/{driver_documents_id}")
	@Operation(summary = "function = get_driver_document_by_id")
	public ApiResponse<DriverDocumentManagementDto> getOemById(@PathVariable int driver_documents_id) {
    	logger.info("DriverDocumentManagementController : /get/id : " + driver_documents_id);
		return driverDocumentManagementService.getDriverDocumentManagementById(driver_documents_id);
	}


}
