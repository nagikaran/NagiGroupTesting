package com.NagiGroup.repository;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.config.GoogleDriveService;
import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.helperClass.DocumentDetails;
import com.NagiGroup.model.DriverDocumentManagementBulkInsertModel;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.model.driverDocumement.DriverDocumentDeleteModel;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.utility.ApiResponse;
import com.NagiGroup.utility.PropertiesReader;


@Repository
public class DriverDocumentManagementRepository   {
	
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

//	@Override
//	public ApiResponse<Integer> driverDocumentManagemeInsert(
//			DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request) {
//		      try {
//		    	  List<DocumentDetails> documentDetailsList = extractDocuments(driverDocumentManagementModel);
//		           System.out.println("documentDetailsList: "+documentDetailsList);
//				    LocalDate currentDate = LocalDate.now();
//				    //String month = currentDate.getMonth().toString(); // Example: "FEBRUARY"
//				    String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
//				    String yearFolder = rootFolder + "_" + Year.now().getValue(); // Example: "documents_2025"
//
//				    String driverFolder = yearFolder + "/" + month + "/" + driverDocumentManagementModel.getDriver_name();
//
//				       String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";  // Your Google Drive root folder ID
//				         String yearFolderId = GoogleDriveService.getOrCreateFolder("year"+"_"+String.valueOf(Year.now().getValue()), googleDriveRootFolderId);
//				         String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
//				         String driverFolderId = GoogleDriveService.getOrCreateFolder(driverDocumentManagementModel.getDriver_name().trim().toLowerCase(), monthFolderId);
//				        
//				       
//		              
//				    int documentId = 0;
//
//				    for (DocumentDetails doc : documentDetailsList) {
//				    	System.out.println("doc new filer name : "+doc.getNewFileName());
//				     
//				            // Create target folder path
//				            String targetFolder = driverFolder + "/" + doc.getSubFolderName();
//
//				            // Create directories if they do not exist
//				            File folder = new File(targetFolder);
//				            if (!folder.exists()) {
//				                folder.mkdirs();
//				            }
//				            
//				            // Save the file to the folder
//				            File savedFile = new File(targetFolder + "/" + doc.getNewFileName());
//				            doc.getFile().transferTo(savedFile);
//				           
//				            String subFolderId = GoogleDriveService.getOrCreateFolder(doc.getSubFolderName().trim().toLowerCase(), driverFolderId);     
//				            MultipartFile convertFileToMultipartFile = CommonController.convertFileToMultipartFile(savedFile);
//				            
//				            GoogleDriveService.uploadFileToDrive(convertFileToMultipartFile, subFolderId);
//							
//				            // Insert document details into the database
//				            Object[] param = {
//				                doc.getDocumentName(),
//				                doc.getNewFileName(),
//				                targetFolder,
//				                doc.getSubFolderId(),
//				                driverDocumentManagementModel.getDriver_id()
//				            };
//
//				            documentId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document, param);
//
//				        } 
//
//
//				    return new ApiResponse<Integer>(true, "Documents saved successfully",true, 1,  documentId);
//
//				    
//			} 
//		      
//		      catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//	            return new ApiResponse<Integer>(false, "Error while saving document: " , false, 0, 0);
//	        
//			}
//		
//		
//
//	}
	
	public ApiResponse<Integer> driverDocumentManagemeInsert(
	        DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request) {
	    try {
	    	logger.info("driverDocumentManagemeInsert start");
	        List<DocumentDetails> documentDetailsList = extractDocuments(driverDocumentManagementModel);
	        System.out.println("documentDetailsList: " + documentDetailsList);
	        
	        LocalDate currentDate = LocalDate.now();
	        String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	        String yearFolder = rootFolder + "_" + Year.now().getValue();
	        String driverFolder = yearFolder + "/" + month + "/" + driverDocumentManagementModel.getDriver_name();
	        
	        // Google Drive Folder Setup
	        String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
	        String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(), googleDriveRootFolderId);
	        String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
	        String driverFolderId = GoogleDriveService.getOrCreateFolder(driverDocumentManagementModel.getDriver_name().trim(), monthFolderId);
	        
	        List<CompletableFuture<Void>> futures = new ArrayList<>();
	        
	        for (DocumentDetails doc : documentDetailsList) {
	            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	                try {
	                    String targetFolder = driverFolder + "/" + doc.getSubFolderName();
	                    File folder = new File(targetFolder);
	                    if (!folder.exists()) {
	                        folder.mkdirs();
	                    }
	                    logger.info("driverDocumentManagemeInsert Target Folder: "+targetFolder);
	                    // Save the file locally
	                    File savedFile = new File(targetFolder + "/" + doc.getNewFileName());
	                    doc.getFile().transferTo(savedFile);
	                    
	                    // Upload to Google Drive
	                    String subFolderId = GoogleDriveService.getOrCreateFolder(doc.getSubFolderName().trim(), driverFolderId);
	                    MultipartFile multipartFile = CommonController.convertFileToMultipartFile(savedFile);
	                    String drive_file_id = GoogleDriveService.uploadFileToDrive(multipartFile, subFolderId);
	                    
	                    // Insert document details into the database
	                    Object[] param = {
	                        doc.getDocumentName(),
	                        doc.getNewFileName(),
	                        targetFolder,
	                        doc.getSubFolderId(),
	                        driverDocumentManagementModel.getDriver_id(),
	                        driverDocumentManagementModel.getLoad_number(),
	                        drive_file_id
	                        
	                    };
//	                    dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document, param);
	                    dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_drive_fie_id, param);
	                    
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            });
	            futures.add(future);
	        }
	        
	        // Wait for all uploads to finish before returning response
	        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
	        logger.info("driverDocumentManagemeInsert end");
	        return new ApiResponse<>(true, "Documents saved successfully", true, 1, 1);
	    } catch (Exception e) {
	    	logger.error("Error at driverDocumentManagemeInsert : "+e.getMessage());
	        e.printStackTrace();
	        
	        return new ApiResponse<>(false, "Error while saving document", false, 0, 0);
	    }
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
	           String subFolderShortName =  getSubFolderShortName(documentKey,subFolderList);
	           String newFileName = CommonController.renameFileWithExtension(file,model.getLoad_number()+"_"+documentKey);
               
	            if (subFolderDetails != null) {
	                documents.add(new DocumentDetails(
	                    CommonController.getNewFileNameWithoutExtension(newFileName),
	                    file.getOriginalFilename(),
	                    subFolderDetails.getSub_folder_name(), // Folder name from DB
	                    file,
	                    subFolderDetails.getSub_folder_id(),  // Sub-folder ID from DB
	                    CommonController.renameFileWithExtension(file,model.getLoad_number()+"_"+documentKey))
	               
	                		);
	            }
	        }
	    }

	    return documents;
	}






private String getSubFolderShortName(String documentKey, List<SubFolderDto> subFolderList) {
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
System.out.println("documentKey: "+documentKey);
    // Get the mapped folder name
    String folderName = documentToFolderMap.get(documentKey);
    
    System.out.println("folderName: "+folderName);
for(SubFolderDto shortName:subFolderList)
    System.out.println("subFolderList: "+shortName.getSub_folder_short_name());
    
if (folderName == null) {
        return null; // No mapping found
    }

    // Find the sub-folder details from the DB list
    subFolderList.stream()
        .filter(subFolder -> subFolder.getSub_folder_name().equalsIgnoreCase(folderName))
        .findFirst()
        .orElse(null);
    
    return null;
}

//	@Override
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


//	@Override
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

	
	
	public SubFolderDto getSubFolderDetails(String documentType, List<SubFolderDto> subFolderList) {
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
	public MultipartFile getDocumentFile(DriverDocumentManagementModel model, String documentKey) {
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

//	@Override
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

//	@Override
	public ApiResponse<Integer> driverDocumentManagemeUpdate(
			DriverDocumentManagementUpdateModel documentManagementUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
			
		return null;
	}

	public ApiResponse<Integer> driverDocumentManagemeInsertForSpecifedMonth(
			DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request) 
	{

	    try {
	        List<DocumentDetails> documentDetailsList = extractDocuments(driverDocumentManagementModel);
	        System.out.println("documentDetailsList: " + documentDetailsList);
	        
	        LocalDate currentDate = LocalDate.now();
//	        String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	        String month = driverDocumentManagementModel.getMonth();
	        String yearFolder = rootFolder + "_of_" + Year.now().getValue();
	        String driverFolder = yearFolder + "/" + month + "/" + driverDocumentManagementModel.getDriver_name();
	        
	        // Google Drive Folder Setup
	        String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
	        String yearFolderId = GoogleDriveService.getOrCreateFolder("year_of_" + Year.now().getValue(), googleDriveRootFolderId);
	        String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
	        String driverFolderId = GoogleDriveService.getOrCreateFolder(driverDocumentManagementModel.getDriver_name().trim(), monthFolderId);
	        
	        List<CompletableFuture<Void>> futures = new ArrayList<>();
	        
	        for (DocumentDetails doc : documentDetailsList) {
	            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	                try {
	                    String targetFolder = driverFolder + "/" + doc.getSubFolderName();
	                    File folder = new File(targetFolder);
	                    if (!folder.exists()) {
	                        folder.mkdirs();
	                    }
	                    
	                    // Save the file locally
	                    File savedFile = new File(targetFolder + "/" + doc.getNewFileName());
	                    doc.getFile().transferTo(savedFile);
	                    
	                    // Upload to Google Drive
	                    String subFolderId = GoogleDriveService.getOrCreateFolder(doc.getSubFolderName().trim(), driverFolderId);
	                    MultipartFile multipartFile = CommonController.convertFileToMultipartFile(savedFile);
	                    String drive_file_id = GoogleDriveService.uploadFileToDrive(multipartFile, subFolderId);
	                    
	                    // Insert document details into the database
	                    Object[] param = {
	                        doc.getDocumentName(),
	                        doc.getNewFileName(),
	                        targetFolder,
	                        doc.getSubFolderId(),
	                        driverDocumentManagementModel.getDriver_id(),
	                        month,
	                        driverDocumentManagementModel.getLoad_number(),
	                        drive_file_id
	                    };
//	                    dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_with_month, param);
	                    dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_with_month_drive_file_id, param);
	                    
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            });
	            futures.add(future);
	        }
	        
	        // Wait for all uploads to finish before returning response
	        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
	        
	        return new ApiResponse<>(true, "Documents saved successfully", true, 1, 1);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ApiResponse<>(false, "Error while saving document", false, 0, 0);
	    }
	
	}

	public ApiResponse<Integer> driverDocumentManagementBulkInsert1(
	        DriverDocumentManagementBulkInsertModel documentManagementBulkInsertModel, HttpServletRequest request) {

	    int documentId = 0;
	    String subFolderName = PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");

	    LocalDate currentDate = LocalDate.now();
	    String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    String yearFolder = rootFolder + "_" + Year.now().getValue();
	    String driverFolder = yearFolder + "/" + month + "/" + documentManagementBulkInsertModel.getDriver_name();

	    // Google Drive Folder Setup
	    String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ"; // Ideally move this to properties
	    String yearFolderId = "", monthFolderId = "", driverFolderId = "";
	    String subFolderId = "";

	    try {
	        yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(), googleDriveRootFolderId);
	        monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
	        driverFolderId = GoogleDriveService.getOrCreateFolder(documentManagementBulkInsertModel.getDriver_name().trim(), monthFolderId);
	        subFolderId = GoogleDriveService.getOrCreateFolder(subFolderName.toLowerCase(), driverFolderId);
	    } catch (IOException | GeneralSecurityException e) {
	        logger.error("Failed to create folders in Google Drive", e);
	        //return new ApiResponse<>(0, "Google Drive folder creation failed", false);
	        return new ApiResponse<>(false, "Google Drive folder creation failed", false, 0, 0);
	    }

	    // Local Folder Creation
	    String targetFolder = driverFolder + "/" + subFolderName.toLowerCase();
	    logger.info("Target local folder path: {}", targetFolder);
	    File folder = new File(targetFolder);
	    if (!folder.exists()) {
	        folder.mkdirs();
	    }

	    List<MultipartFile> files = documentManagementBulkInsertModel.getFiles();
	    List<CompletableFuture<Void>> futures = new ArrayList<>();

	    for (MultipartFile file : files) {
	        String newFileName = CommonController.renameFileWithExtension(file, documentManagementBulkInsertModel.getLoad_number() + "_roc");
	        final String currentSubFolderId = subFolderId;
	        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	            try {
	                String newFilePath = targetFolder + "/" + newFileName;
	                File savedFile = new File(newFilePath);
	                file.transferTo(savedFile.toPath());
	                logger.info("Saved file locally: {}", newFilePath);

	                // Upload to Google Drive
	                try {
	                	
	                    MultipartFile convertedFile = CommonController.convertFileToMultipartFile(savedFile);
	                    GoogleDriveService.uploadFileToDrive(convertedFile, currentSubFolderId);
	                    logger.info("Uploaded file to Google Drive: {}", newFileName);
	                } catch (Exception e) {
	                    logger.error("Failed during Google Drive upload", e);
	                    return;
	                }

	                // Insert into database
	                try {
	                    Object[] params = {
	                            CommonController.getNewFileNameWithoutExtension(newFileName),
	                            newFileName,
	                            targetFolder,
	                            2,
	                            documentManagementBulkInsertModel.getDriver_id(),
	                            documentManagementBulkInsertModel.getLoad_number()
	                            
	                    };
	                    int insertedId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document, params);
	                    logger.info("Inserted document ID: {}", insertedId);
	                } catch (Exception e) {
	                    logger.error("Database insertion failed", e);
	                }

	            } catch (IOException e) {
	                logger.error("Failed to save file locally", e);
	            } catch (Exception e) {
	                logger.error("Unexpected error in async process", e);
	            }
	        });

	        futures.add(future);
	    }

	    // Wait for all uploads and inserts to finish
	    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

	    //return new ApiResponse<>(documentId, "Bulk document insert completed", true);
	    return new ApiResponse<>(true, "Bulk document insert completed", true, 1, documentId);
	}

	public ApiResponse<Integer> driverDocumentManagemeBulkInsert(
			DriverDocumentManagementBulkInsertModel documentManagementBulkInsertModel, HttpServletRequest request) {


		logger.info("documentManagementBulkInsertModel: "+documentManagementBulkInsertModel);
	    int documentId = 0;
	    String subFolderName = PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
	    String subFolerNameBol = PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE");
	       
	    LocalDate currentDate = LocalDate.now();
	    String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    month = documentManagementBulkInsertModel.getMonth();
	    String yearFolder = rootFolder.toLowerCase() + "_of_" + Year.now().getValue();
	    String driverFolder = yearFolder + "/" + month + "/" + documentManagementBulkInsertModel.getDriver_name();

	    // Google Drive Folder Setup
	    String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ"; // Ideally move this to properties
	    String yearFolderId = "", monthFolderId = "", driverFolderId = "";
	    String subFolderId = "";
	    String subFolerNameBolCloud = "";

	    try {
	        yearFolderId = GoogleDriveService.getOrCreateFolder("documents" + Year.now().getValue(), googleDriveRootFolderId);
	        monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
	        driverFolderId = GoogleDriveService.getOrCreateFolder(documentManagementBulkInsertModel.getDriver_name().trim(), monthFolderId);
	        subFolderId = GoogleDriveService.getOrCreateFolder(subFolderName, driverFolderId);
	        subFolerNameBolCloud = GoogleDriveService.getOrCreateFolder(subFolerNameBol, driverFolderId);
	  	  
	    } catch (IOException | GeneralSecurityException e) {
	        logger.error("Failed to create folders in Google Drive", e);
	        //return new ApiResponse<>(0, "Google Drive folder creation failed", false);
	        return new ApiResponse<>(false, "Google Drive folder creation failed", false, 0, 0);
	    }

	    // Local Folder Creation
	    String targetFolder = driverFolder + "/" + subFolderName;
	   String targFolderBol = driverFolder + "/" + subFolerNameBol;
	    logger.info("Target local folder path: {}", targetFolder);
	    File folder = new File(targetFolder);
	    if (!folder.exists()) {
	        folder.mkdirs();
	    }
	    
	    File bolFolder = new File(targFolderBol);
	    if (!bolFolder.exists()) {
	    	bolFolder.mkdirs();
	    }

	    List<MultipartFile> files = documentManagementBulkInsertModel.getFiles();
	    List<CompletableFuture<Void>> futures = new ArrayList<>();
	    if (files != null && !files.isEmpty()) {
	    for (MultipartFile file : files) {
	       // String newFileName = CommonController.renameFileWithExtension(file, "_roc");
	        String newFileName = file.getOriginalFilename();
	        
	        final String currentSubFolderId = subFolderId;
	        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	            try {
	                String newFilePath = targetFolder + "/" + newFileName;
	                File savedFile = new File(newFilePath);
	                file.transferTo(savedFile.toPath());
	                logger.info("Saved file locally: {}", newFilePath);

	                // Upload to Google Drive
	                String drive_file_id = "";
	                try {
	                	
	                    MultipartFile convertedFile = CommonController.convertFileToMultipartFile(savedFile);
	                     drive_file_id = GoogleDriveService.uploadFileToDrive(convertedFile, currentSubFolderId);
	                    logger.info("Uploaded file to Google Drive: {}", newFileName);
	                } catch (Exception e) {
	                    logger.error("Failed during Google Drive upload", e);
	                    return;
	                }

	                // Insert into database
	                try {
	                	
	                    Object[] params = {
	                            CommonController.getNewFileNameWithoutExtension(newFileName),
	                            newFileName,
	                            targetFolder,
	                            2,
	                            documentManagementBulkInsertModel.getDriver_id(),
	                            documentManagementBulkInsertModel.getMonth(),
	                            documentManagementBulkInsertModel.getLoad_number(),
	                            drive_file_id
	                    };
//	                    int insertedId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_with_month, params);
	                    int insertedId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_with_month_drive_file_id, params);
	                   
	                    
	                    logger.info("Inserted document ID: {}", insertedId);
	                } catch (Exception e) {
	                    logger.error("Database insertion failed", e);
	                }

	            } catch (IOException e) {
	                logger.error("Failed to save file locally", e);
	            } catch (Exception e) {
	                logger.error("Unexpected error in async process", e);
	            }
	        });

	        futures.add(future);
	    }
	    }
	    // Wait for all uploads and inserts to finish
	    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

	    //bulk insert for the POD
	    List<MultipartFile> bolFiles = documentManagementBulkInsertModel.getPOD();
	    List<CompletableFuture<Void>> futures_bol = new ArrayList<>();
	    if (bolFiles != null && !bolFiles.isEmpty()) {
	    for (MultipartFile bolFile : bolFiles) {
	      //  String newFileName = CommonController.renameFileWithExtension(bolFile, "_POD");
	      String  newFileName = bolFile.getOriginalFilename();
	        final String currentSubFolderId = subFolerNameBolCloud;
	        CompletableFuture<Void> future_bol = CompletableFuture.runAsync(() -> {
	            try {
	                String newFilePath = targFolderBol + "/" + newFileName;
	                File savedFile = new File(newFilePath);
	                bolFile.transferTo(savedFile.toPath());
	                logger.info("Saved file locally: {}", newFilePath);

	                // Upload to Google Drive
	                String drive_file_id = "";
	                try {
	                	
	                    MultipartFile convertedFile = CommonController.convertFileToMultipartFile(savedFile);
	                     drive_file_id = GoogleDriveService.uploadFileToDrive(convertedFile, currentSubFolderId);
	                    logger.info("Uploaded file to Google Drive: {}", newFileName);
	                } catch (Exception e) {
	                    logger.error("Failed during Google Drive upload", e);
	                    return;
	                }

	                // Insert into database
	                try {
	                	
	                    Object[] params = {
	                            CommonController.getNewFileNameWithoutExtension(newFileName),
	                            newFileName,
	                            targFolderBol,
	                            6,
	                            documentManagementBulkInsertModel.getDriver_id(),
	                            documentManagementBulkInsertModel.getMonth(),
	                            documentManagementBulkInsertModel.getLoad_number(),
	                            drive_file_id
	                    };
//	                    int insertedId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_with_month, params);
	                    int insertedId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_with_month_drive_file_id, params);
	                   
	                    
	                    logger.info("Inserted document ID: {}", insertedId);
	                } catch (Exception e) {
	                    logger.error("Database insertion failed", e);
	                }

	            } catch (IOException e) {
	                logger.error("Failed to save file locally", e);
	            } catch (Exception e) {
	                logger.error("Unexpected error in async process", e);
	            }
	        });

	        futures_bol.add(future_bol);
	    }
	    }
	    // Wait for all uploads and inserts to finish
	    CompletableFuture.allOf(futures_bol.toArray(new CompletableFuture[0])).join();

	    
	    
	    //return new ApiResponse<>(documentId, "Bulk document insert completed", true);
	    return new ApiResponse<Integer>(true, "Bulk document insert completed", true, 1, documentId);
	
	}

	public ApiResponse<Integer> deleteDocument(DriverDocumentDeleteModel documentDeleteModel,
			HttpServletRequest request)  {

		boolean deleteFileFromLocalAndDrive = false;

		String sub_folder_name_for_roc = PropertiesReader.getProperty("constant",
				"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
		LocalDate currentDate = LocalDate.now();
		String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		String yearFolder = rootFolder + "_" + Year.now().getValue();
		String driverFolder = yearFolder + "/" + month + "/" + documentDeleteModel.getDriver_name();

		String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
		String yearFolderId  = "";
		String monthFolderId = "";
		String tempDriverFolderId = "";  // Temporary variable
		try {
			yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
					googleDriveRootFolderId);
			monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
			tempDriverFolderId = GoogleDriveService.getOrCreateFolder(documentDeleteModel.getDriver_name().trim(),
					monthFolderId);
		} catch (IOException | GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error("Error accessing Google Drive folders", e1);
			return new ApiResponse<>(false, "Google Drive folder access failed", false, 0, 0);
		} 
		
		final String driverFolderId = tempDriverFolderId;
		String targetFolder = driverFolder + "/" + sub_folder_name_for_roc;
		String filePath = targetFolder + "/" + documentDeleteModel.getOriginal_document_name();

		logger.info("Initiating document deletion: {}", filePath);

		CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
			try {
				
				String subFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name_for_roc, driverFolderId);
				return GoogleDriveService.deleteFileFromLocalAndDrive(filePath, subFolderId,
						documentDeleteModel.getOriginal_document_name());
			} catch (Exception ex) {
				logger.error("Error during async Google Drive deletion", ex);
				return false;
			}
		});

		try {
			deleteFileFromLocalAndDrive = future.get();

			if (deleteFileFromLocalAndDrive) {
				Object[] param_for_doc_delete = { documentDeleteModel.getDocument_id() };

				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.mark_documents_as_deleted_by_id, param_for_doc_delete);

				return new ApiResponse<>(true, "Document deleted successfully", true, 1, 1);
			} else {
				logger.warn("File deletion failed. Skipping DB update.");
				return new ApiResponse<>(false, "Document deletion failed", false, 0, 0);
			}

		} catch (InterruptedException | ExecutionException e) {
			logger.error("Async document deletion task failed", e);
			return new ApiResponse<>(false, "Deletion process interrupted", false, 0, 0);
		}
	}

}
