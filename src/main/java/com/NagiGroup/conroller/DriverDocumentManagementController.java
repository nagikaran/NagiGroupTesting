package com.NagiGroup.conroller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import com.NagiGroup.config.GoogleDriveService;
import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.model.DriverDocumentManagementBulkInsertModel;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.model.driverDocumement.DriverDocumentDeleteModel;
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
	 @Autowired
	 public DriverDocumentManagementService driverDocumentManagementService;
	 

//	public DriverDocumentManagementController(DriverDocumentManagementService driverDocumentManagementService) {
//		this.driverDocumentManagementService=driverDocumentManagementService;
//	}
	
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
	public ApiResponse<DriverDocumentManagementDto> getDriverDocumentManagementById(@PathVariable int driver_documents_id) {
    	logger.info("DriverDocumentManagementController : /get/id : " + driver_documents_id);
		return driverDocumentManagementService.getDriverDocumentManagementById(driver_documents_id);
	}

    @PostMapping("/download")
	public ResponseEntity<InputStreamResource> downloadPoFile(@RequestBody DriverDocumentDownloadModel documentDownloadModel) {
	
		try {
		//	D:\NAGI_GROUP\YEAR_2025\MARCH\David\DISPATCH RECORD
			String base_url = PropertiesReader.getProperty("constant", "BASEURL_FOR_YEAR");
			String base_path_for_docment = base_url+"_"+documentDownloadModel.getYear()+"/"+documentDownloadModel.getMonth()+"/"+documentDownloadModel.getDriver_name()+"/"+documentDownloadModel.getSub_folder_name()+"/";
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
    
    @PostMapping(value = "/insert/bulk/month_wise" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = insert_driver_document")
	public ApiResponse<Integer>  driverDocumentManagemeBulkInsert(@ModelAttribute DriverDocumentManagementBulkInsertModel documentManagementBulkInsertModel,HttpServletRequest request) {
		
		return driverDocumentManagementService.driverDocumentManagemeBulkInsert(documentManagementBulkInsertModel,request);
	
	}
    
    @PostMapping(value = "/delete_document")	
	@Operation(summary = "function = mark_driver_document_as_deleted")
	public ApiResponse<Integer>  deleteDocument(@RequestBody DriverDocumentDeleteModel documentDeleteModel,HttpServletRequest request) {
		
		return driverDocumentManagementService.deleteDocument(documentDeleteModel,request);
	
	}
    
    @PostMapping("/view-document")
    public ResponseEntity<Resource> viewDocument(@RequestBody DriverDocumentDownloadModel documentDownloadModel) throws IOException {
        String base_url = PropertiesReader.getProperty("constant", "BASEURL_FOR_YEAR");
        String base_path_for_document = base_url + "_" + documentDownloadModel.getYear() + "/"
                + documentDownloadModel.getMonth() + "/"
                + documentDownloadModel.getDriver_name() + "/"
                + documentDownloadModel.getSub_folder_name() + "/";

        String documentName = documentDownloadModel.getDocument_name();
        System.out.println("path: "+base_path_for_document + documentName);
        File file = new File(base_path_for_document + documentName);

        if (!file.exists()) {
            return ResponseEntity.status(404).body(null);
        }

        Path filePath = file.toPath();
        Resource resource = new UrlResource(filePath.toUri());

        // Detect content type
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + documentName + "\"")
                .body(resource);
    }

    @GetMapping("/download/{file_id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("file_id") String fileId) throws GeneralSecurityException {
        if (fileId == null || fileId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            // Get the file content
            ByteArrayOutputStream outputStream = null;
			try {
				outputStream = GoogleDriveService.downloadFile(fileId);
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Get the file name from Google Drive metadata
            String fileName = GoogleDriveService.getFileName(fileId);
            if (fileName == null || fileName.isEmpty()) {
                fileName = "document.pdf"; // fallback name
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // or PDF if always pdf
            headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(fileName)
                .build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
