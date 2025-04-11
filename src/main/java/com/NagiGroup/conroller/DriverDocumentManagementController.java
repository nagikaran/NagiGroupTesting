package com.NagiGroup.conroller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.model.driverDocumement.DriverDocumentDownloadModel;
import com.NagiGroup.service.DriverDocumentManagementService;
import com.NagiGroup.utility.ApiResponse;
import com.NagiGroup.utility.PropertiesReader;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value="/api/driver_document_management")
public class DriverDocumentManagementController {
//	private Logger infoLogger = Logger.getLogger("info");
	 private static final Logger logger = LoggerFactory.getLogger(DriverDocumentManagementController.class);
	
	 public DriverDocumentManagementService driverDocumentManagementService;
	@Autowired
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

    @PostMapping("/download")
	public ResponseEntity<InputStreamResource> downloadPoFile(@RequestBody DriverDocumentDownloadModel documentDownloadModel) {
	
		try {
		//	D:\NAGI_GROUP\YEAR_2025\MARCH\David\DISPATCH RECORD
			String base_url = PropertiesReader.getProperty("constant", "BASEURL_FOR_YEAR");
			String base_path_for_docment = base_url+"_"+documentDownloadModel.getYear()+"/"+documentDownloadModel.getMonth().toUpperCase()+"/"+documentDownloadModel.getDriver_name()+"/"+documentDownloadModel.getSub_folder_name()+"/";
			System.out.println("base_path_for_docment: "+base_path_for_docment);
			File file = new File(base_path_for_docment + documentDownloadModel.getDocument_name());
			if (!file.exists()) {
				return ResponseEntity.status(404) // Not Found
						.body(null);
			}
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} catch (FileNotFoundException e) {
			//errorLogger.error("OEMController : Error At : downloadPoFile " + e.toString());
			return ResponseEntity.status(404).body(null);
		}
	}
    
    @PostMapping(value = "/insert/month_wise" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = insert_driver_document")
	public ApiResponse<Integer>  driverDocumentManagemeInsertForSpecifedMonth(@ModelAttribute DriverDocumentManagementModel driverDocumentManagementModel,HttpServletRequest request) {
		
		return driverDocumentManagementService.driverDocumentManagemeInsertForSpecifedMonth(driverDocumentManagementModel,request);
	
	}

}
