package com.NagiGroup.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.helperClass.DocumentDetails;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.repository.DriverDocumentManagementRepository;

public class DocumentUtils {
	
	 private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	 
	 private static DriverDocumentManagementRepository driverDocumentManagementRepository;
	 
	 public DocumentUtils(DriverDocumentManagementRepository driverDocumentManagementRepository) {
		 this.driverDocumentManagementRepository=driverDocumentManagementRepository;
	 }
		
	 public static String getFileNameWithoutExtension(MultipartFile file) {
	        if (file == null || file.getOriginalFilename() == null) {
	            return null;
	        }
	        
	        String originalFileName = file.getOriginalFilename();
	        int lastDotIndex = originalFileName.lastIndexOf(".");
	        
	        if (lastDotIndex == -1) {
	            return originalFileName; // No extension found, return as is
	        }
	        
	        return originalFileName.substring(0, lastDotIndex);
	    }
	
//	public static List<DocumentDetails> extractDocuments(DriverDocumentManagementModel model) {
//	    List<DocumentDetails> documents = new ArrayList<>();
//	    List<SubFolderDto> subFolderList = driverDocumentManagementRepository.getsubFolderMaster().Data;
//
//	    // Define all document types that map to sub-folder names
//	    Map<String, String> documentToFolderMap = new HashMap<>();
//	    documentToFolderMap.put("roc", "DISPATCH RECORD");
//	    documentToFolderMap.put("pod", "P.O.D + LUMPER RECEIPT + SCALE");
//	    documentToFolderMap.put("fuel_reciept", "FUEL RECEIPT");
//	    documentToFolderMap.put("annual_dot_inspection", "ANNUAL DOT INSPECTION");
//	    documentToFolderMap.put("truck_and_trailer_repair", "TRUCK AND TRAILER REPAIR");
//	    documentToFolderMap.put("ifta_quaterly", "IFTA QUARTERLY");
//	    documentToFolderMap.put("truck_trailer_services", "TRUCK TRAILER SERVICES");
//	    documentToFolderMap.put("driver_equipment_information", "DRIVER + EQUIPMENT INFORMATION");
//
//	    // Process each document dynamically
//	    for (Map.Entry<String, String> entry : documentToFolderMap.entrySet()) {
//	        String documentKey = entry.getKey();
//	        String folderName = entry.getValue();
//
//	        MultipartFile file = driverDocumentManagementRepository.getDocumentFile(model, documentKey);
//	        if (file != null && !file.isEmpty()) {
//	            SubFolderDto subFolderDetails = driverDocumentManagementRepository.getSubFolderDetails(documentKey, subFolderList);
//	            if (subFolderDetails != null) {
//	                documents.add(new DocumentDetails(
//	                    CommonController.getFileNameWithoutExtension(file),
//	                    file.getOriginalFilename(),
//	                    subFolderDetails.getSub_folder_name(), // Folder name from DB
//	                    file,
//	                    subFolderDetails.getSub_folder_id()  // Sub-folder ID from DB
//	                ));
//	            }
//	        }
//	    }
//
//	    return documents;
//	}


}
