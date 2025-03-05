package com.NagiGroup.repository;

import java.io.File;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.helperClass.DocumentDetails;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.service.DriverDocumentManagementService;
import com.NagiGroup.utility.ApiResponse;
import com.NagiGroup.utility.PropertiesReader;


@Repository
public class DriverDocumentManagementRepository implements DriverDocumentManagementService {
	
	 private static final Logger logger = LoggerFactory.getLogger(DriverDocumentManagementRepository.class);
		
	private DbContextService dbContextserviceBms;
	public DriverDocumentManagementRepository(DbContextService dbContextserviceBms){
		this.dbContextserviceBms=dbContextserviceBms;
	}
	public static String rootFolder = PropertiesReader.getProperty("constant", "BASEURL_FOR_YEAR");
	  private static final List<String> SUBFOLDERS = Arrays.asList(
	            "ANNUAL DOT INSPECTION",
	            "DISPATCH RECORD",
	            "DRIVER + EQUIPMENT INFORMATION",
	            "FUEL RECEIPT", 
	            "IFTA QUARTERLY",
	            "P.O.D + LUMPER RECEIPT + SCALE",
	            "TRUCK AND TRAILER REPAIR",
	            "TRUCK TRAILER SERVICES"
	    );


	public List<SubFolderDto> getSubFolder() {
		List<SubFolderDto> subFolderDtos = null;
		return subFolderDtos = dbContextserviceBms.QueryToList(QueryMaster.get_all_sub_folders, SubFolderDto.class);
		
	}

	@Override
	public ApiResponse<Integer> driverDocumentManagemeInsert(
			DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request) {
		List<DocumentDetails> documentDetailsList = extractDocuments(driverDocumentManagementModel);

		    LocalDate currentDate = LocalDate.now();
		    String month = currentDate.getMonth().toString(); // Example: "FEBRUARY"
		    String yearFolder = rootFolder + "_" + Year.now().getValue(); // Example: "documents_2025"

		    String driverFolder = yearFolder + "/" + month + "/" + driverDocumentManagementModel.getDriver_name();

		    int documentId = 0;

		    for (DocumentDetails doc : documentDetailsList) {
		        try {
		            // Create target folder path
		            String targetFolder = driverFolder + "/" + doc.getSubFolderName();

		            // Create directories if they do not exist
		            File folder = new File(targetFolder);
		            if (!folder.exists()) {
		                folder.mkdirs();
		            }

		            // Save the file to the folder
		            File savedFile = new File(targetFolder + "/" + doc.getOriginalDocumentName());
		            doc.getFile().transferTo(savedFile);

		            // Insert document details into the database
		            Object[] param = {
		                doc.getDocumentName(),
		                doc.getOriginalDocumentName(),
		                targetFolder,
		                doc.getSubFolderId(),
		                driverDocumentManagementModel.getDriver_id()
		            };

		            documentId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document, param);

		        } catch (Exception e) {
		            e.printStackTrace();
		            return new ApiResponse<Integer>(false, "Error while saving document: " + doc.getOriginalDocumentName(), false, 0, documentId);
		        }
		    }

		    return new ApiResponse<Integer>(true, "Documents saved successfully",true, 1,  documentId);
}
	

	private List<DocumentDetails> extractDocuments(DriverDocumentManagementModel model) {
	    List<DocumentDetails> documents = new ArrayList<>();
	    List<SubFolderDto> subFolderList = getsubFolderMaster().Data;

	    // Define all document types that map to sub-folder names
	    Map<String, String> documentToFolderMap = new HashMap<>();
	    documentToFolderMap.put("roc", "DISPATCH RECORD");
	    documentToFolderMap.put("pod", "P.O.D + LUMPER RECEIPT + SCALE");
	    documentToFolderMap.put("fuel_reciept", "FUEL RECEIPT");
	    documentToFolderMap.put("annual_dot_inspection", "ANNUAL DOT INSPECTION");
	    documentToFolderMap.put("truck_and_trailer_repair", "TRUCK AND TRAILER REPAIR");
	    documentToFolderMap.put("ifta_quaterly", "IFTA QUARTERLY");
	    documentToFolderMap.put("truck_trailer_services", "TRUCK TRAILER SERVICES");
	    documentToFolderMap.put("driver_equipment_information", "DRIVER + EQUIPMENT INFORMATION");

	    // Process each document dynamically
	    for (Map.Entry<String, String> entry : documentToFolderMap.entrySet()) {
	        String documentKey = entry.getKey();
	        String folderName = entry.getValue();

	        MultipartFile file = getDocumentFile(model, documentKey);
	        if (file != null && !file.isEmpty()) {
	            SubFolderDto subFolderDetails = getSubFolderDetails(documentKey, subFolderList);
	            if (subFolderDetails != null) {
	                documents.add(new DocumentDetails(
	                    CommonController.getFileNameWithoutExtension(file),
	                    file.getOriginalFilename(),
	                    subFolderDetails.getSub_folder_name(), // Folder name from DB
	                    file,
	                    subFolderDetails.getSub_folder_id()  // Sub-folder ID from DB
	                ));
	            }
	        }
	    }

	    return documents;
	}






	@Override
	public ApiResponse<List<DriverDocumentManagementDto>> getDriverDocuments() {
		
		List<DriverDocumentManagementDto> driverDocumentManagementDtos = null;
		try {
			driverDocumentManagementDtos = dbContextserviceBms.QueryToList(QueryMaster.get_all_driver_documents, DriverDocumentManagementDto.class);
			
			return new ApiResponse<List<DriverDocumentManagementDto>>(true, "Total Record " + driverDocumentManagementDtos.size() + " ", true,
					driverDocumentManagementDtos, driverDocumentManagementDtos.size());
		} catch (Exception e) {
			
			return new ApiResponse<List<DriverDocumentManagementDto>>(false, e.getMessage(), false, null, 0);
		}
	}


	@Override
	public ApiResponse<List<SubFolderDto>> getsubFolderMaster() {
		
		List<SubFolderDto> subFolderDtos = null;
		try {
			subFolderDtos = dbContextserviceBms.QueryToList(QueryMaster.get_all_sub_folders, SubFolderDto.class);
			
			return new ApiResponse<List<SubFolderDto>>(true, "Total Record " + subFolderDtos.size() + " ", true,
					subFolderDtos, subFolderDtos.size());
		} catch (Exception e) {
			
			return new ApiResponse<List<SubFolderDto>>(false, e.getMessage(), false, null, 0);
		}
	}

	
	
	private SubFolderDto getSubFolderDetails(String documentType, List<SubFolderDto> subFolderList) {
	    // Map document types to sub-folder names
	    Map<String, String> documentToFolderMap = new HashMap<>();
	    documentToFolderMap.put("roc", "DISPATCH RECORD");
	    documentToFolderMap.put("pod", "P.O.D + LUMPER RECEIPT + SCALE");
	    documentToFolderMap.put("fuel_reciept", "FUEL RECEIPT");
	    documentToFolderMap.put("annual_dot_inspection", "ANNUAL DOT INSPECTION");
	    documentToFolderMap.put("truck_and_trailer_repair", "TRUCK AND TRAILER REPAIR");
	    documentToFolderMap.put("ifta_quaterly", "IFTA QUARTERLY");
	    documentToFolderMap.put("truck_trailer_services", "TRUCK TRAILER SERVICES");
	    documentToFolderMap.put("driver_equipment_information", "DRIVER + EQUIPMENT INFORMATION");

	    // Get the mapped folder name
	    String folderName = documentToFolderMap.get(documentType);
	    if (folderName == null) {
	        return null; // No mapping found
	    }

	    // Find the sub-folder details from the DB list
	    return subFolderList.stream()
	        .filter(subFolder -> subFolder.getSub_folder_name().equalsIgnoreCase(folderName))
	        .findFirst()
	        .orElse(null);
	}
	private MultipartFile getDocumentFile(DriverDocumentManagementModel model, String documentKey) {
	    switch (documentKey) {
	        case "roc": return model.getRoc();
	        case "pod": return model.getPod();
	        case "fuel_reciept": return model.getFuel_reciept();
	        case "annual_dot_inspection": return model.getAnnual_dot_inspection();
	        case "truck_and_trailer_repair": return model.getTruck_and_trailer_repair();
	        case "ifta_quaterly": return model.getIfta_quaterly();
	        case "truck_trailer_services": return model.getTruck_trailer_serivices();
	        case "driver_equipment_information": return model.getDriver_equipment_information();
	        default: return null;
	    }
	}

	@Override
	public ApiResponse<DriverDocumentManagementDto> getDriverDocumentManagementById(int driver_documents_id) {
		// TODO Auto-generated method stub
		logger.info("DriverDocumentManagementRepository : getDriverDocumentManagementById Start");
		DriverDocumentManagementDto documentManagementDto = null;
		try {
			Object params[] = { driver_documents_id };
			documentManagementDto = dbContextserviceBms.QueryToFirstWithParam(QueryMaster.get_driver_document_by_id, params,
					DriverDocumentManagementDto.class);
			logger.info("DriverDocumentManagementRepository : getDriverDocumentManagementById End");
			return new ApiResponse<DriverDocumentManagementDto>(true, "Total Record " + 1 + " ", true, documentManagementDto, 1);
		} catch (Exception e) {			
			logger.info("DriverDocumentManagementRepository : Exception At : getDriverDocumentManagementById :" , e);
			return new ApiResponse<DriverDocumentManagementDto>(false, "No record found", false, null, 0);
		}
	}

	@Override
	public ApiResponse<Integer> driverDocumentManagemeUpdate(
			DriverDocumentManagementUpdateModel documentManagementUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
			
		return null;
	}
	

}
