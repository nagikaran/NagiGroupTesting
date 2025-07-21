package com.NagiGroup.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
import com.NagiGroup.dto.companyDetails.CompanyDetailsDto;
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
import com.NagiGroup.model.mail.InvoiceMailModel;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.service.MailService;
import com.NagiGroup.utility.ApiResponse;
import com.NagiGroup.utility.CommonUtility;
import com.NagiGroup.utility.PropertiesReader;

@Repository
public class LoadRepository {
	private static final Logger logger = LoggerFactory.getLogger(LoadRepository.class);

	public static String rootFolder = PropertiesReader.getProperty("constant", "BASEURL_FOR_YEAR");

	public static final int STATUS_PENDING = 1;
	public static final int STATUS_ASSIGNED = 2;
	public static final int STATUS_IN_PROGRESS = 3;
	public static final int STATUS_COMPLETED = 4;
	private DbContextService dbContextserviceBms;
	private MailService mailService;

	public LoadRepository(DbContextService dbContextserviceBms, MailService mailService) {
		this.dbContextserviceBms = dbContextserviceBms;
		this.mailService = mailService;
	}

//	@Override
	public ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		logger.info("LoadRepository : loadInsert start");
		CommonController commonController = new CommonController();
		int load_id = 0;
		try {

			int statusId = (loadModel.getAssign_to() != 0) ? 2 : 1; // 2 = Assigned, 1 = Pending
			int previousStatus = (statusId == 2) ? 1 : 0; // If assigned at creation, previous was "Pending"

			String pick_up_date_string = loadModel.getPick_up_date_string();
			String delievery_date_string = loadModel.getDelievery_date_string();
			// String earliest_time_arrival_string =
			// loadModel.getEarliest_time_arrival_string();

			LocalDateTime pick_up_date_time = CommonUtility.parseDateString(pick_up_date_string);
			LocalDateTime delievery_date_time = CommonUtility.parseDateString(delievery_date_string);
			// LocalDateTime earliest_time_arrival =
			// CommonUtility.parseDateString(earliest_time_arrival_string);
			logger.info("loadModel: " + loadModel);
			logger.info("pick_up_date_time: " + pick_up_date_time);
			logger.info("delievery_date_time: " + delievery_date_time);
			// logger.info("earliest_time_arrival: " + earliest_time_arrival);
			logger.info("pick_up_date_string: " + pick_up_date_string);

			Object param[] = { loadModel.getLoadNumber(), // String
					loadModel.getSource(), // String
					loadModel.getDestination(), // String
					pick_up_date_time, // timestamp without time zone
					delievery_date_time, // timestamp without time zone
					loadModel.getAssign_to() != 0 ? loadModel.getAssign_to() : 0, // integer
					loadModel.getBase_price(), // double
					loadModel.getFinal_price(), // double
					loadModel.getAssign_to() != 0 ? loadModel.getAssign_to() : 0, // integer
					statusId, // integer
					loadModel.getTrailer_used(), // integer
					commonController.getUserDtoDataFromToken(request), // integer
					loadModel.getCompany_id(),// String
					// earliest_time_arrival,//timestamp without time zone
			};

			load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_load, param);
			if (load_id != 0) {
				logger.info("LoadRepository : load_id : " + load_id);
				Object statusHistoryParam[] = { load_id, // Load ID
						loadModel.getLoadNumber(),
						// Previous status is always "Pending" (1)
						statusId, // New status (Pending or Assigned)
						commonController.getUserDtoDataFromToken(request) };
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_load_status_history, statusHistoryParam);
			}
			if (load_id != 0 && loadModel.getRoc() != null && loadModel.getAssign_to() == 0) {
				logger.info("LoadRepository : load_id : " + load_id);
				/*
				 * file saving work now here i want to save the roc
				 */
				/*if file is image converting it into the pdf*/
				MultipartFile roc = loadModel.getRoc();
				if (roc != null && CommonUtility.isImage(roc)) {
					logger.info("LoadRepository : Extendion : " + loadModel.getRoc().getOriginalFilename());
				    MultipartFile rocPdf = CommonUtility.convertImageToPdfUsingIText(roc);
				    loadModel.setRoc(rocPdf);
				} else {
				    // It's already a PDF or unsupported — skip conversion
				    loadModel.setRoc(roc);
				}

				

				
				String fileNameWithoutExtension = CommonController.getFileNameWithoutExtension(loadModel.getRoc());
				String newFileName = CommonController.renameFileWithExtension(loadModel.getRoc(),
						loadModel.getLoadNumber() + "_roc");

				String baseUrl = PropertiesReader.getProperty("constant", "BASEURL");
				String folderName = PropertiesReader.getProperty("constant",
						"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD" + "/");
				String basePathForDocument = baseUrl
						+ PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD" + "/")
						+ loadModel.getLoadNumber() + "/";
				boolean isCreated = CommonUtility.saveDocument(loadModel.getRoc(), loadModel.getLoadNumber(), baseUrl,
						folderName);
				if (isCreated) {
					String fileName = newFileName;
					String sourcePath = basePathForDocument + fileName;
					logger.info("loadNumber: " + loadModel.getLoadNumber());
					logger.info("fileNameWithoutExtension: "
							+ CommonController.getNewFileNameWithoutExtension(newFileName));
					logger.info("basePathForDocument: " + basePathForDocument);
					logger.info("OriginalFilename: " + newFileName);

					// System.out.println("File deleted successfully.");
					Object loadDocumentParam[] = { loadModel.getLoadNumber(),
							CommonController.getNewFileNameWithoutExtension(newFileName), basePathForDocument,
							newFileName };
					int document_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_load_document,
							loadDocumentParam);
					if (document_id != 0) {
						logger.info("File details updated successfully.");
					} else {
						logger.info("Failed to update File details.");
					}

					logger.info("LoadRepository : sourcePath : " + sourcePath);
				}
			} else {
				String sub_folder_name = PropertiesReader.getProperty("constant",
						"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
				LocalDate currentDate = LocalDate.now();
				String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
				String yearFolder = rootFolder + "_" + Year.now().getValue();
				String driverFolder = yearFolder + "/" + month + "/" + loadModel.getDriver_name();

				// Google Drive Folder Setup
				String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
				String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
						googleDriveRootFolderId);
				String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
				String driverFolderId = GoogleDriveService.getOrCreateFolder(loadModel.getDriver_name().trim(),
						monthFolderId);

				String newFileName = CommonController.renameFileWithExtension(loadModel.getRoc(),
						loadModel.getLoadNumber() + "_roc");
				// Read file data BEFORE the async block
				byte[] fileBytes = loadModel.getRoc().getBytes(); // ✅ Safe: temp file still exists
				String originalFileName = loadModel.getRoc().getOriginalFilename();
				//String drive_file_id = "";
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					try {
						String targetFolder = driverFolder + "/" + sub_folder_name;
						File folder = new File(targetFolder);
						if (!folder.exists()) {
							folder.mkdirs();
						}

						String newFilePath = targetFolder + "/" + newFileName;
						logger.info("newFilePath: " + newFilePath);
						File savedFile = new File(newFilePath);

						// loadModel.getRoc().transferTo(savedFile);
						try (FileOutputStream fos = new FileOutputStream(savedFile)) {
							fos.write(fileBytes); // ✅ Writes from in-memory bytes
						}

						// Upload to Google Drive
						String	drive_file_id = "";
						try {
							String subFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name, driverFolderId);
							MultipartFile multipartFile = CommonController.convertFileToMultipartFile(savedFile);
							drive_file_id  = GoogleDriveService.uploadFileToDrive(multipartFile, subFolderId);
						} catch (IOException ioEx) {
							// Handle file conversion or upload errors
							System.err.println("Failed during Google Drive operations: " + ioEx.getMessage());
							ioEx.printStackTrace();
							return;
						} catch (Exception driveEx) {
							System.err.println("Unexpected error during Google Drive upload: " + driveEx.getMessage());
							driveEx.printStackTrace();
							return;
						}

						// Insert document details into the database
						try {
							Object[] param_for_document_insert = {
									CommonController.getNewFileNameWithoutExtension(newFileName),
									newFileName,
									targetFolder,
									2,
									loadModel.getAssign_to(),
									loadModel.getLoadNumber(),
									drive_file_id
									};
//							dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document,
//									param_for_document_insert);
							dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document_drive_fie_id,
									param_for_document_insert);
							
						} catch (Exception dbEx) {
							System.err.println("Unexpected database error: " + dbEx.getMessage());
							dbEx.printStackTrace();
						}

					} catch (IOException fileEx) {
						System.err.println("Failed to save file locally: " + fileEx.getMessage());
						fileEx.printStackTrace();
					} catch (Exception ex) {
						System.err.println("Unexpected error in async process: " + ex.getMessage());
						ex.printStackTrace();
					}
				});

			}

			/**/

			if (load_id != 0) {
				return new ApiResponse<Integer>(true, "Load added successfully", true, load_id, 1);
			} else {
				return new ApiResponse<Integer>(false, "Something went wrong while saving the data", true, load_id, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("LoadRepository : Error at : loadInsert " + e);
			return new ApiResponse<Integer>(false, "Something went wrong while saving the data", false, load_id, 0);
		}
	}

//	@Override
	public ApiResponse<Integer> loadUpdate(LoadUpdateModel loadUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		int load_id = 0;
		CommonController commonController = new CommonController();
		try {
			String pick_up_date_string = loadUpdateModel.getPick_up_date_string();
			String delievery_date_string = loadUpdateModel.getDelievery_date_string();
			String earliest_time_arrival_string = loadUpdateModel.getEarliest_time_arrival_string();

			LocalDateTime pick_up_date_time = CommonUtility.parseDateString(pick_up_date_string);
			LocalDateTime delievery_date_time = CommonUtility.parseDateString(delievery_date_string);
			LocalDateTime earliest_time_arrival = CommonUtility.parseDateString(earliest_time_arrival_string);

			logger.info("pick_up_date_time: " + pick_up_date_time);
			logger.info("delievery_date_time: " + delievery_date_time);
			logger.info("earliest_time_arrival: " + earliest_time_arrival);
			logger.info("pick_up_date_string: " + pick_up_date_string);
			Object param[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getLoadNumber(),
					loadUpdateModel.getSource(), loadUpdateModel.getDestination(), pick_up_date_time,
					delievery_date_time, earliest_time_arrival, loadUpdateModel.getDriver_id(),
					loadUpdateModel.getBase_price(), loadUpdateModel.getFinal_price(),
					loadUpdateModel.getAssign_to() != 0 ? loadUpdateModel.getAssign_to() : 0,
					commonController.getUserDtoDataFromToken(request) };

			load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_Update, param);

			if (load_id != 0 && loadUpdateModel.getRoc() != null) {

				logger.info("LoadRepository : load_id : " + load_id);
				/*
				 * file saving work now here i want to save the roc
				 */
				String baseUrl = PropertiesReader.getProperty("constant", "BASEURL");
				String folderName = PropertiesReader.getProperty("constant", "BASEURL_FOR_ROC");
				String basePathForDocument = baseUrl + loadUpdateModel.getLoadNumber() + "/"
						+ PropertiesReader.getProperty("constant", "BASEURL_FOR_ROC");
				boolean isCreated = CommonUtility.saveDocument(loadUpdateModel.getRoc(),
						loadUpdateModel.getLoadNumber(), baseUrl, folderName);
				if (isCreated) {

					String fileName = loadUpdateModel.getRoc().getOriginalFilename();
					String sourcePath = basePathForDocument + loadUpdateModel.getOldFileName();
					boolean result = CommonUtility.deleteFile(sourcePath);
					if (result) {
						// System.out.println("File deleted successfully.");
						Object loadDocumentParam[] = { loadUpdateModel.getLoadNumber(),
								loadUpdateModel.getRoc().getOriginalFilename() };
						int document_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_document_update,
								loadDocumentParam);
						if (document_id != 0) {
							logger.info("File details updated successfully.");
						} else {
							logger.info("Failed to update File details.");
						}

					} else {
						logger.info("File deletion failed.");
					}

				}

			}
			logger.info("LoadRepository : load_id : " + load_id);
			if (load_id != 0) {
				return new ApiResponse<Integer>(true, "Load updated successfully", true, load_id, 1);
			} else {
				return new ApiResponse<Integer>(false, "Something went wrong while updating  the load", true, load_id,
						0);
			}

		} catch (Exception e) {
			logger.info("LoadRepository : Error at : " + e);
			return new ApiResponse<Integer>(false, "Something went wrong while updating  the load", true, load_id, 0);
		}
	}

//	@Override
	public ApiResponse<List<LoadDto>> geAlltLoads() {

		List<LoadDto> loadDtos = null;
		try {
			logger.info("LoadRepository : geAlltLoads Start");
			loadDtos = dbContextserviceBms.QueryToList(QueryMaster.get_all_loads, LoadDto.class);
			logger.info("LoadRepository : geAlltLoads Start");
			return new ApiResponse<List<LoadDto>>(true, "Total Record " + loadDtos.size() + " ", true, loadDtos,
					loadDtos.size());

		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : geAlltLoads :", e);
			return new ApiResponse<List<LoadDto>>(false, e.getMessage(), false, null, 0);
		}
	}

//	@Override
	public ApiResponse<LoadDto> getLoadById(int load_id) {
		// TODO Auto-generated method stub
		logger.info("LoadRepository : getLoadById Start");
		LoadDto loadDto = null;
		try {
			Object params[] = { load_id };
			loadDto = dbContextserviceBms.QueryToFirstWithParam(QueryMaster.get_load_by_id, params, LoadDto.class);
			logger.info("LoadRepository : getLoadById End");
			return new ApiResponse<LoadDto>(true, "Total Record " + 1 + " ", true, loadDto, 1);
		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : getLoadById :", e);
			return new ApiResponse<LoadDto>(false, "No record found", false, null, 0);
		}
	}

//	@Override
	public ApiResponse<Integer> updateLoad(LoadUpdateModel loadUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			logger.info("LoadRepository : updateLoad Start");
			int latest_status_id = 0;
			boolean isUpdated = false;
			CommonController commonController = new CommonController();
			Object load_status_param[] = { loadUpdateModel.getLoadNumber() };
			latest_status_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,
					load_status_param);

			int current_status = latest_status_id;
			int updatedBy = commonController.getUserDtoDataFromToken(request);
			if (current_status == 4) {
				return new ApiResponse<Integer>(false, "Cannot update a completed load", false, 0, 0);
			}
			Object reassigning_driver[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getAssign_to(), updatedBy

			};
			// Reassign Driver (Allowed only in Pending or Assigned)
			if (loadUpdateModel.getAssign_to() != 0 && (current_status == 1 || current_status == 2)) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_driver, reassigning_driver);
				isUpdated = true;
			}

			Object update_sorce_pick_up[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getSource(),
					loadUpdateModel.getPick_up_date(), updatedBy

			};
			// Update Source & Pickup Date/Time (Allowed only in Pending or Assigned)
			if ((loadUpdateModel.getSource() != null || loadUpdateModel.getPick_up_date() != null)
					&& (current_status == 1 || current_status == 2)) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_source_pickup, update_sorce_pick_up);
				isUpdated = true;
			}
			Object update_destination_delivery[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getDestination(),
					loadUpdateModel.getDelievery_date(), updatedBy

			};

			// Update Destination & Delivery Date/Time (Allowed in Pending, Assigned, or In
			// Progress)
			if ((loadUpdateModel.getDestination() != null || loadUpdateModel.getDelievery_date() != null)
					&& (current_status != 4)) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_destination_delivery,
						update_destination_delivery);
				isUpdated = true;
			}

			Object update_final_price[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getFinal_price(), updatedBy

			};
			// Update Final Price (Allowed in all except Completed)
			if (loadUpdateModel.getFinal_price() != 0 && current_status != 4) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_final_price, update_final_price);
				isUpdated = true;
			}

			Object update_base_price[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getBase_price(), updatedBy

			};
			// Update Base Price (Allowed in all except Completed)
			if (loadUpdateModel.getBase_price() != 0 && current_status != 4) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_base_price, update_base_price);
				isUpdated = true;

			}

			Object update_company_name[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getCompany_name(), updatedBy

			};

			// Update Company Name (Allowed in all except Completed)
			if (loadUpdateModel.getCompany_name() != null && current_status != 4) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_loads_company_name, update_company_name);
				isUpdated = true;
			}
			Object update_trailer_used[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getTrailer_used(), updatedBy

			};

			if (loadUpdateModel.getTrailer_used() != null && current_status != 4) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_trailer_used, update_trailer_used);
				isUpdated = true;
			}
			Object update_loads_number[] = { loadUpdateModel.getLoad_id(), loadUpdateModel.getLoadNumber(), updatedBy

			};

			// Update Load Number (Allowed in all except Completed)
			if (loadUpdateModel.getLoadNumber() != null && current_status != 4) {
				dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_number, update_loads_number);
				isUpdated = true;
			}

			if (!isUpdated) {
				return new ApiResponse<Integer>(false, "No changes were made", false, 0, 0);
			}

			logger.info("LoadRepository : updateLoad end");
			return new ApiResponse<Integer>(true, "Load updated successfully", true, 1, 1);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("LoadRepository : Error in updateLoad", e);
			e.printStackTrace();
			return new ApiResponse<Integer>(false, "Error while updating load", false, 0, 0);
		}

	}

//	@Override
	public ApiResponse<Integer> assignLoad(LoadStatusModel loadStatusModel, HttpServletRequest request) {
		logger.info("LoadRepository : assignLoad start hello "+loadStatusModel);
		CommonController commonController = new CommonController();
		int updatedBy = 0;
		try {
			updatedBy = commonController.getUserDtoDataFromToken(request);
			Object load_status_param[] = { loadStatusModel.getLoad_number() };
			int previous_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,
					load_status_param);

			// If load is neither PENDING nor ASSIGNED, it can't be reassigned
			if (previous_status != STATUS_PENDING && previous_status != STATUS_ASSIGNED) {
				return new ApiResponse<Integer>(false, "Load cannot be reassigned. Current status: " + previous_status,
						false, 0, 0);
			}

			// Insert into Status History
			Object statusHistoryParam[] = { loadStatusModel.getLoad_id(), STATUS_ASSIGNED,
					loadStatusModel.getDriver_id(), updatedBy };
			int load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_status, statusHistoryParam);

			if (load_id != 0 && previous_status == 1) {
				logger.info("LoadRepository : assignLoad end");

				String sourcePathStr = loadStatusModel.getLoad_doc_base_path() + loadStatusModel.getFile_name(); // e.g.,
				// D:/NAGI_GROUP/load4455001
				String sub_folder_name = PropertiesReader.getProperty("constant",
						"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");

// Create folder path structure
				LocalDate currentDate = LocalDate.now();
				String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
				String yearFolder = rootFolder + "_" + Year.now().getValue();
				String driverFolder = yearFolder + "/" + month + "/" + loadStatusModel.getDriver_name();
				String targetFolder = driverFolder + "/" + sub_folder_name;

// Setup Google Drive folders
				String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
				String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
						googleDriveRootFolderId);
				String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
				String driverFolderId = GoogleDriveService.getOrCreateFolder(loadStatusModel.getDriver_name().trim(),
						monthFolderId);

// Run the copy/upload logic asynchronously
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					try {
						// Create target directory if it doesn't exist
						File folder = new File(targetFolder);
						if (!folder.exists()) {
							folder.mkdirs();
						}

						// Define new file name and path
						String newFileName = loadStatusModel.getLoad_number() + "_roc.pdf"; // You can format this name
																							// as needed
						String newFilePath = targetFolder + "/" + newFileName;

						// Copy source file to target folder
						Path sourcePath = Paths.get(sourcePathStr);
						Path targetPath = Paths.get(newFilePath);
						Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

						// Upload to Google Drive
						try {
							String subFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name.trim(),
									driverFolderId);
							File savedFile = targetPath.toFile();
							MultipartFile multipartFile = CommonController.convertFileToMultipartFile(savedFile);
							GoogleDriveService.uploadFileToDrive(multipartFile, subFolderId);
						} catch (IOException ioEx) {
							System.err.println("Google Drive upload error: " + ioEx.getMessage());
							ioEx.printStackTrace();
							return;
						}
						String newFileNameWithoutExtension = CommonController
								.getNewFileNameWithoutExtension(newFileName);

						// Insert document record into DB
						try {
							Object[] param_for_document_insert = {
									CommonController.getNewFileNameWithoutExtension(newFileName), newFileName,
									targetFolder, 2, loadStatusModel.getDriver_id(),
									loadStatusModel.getLoad_number()
									};
							dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document,
									param_for_document_insert);
						} catch (Exception dbEx) {
							System.err.println("DB error while inserting document: " + dbEx.getMessage());
							dbEx.printStackTrace();
						}

					} catch (IOException fileEx) {
						System.err.println("File copy error: " + fileEx.getMessage());
						fileEx.printStackTrace();
					} catch (Exception ex) {
						System.err.println("Unexpected error in async process: " + ex.getMessage());
						ex.printStackTrace();
					}
				});

				return new ApiResponse<Integer>(true, "Load assigned successfully. Current status: Assigned" + 1, true,
						1, 1);
			}
			if (load_id != 0 && previous_status == 2) {
				logger.info("LoadRepository : assignLoad end : success");
				/*
				 * here we need to reassign the load to the other driver so the params we need
				 * are this is the old driver doc path
				 * D:\NAGI_GROUP\DOCUMENTS_2025\MAY\KANWAR\DISPATCH RECORD
				 */

				logger.info("LoadRepository : assignLoad end");

				// D:/NAGI_GROUP/load4455001
				String sub_folder_name = PropertiesReader.getProperty("constant",
						"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");

// Create folder path structure
				LocalDate currentDate = LocalDate.now();
				String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
				String yearFolder = rootFolder + "_" + Year.now().getValue();
				String driverFolder = yearFolder + "/" + month + "/" + loadStatusModel.getDriver_name();
				String targetFolder = driverFolder + "/" + sub_folder_name.trim();
				// sourcePathStr = loadStatusModel.getLoad_doc_base_path() +
				// loadStatusModel.getFile_name(); // e.g.,
				String sourcePathStr = yearFolder + "/" + month + "/" + loadStatusModel.getOld_driver_name() + "/"
						+ sub_folder_name + "/" + loadStatusModel.getFile_name();
// Setup Google Drive folders
				String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
				String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
						googleDriveRootFolderId);
				String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
				String driverFolderId = GoogleDriveService.getOrCreateFolder(loadStatusModel.getDriver_name().trim(),
						monthFolderId);
				String oldDriverFolderId = GoogleDriveService
						.getOrCreateFolder(loadStatusModel.getOld_driver_name().trim(), monthFolderId);

// Run the copy/upload logic asynchronously
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					try {
						// Create target directory if it doesn't exist
						File folder = new File(targetFolder);
						if (!folder.exists()) {
							folder.mkdirs();
						}

						// Define new file name and path
						String newFileName = loadStatusModel.getLoad_number() + "_roc.pdf"; // You can format this name
																							// as needed
						String newFilePath = targetFolder + "/" + newFileName;

						// Copy source file to target folder
						Path sourcePath = Paths.get(sourcePathStr);
						Path targetPath = Paths.get(newFilePath);
						Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
						Files.deleteIfExists(sourcePath);

						// Upload to Google Drive
						try {
							String oldDriversubFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name,
									oldDriverFolderId);
							String fileId = GoogleDriveService.findFileIdInFolder(loadStatusModel.getFile_name(),
									oldDriversubFolderId);

							File savedFile = targetPath.toFile();
//								
							String newSubFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name,
									driverFolderId);
							if (fileId != null) {
								GoogleDriveService.moveFileToFolder(fileId, newSubFolderId,
										loadStatusModel.getFile_name());
							} else {
								logger.error("File not found in old driver's folder: {}",
										loadStatusModel.getFile_name());
							}

							// GoogleDriveService.moveFileToFolder(fileId,
							// newSubFolderId,loadStatusModel.getFile_name());

						} catch (IOException ioEx) {
							System.err.println("Google Drive upload error: " + ioEx.getMessage());
							ioEx.printStackTrace();
							return;
						}
						String newFileNameWithoutExtension = CommonController
								.getNewFileNameWithoutExtension(newFileName);

						// Insert document record into DB
						try {
							Object[] param_for_document_update = {
									loadStatusModel.getDriver_documents_id(),
									CommonController.getNewFileNameWithoutExtension(newFileName), //p_document_name
									newFileName,//p_original_document_name with extension
									targetFolder, //p_document_path
									2, //p_sub_folder_id
									loadStatusModel.getDriver_id(),
									loadStatusModel.getLoad_number()
									};
						
							dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_driver_document,
									param_for_document_update);
						} catch (Exception dbEx) {
							System.err.println("DB error while inserting document: " + dbEx.getMessage());
							dbEx.printStackTrace();
						}

					} catch (IOException fileEx) {
						System.err.println("File copy error: " + fileEx.getMessage());
						fileEx.printStackTrace();
					} catch (Exception ex) {
						System.err.println("Unexpected error in async process: " + ex.getMessage());
						ex.printStackTrace();
					}
				});

				return new ApiResponse<Integer>(true, "Load re-assigned successfully. Current status: Assigned" + 1,
						true, 1, 1);
			}

			else {
				logger.info("LoadRepository : assignLoad end : failed");
				return new ApiResponse<Integer>(false, "Error updating load status: " + 0, false, 0, 0);
			}

			/* now file savig work for the assigned driver */

		} catch (Exception e) {
			logger.error("Error in startLoad", e);
			e.printStackTrace();
			return new ApiResponse<Integer>(false, "Error while assigning load: " + 0, false, 0, 0);
		}

	}

//	@Override
	public ApiResponse<Integer> markLoadInProgress(LoadStatusModel loadStatusModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			System.out.println("loadStatusModel: " + loadStatusModel);
			logger.info("LoadRepository : markLoadInProgress end");
			CommonController commonController = new CommonController();
			int updatedBy = commonController.getUserDtoDataFromToken(request);
			System.out.println("updatedBy: " + updatedBy);
			Object load_status_param[] = { loadStatusModel.getLoad_number() };
			int previous_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,
					load_status_param);

			if (previous_status != STATUS_ASSIGNED) {
				return new ApiResponse<Integer>(false, "Load cannot be started. Current status: Pending", false, 0, 0);
			}
			Object in_progress_param[] = { loadStatusModel.getLoad_id(), updatedBy

			};
			int in_progress_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.mark_load_in_progress,
					in_progress_param);

			if (in_progress_status != 0) {
				logger.info("LoadRepository : markLoadInProgress end : success");
				return new ApiResponse<Integer>(true,
						"Load marked as in progress successfully. Current status: In-Progress" + 1, true, 1, 1);
			} else {
				logger.info("LoadRepository : markLoadInProgress end : fail");
				return new ApiResponse<Integer>(false, "Failed to mark the load In-progress. Current status: Assigned",
						false, 0, 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error in markLoadInProgress", e);
			e.printStackTrace();
			return new ApiResponse<Integer>(false, "Error while marking the load In-progress. Current status: Assigned",
					false, 0, 0);
		}

	}

//	@Override
	public ApiResponse<Integer> markLoadComplete(LoadCompletionModel loadCompletionModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			CommonController commonController = new CommonController();
			logger.info("LoadRepository : markLoadComplete start");
			String source_path_for_roc = "";
			String source_path_for_pod = "";
			String sub_folder_name_for_roc = PropertiesReader.getProperty("constant",
					"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
			
			
			int updatedBy = commonController.getUserDtoDataFromToken(request);
			
			logger.info("updatedBy 01: "+updatedBy);
			Object load_status_param[] = { loadCompletionModel.getLoad_number() };
			int previous_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,
					load_status_param);

			if (previous_status != STATUS_IN_PROGRESS) {
				return new ApiResponse<Integer>(false,
						"Load cannot be Mark as Completed.As Current status is not In Progress", false, 0, 0);

			}
			loadCompletionModel.getCompany_id();
			logger.info("loadComplitionModel: " + loadCompletionModel);
			Object load_completion_param[] = { 
					loadCompletionModel.getLoad_id(),
					loadCompletionModel.getLoad_number(),
					updatedBy, 
					loadCompletionModel.getLumper_value(), 
					loadCompletionModel.getLumper_paid_by(),
					loadCompletionModel.getDetention_value(), 
					loadCompletionModel.isDetention_flag(),
					loadCompletionModel.isLayover_flag(),
					loadCompletionModel.getLayover_value(),
					loadCompletionModel.getScale_value(),
					loadCompletionModel.getExtra_stop_charge(),
					loadCompletionModel.getTrailer_wash(),
					loadCompletionModel.isLog_applicable_flag()
		
			};

//			int load_complition_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.handle_load_completion,
//					load_completion_param);
			int load_complition_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.handle_load_completion_test,
					load_completion_param);
			System.out.println("load_complition_status: "+load_complition_status);
			if (loadCompletionModel.getPod() != null) {
				/**/
				
				String newFileName = loadCompletionModel.getLoad_number() + "_pod";
				newFileName = CommonController.renameFileWithExtension(loadCompletionModel.getPod(), newFileName);
				String newFileNameWithoutExtension = CommonController.getNewFileNameWithoutExtension(newFileName);
				LocalDate currentDate = LocalDate.now();
				String month = currentDate.getMonth().toString(); // Example: "FEBRUARY"
				String yearFolder = rootFolder + "_" + Year.now().getValue(); // Example: "documents_2025"

				String driverFolder = yearFolder + "/" + month + "/" + loadCompletionModel.getDriver_name();
				String targetFolder = driverFolder + "/"
						+ PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE");
				String no_log_folder_path = "";

				if (!loadCompletionModel.isLog_applicable_flag()) {
				    // Original ROC folder (e.g., /driverFolder/ROC)
				    targetFolder = driverFolder + sub_folder_name_for_roc;

				    // No_Log folder path
				    no_log_folder_path = rootFolder + "_" + Year.now().getValue() + "_No_Log/" 
				                       + month + "/" 
				                       + loadCompletionModel.getDriver_name() 
				                       + sub_folder_name_for_roc;

				    logger.info("loadCompletionModel: no_log_folder_path: " + no_log_folder_path);

				    try {
				        // Create target directory if it doesn't exist
				        File folder = new File(no_log_folder_path);
				        if (!folder.exists()) {
				            folder.mkdirs();
				        }

				        // File name
				        String fileName = loadCompletionModel.getLoad_number() + "_roc.pdf";

				        // Use Paths.get to avoid manual slashes
				        Path sourcePath = Paths.get(targetFolder, fileName);
				        Path targetPath = Paths.get(no_log_folder_path, fileName);

				        // Copy then delete
				        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				        Files.deleteIfExists(sourcePath);

				        logger.info("Moved file from {} to {}", sourcePath, targetPath);

				    } catch (IOException e) {
				        logger.error("Error while moving ROC file to No_Log folder: " + e.getMessage(), e);
				    }
				    logger.info("loadCompletionModel: Moving File to No Log Start For Drive");
				 // Setup Google Drive folders
					String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
					String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
							googleDriveRootFolderId);
					String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
					String driverFolderId = GoogleDriveService.getOrCreateFolder(loadCompletionModel.getDriver_name().trim(),
							monthFolderId);
					String subFolderId = GoogleDriveService.getOrCreateFolder(loadCompletionModel.getDriver_name().trim(),
							sub_folder_name_for_roc);
					
					
					
	// Run the copy/upload logic asynchronously
					CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
						try {
							
							// Define new file name and path
							String rocDocName = loadCompletionModel.getLoad_number() + "_roc.pdf"; // You can format this name
																								// as needed
							
							// Upload to Google Drive
							try {
								
								String fileId = GoogleDriveService.findFileIdInFolder(rocDocName,
										subFolderId);

	//							File savedFile = targetPath.toFile();
								String yearFolderIdForNoLog = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue()+"_NoLog",
										googleDriveRootFolderId);
								String monthFolderIdNoLog = GoogleDriveService.getOrCreateFolder(month, yearFolderIdForNoLog);
								String driverFolderIdmonthFolderIdNoLog = GoogleDriveService.getOrCreateFolder(loadCompletionModel.getDriver_name().trim(),
										monthFolderIdNoLog);
								String subFolderIdNoLog = GoogleDriveService.getOrCreateFolder(sub_folder_name_for_roc,
										driverFolderIdmonthFolderIdNoLog);
//									
//								String newSubFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name,
//										driverFolderId);
								if (fileId != null) {
									GoogleDriveService.moveFileToFolder(fileId, subFolderIdNoLog,
											rocDocName);
								} else {
									logger.error("File not found in old driver's folder: {}",
											rocDocName);
								}

								// GoogleDriveService.moveFileToFolder(fileId,
								// newSubFolderId,loadStatusModel.getFile_name());

							} catch (IOException ioEx) {
								System.err.println("Google Drive upload error: " + ioEx.getMessage());
								ioEx.printStackTrace();
								return;
							}
//							String newFileNameWithoutExtension = CommonController
//									.getNewFileNameWithoutExtension(newFileName);

							// Insert document record into DB
							try {
								Object[] param_for_document_update = {
										//loadCompletionModel.getDriver_documents_id(),
//										CommonController.getNewFileNameWithoutExtension(newFileName), //p_document_name
//										newFileName,//p_original_document_name with extension
//										targetFolder, //p_document_path
										2, //p_sub_folder_id
										loadCompletionModel.getDriver_id(),
										loadCompletionModel.getLoad_number()
										};
							
								dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_driver_document,
										param_for_document_update);
							} catch (Exception dbEx) {
								System.err.println("DB error while inserting document: " + dbEx.getMessage());
								dbEx.printStackTrace();
							}

						} catch (Exception ex) {
							System.err.println("Unexpected error in async process: " + ex.getMessage());
							ex.printStackTrace();
						}
					});

					  logger.info("loadCompletionModel: Moving File to No Log End For Drive");
				    
				}

				
					

				// Create directories if they do not exist
				File folder = new File(targetFolder);
				if (!folder.exists()) {
					folder.mkdirs();
				}

				// Save the file to the folder
				source_path_for_roc = driverFolder + "/" + sub_folder_name_for_roc + "/"
						+ loadCompletionModel.getLoad_number() + "_roc.pdf";
				source_path_for_pod = targetFolder + "/" + newFileName;
				logger.info("source_path_for_roc: " + source_path_for_roc);
				logger.info("source_path_for_pod: " + source_path_for_pod);
				File savedFile = new File(targetFolder + "/" + newFileName);
				loadCompletionModel.getPod().transferTo(savedFile);
				logger.info("LoadRepository : markLoadComplete document insert end local");
				logger.info("LoadRepository : markLoadComplete document insert int DataBase Start");
				// Insert document details into the database
				Object[] param = { 
						newFileNameWithoutExtension, 
						newFileName,
						targetFolder,
						6L,
						Integer.parseInt(loadCompletionModel.getDriver_id()),
						loadCompletionModel.getLoad_number()
						};
				logger.info("vakue of params: " + Arrays.toString(param));

				int documentId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document, param);

				logger.info("LoadRepository : markLoadComplete document insert int DataBase end");
				CompletableFuture.runAsync(() -> {
					try {
						/*NOW moving the fiel form log to no log folder start**/
						logger.info("LoadRepository : markLoadComplete document moving from Log to NoLog Google Drive start");
						
						logger.info("LoadRepository : markLoadComplete document moving from Log to NoLog Google Drive end");
						
						logger.info("LoadRepository : markLoadComplete document insert Google Drive start");
						String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
						String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
								googleDriveRootFolderId);
						String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
						String driverFolderId = GoogleDriveService
								.getOrCreateFolder(loadCompletionModel.getDriver_name().trim(), monthFolderId);
						String subFolderId = GoogleDriveService.getOrCreateFolder(PropertiesReader
								.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE").trim(),
								driverFolderId);

						MultipartFile convertFileToMultipartFile = CommonController
								.convertFileToMultipartFile(savedFile);
						GoogleDriveService.uploadFileToDrive(convertFileToMultipartFile, subFolderId);

						logger.info("LoadRepository : markLoadComplete document insert Google Drive end");
					} catch (Exception e) {
						logger.error("Error while uploading file to Google Drive: " + e.getMessage());
					}
				});

			}
			int userID = commonController.getUserDtoDataFromToken(request);
			System.out.println("userID: "+userID);
			
			Long invoice_number = dbContextserviceBms.QueryToFirstWithLong(QueryMaster.get_next_invoice_number, null);
			
			if (load_complition_status == STATUS_COMPLETED) {
				if (loadCompletionModel.getPod() != null) {
					logger.info("LoadRepository : markLoadComplete document insert start local");
					/*
					 * now here we will start the merging of the document lets make the call to get
					 * the invoice number
					 */
					logger.info("LoadRepository : markLoadComplete get invoice number start");

					logger.info("LoadRepository : markLoadComplete get invoice number end : " + invoice_number);
					logger.info("LoadRepository : markLoadComplete get company details start");
					Object params[] = { loadCompletionModel.getCompany_id() };
					CompanyDetailsDto companyDto = dbContextserviceBms.QueryToFirstWithParam(
							QueryMaster.get_company_details_by_id, params, CompanyDetailsDto.class);
					logger.info("LoadRepository : markLoadComplete get company details end");
					logger.info("LoadRepository : markLoadComplete generateInvoicePdf start");
					CommonUtility.generateInvoicePdf(companyDto, invoice_number, loadCompletionModel.getAmount(),
							loadCompletionModel.getLoad_number());
					logger.info("LoadRepository : markLoadComplete generateInvoicePdf end");
					logger.info("LoadRepository : markLoadComplete mergePDFDocuments start");
					String invoice = "D:\\NAGI_GROUP\\invoice-sample\\invoice_step2.pdf";
					invoice = "C:\\NAGI_GROUP\\invoice\\" + loadCompletionModel.getLoad_number() + "_invoice.pdf";
					List<String> paths = new ArrayList<String>();
					paths.add(invoice);
					paths.add(source_path_for_roc);
					paths.add(source_path_for_pod);
					String invoice_file_path = "C:\\NAGI_GROUP\\INVOICES\\" + loadCompletionModel.getLoad_number()
							+ "\\" + loadCompletionModel.getLoad_number() + ".pdf";
					CommonUtility.mergePDFDocuments(paths, invoice_file_path);
					logger.info("LoadRepository : markLoadComplete mergePDFDocuments end");
					logger.info("LoadRepository : markLoadComplete mail sending start");
					InvoiceMailModel invoiceMailModel = new InvoiceMailModel();
					invoiceMailModel.setLoad_number(loadCompletionModel.getLoad_number());
					invoiceMailModel.setAttachment_path(invoice_file_path);
					invoiceMailModel.setCompany_mail_id(companyDto.getEmail().trim());
					List<File> attachments = new ArrayList<>();
					ArrayList<String> filePaths = new ArrayList<String>();
					filePaths.add(invoice_file_path);
					for (String path : filePaths) {
						File file = new File(path);
						if (file.exists()) {
							attachments.add(file);
						} else {
							System.out.println("File not found: " + path);
						}
					}
					
					invoiceMailModel.setAttachments(attachments);
					CompletableFuture.runAsync(() -> {
						Boolean isMailSent = mailService.sendInvoiceEmailWithAttachment(invoiceMailModel);
						// whatsappService.sendFinanceRequestInsertWp(financeMailModel);
						if (isMailSent) {
							logger.info("LoadRepository : markLoadComplete insert_invoice start");
							
							logger.info("commonController.getUserDtoDataFromToken(request): "+commonController.getUserDtoDataFromToken(request));
							System.out.println("userID 01: "+userID);
							
							Object[] invoice_insert_param = { 
									invoice_number, loadCompletionModel.getCompany_id(),
									loadCompletionModel.getLoad_number(),
									true,
									Integer.parseInt(loadCompletionModel.getDriver_id()),
									userID,

							};
							int documentId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_invoice,
									invoice_insert_param);

							logger.info("LoadRepository : markLoadComplete insert_invoice end: "+documentId);
						}

					}).thenAccept(resultMail -> {//
						logger.info("LoadRepository : Load response mail end completed");
					});

				}
				logger.info("LoadRepository : markLoadComplete mail sending end");
				logger.info("LoadRepository : markLoadComplete end");
				logger.info("LoadRepository : markLoadComplete : with status : " + load_complition_status);

				return new ApiResponse<Integer>(true, "Load Mark as Completed", true, 1, 1);

			} else {
				
				logger.info("LoadRepository : markLoadComplete : with status else : " + load_complition_status);
				Object[] invoice_insert_param = { 
						invoice_number, 
						loadCompletionModel.getCompany_id(),
						loadCompletionModel.getLoad_number(),
						false,
						Integer.parseInt(loadCompletionModel.getDriver_id()),
						userID,

				};
				
				int documentId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_invoice,
						invoice_insert_param);

				return new ApiResponse<Integer>(true,
						"Load Mark as Completed,please request for the accessorial charges", true, 1, 1);
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.info("LoadRepository : markLoadComplete : error " + e.getMessage());
			e.printStackTrace();
			return new ApiResponse<Integer>(false, "Something went wrong while marking the load as complete", false, 0,
					0);
		}

	}

//	@Override
	public ApiResponse<Integer> saveDocument(LoadCompletionModel loadCompletionModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			LocalDate currentDate = LocalDate.now();
			String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			String yearFolder = rootFolder + "_" + Year.now().getValue(); // Example: "documents_2025"

			String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ"; // Your Google Drive root folder ID
			String yearFolderId = GoogleDriveService
					.getOrCreateFolder("year" + "_" + String.valueOf(Year.now().getValue()), googleDriveRootFolderId);
			String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
			String driverFolderId = GoogleDriveService.getOrCreateFolder(loadCompletionModel.getDriver_name().trim(),
					monthFolderId);
			String subFolderId = GoogleDriveService.getOrCreateFolder(
					PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE").trim(),
					driverFolderId);
			logger.info("subFolderId: " + subFolderId);

			GoogleDriveService.uploadFileToDrive(loadCompletionModel.getPod(), subFolderId);

			String newFileName = loadCompletionModel.getLoad_number() + "_POD";
			newFileName = CommonController.renameFileWithExtension(loadCompletionModel.getPod(), newFileName);

			// String month = currentDate.getMonth().toString(); // Example: "FEBRUARY"

			String driverFolder = yearFolder + "/" + month + "/" + loadCompletionModel.getDriver_name();
			String targetFolder = driverFolder + "/"
					+ PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE");
			;
			logger.info("targetFolder: " + targetFolder);
			// Create directories if they do not exist
			File folder = new File(targetFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			logger.info("targetFolder +newFileName:" + targetFolder + "/" + newFileName);
			// Save the file to the folder
			File savedFile = new File(targetFolder + "/" + newFileName);
			loadCompletionModel.getPod().transferTo(savedFile);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public ApiResponse<List<LoadDto>> getLoadByStatusId(int status_id) {

		List<LoadDto> loadDtos = null;
		try {
			logger.info("LoadRepository : getLoadByStatusId Start");
			Object param[] = { status_id };
			loadDtos = dbContextserviceBms.QueryToListWithParam(QueryMaster.get_loads_by_status, param, LoadDto.class);
			logger.info("LoadRepository : getLoadByStatusId end");
			return new ApiResponse<List<LoadDto>>(true, "Total Record " + loadDtos.size() + " ", true, loadDtos,
					loadDtos.size());

		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : getLoadByStatusId :", e);
			e.printStackTrace();
			return new ApiResponse<List<LoadDto>>(false, e.getMessage(), false, null, 0);
		}
	}

	public ApiResponse<LoadStatusSummaryDto> getLoadCountAsPerTheStatus() {
		// TODO Auto-generated method stub
		logger.info("LoadRepository : getLoadById Start");
		LoadStatusSummaryDto loadStatusSummaryDto = null;
		try {

			loadStatusSummaryDto = dbContextserviceBms.QueryToFirst(QueryMaster.get_load_status_summary,
					LoadStatusSummaryDto.class);
			logger.info("LoadRepository : getLoadById End");
			return new ApiResponse<LoadStatusSummaryDto>(true, "Total Record " + 1 + " ", true, loadStatusSummaryDto,
					1);
		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : getLoadById :", e);
			return new ApiResponse<LoadStatusSummaryDto>(false, "No record found", false, null, 0);
		}
	}

	public ApiResponse<List<CompanyNameDto>> getAllCompanyName() {

		List<CompanyNameDto> companyDto = null;
		try {
			logger.info("LoadRepository : geAlltLoads Start");
			companyDto = dbContextserviceBms.QueryToList(QueryMaster.get_company_list, CompanyNameDto.class);
			logger.info("LoadRepository : geAlltLoads Start");
			return new ApiResponse<List<CompanyNameDto>>(true, "Total Record " + companyDto.size() + " ", true,
					companyDto, companyDto.size());

		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : geAlltLoads :", e);
			return new ApiResponse<List<CompanyNameDto>>(false, e.getMessage(), false, null, 0);
		}
	}

	public ApiResponse<LoadAssignmentDocumentDto> getLoadDetailsForAssignment(int load_id, String load_number) {

		// TODO Auto-generated method stub
		logger.info("LoadRepository : getLoadDetailsForAssignment Start");
		LoadAssignmentDocumentDto loadAssignmentDocumentDto = null;
		try {
			Object params[] = { load_id, load_number };
			loadAssignmentDocumentDto = dbContextserviceBms.QueryToFirstWithParam(
					QueryMaster.get_load_details_for_driver_assignment, params, LoadAssignmentDocumentDto.class);
			logger.info("LoadRepository : getLoadDetailsForAssignment End");
			return new ApiResponse<LoadAssignmentDocumentDto>(true, "Total Record " + 1 + " ", true,
					loadAssignmentDocumentDto, 1);
		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : getLoadDetailsForAssignment :", e.getMessage());
			e.printStackTrace();
			return new ApiResponse<LoadAssignmentDocumentDto>(false, "No record found", false, null, 0);
		}

	}

	public ApiResponse<Integer> requestToInvoice(LoadAdditionalCharges loadAdditionalCharges,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			String source_path_for_roc = "";
			String source_path_for_pod = "";
			String sub_folder_name_for_roc = PropertiesReader.getProperty("constant",
					"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
			String sub_folder_name_for_pod = PropertiesReader.getProperty("constant",
					"BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE");
			CommonController commonController = new CommonController();

			loadAdditionalCharges.setAmount(CommonUtility.getFinalRate(loadAdditionalCharges));
			logger.info("Final Amunt : " + loadAdditionalCharges.getAmount());
			logger.info("LoadRepository : requestToInvoice  start");
			logger.info("LoadRepository : update_load_with_charges_and_invoice  start");
			Object[] params = { loadAdditionalCharges.getLoad_id(), 
					commonController.getUserDtoDataFromToken(request),
					loadAdditionalCharges.getLumper_value(),
					loadAdditionalCharges.getDetention_value(),
					loadAdditionalCharges.getScale_value(),
					loadAdditionalCharges.getExtra_stop_charge(),
					loadAdditionalCharges.getTrailer_wash(),
					loadAdditionalCharges.getLayover(), };
			int load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_with_charges_and_invoice,
					params);

			logger.info("LoadRepository : requestToInvoice update_load_with_charges_and_invoice end");
			Object load_status_param[] = { loadAdditionalCharges.getLoad_number() };
			int load_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,
					load_status_param);
			if (load_status == STATUS_COMPLETED && load_id > 0) {
				/* Now we will replace the file in the destination */
				logger.info("LoadRepository : requestToInvoice File uploding start");
				String sub_folder_name = PropertiesReader.getProperty("constant",
						"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
				LocalDate currentDate = LocalDate.now();
				String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
				String yearFolder = rootFolder + "_" + Year.now().getValue();
				String driverFolder = yearFolder + "/" + month + "/" + loadAdditionalCharges.getDriver_name();

				// Google Drive Folder Setup
				String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
				String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
						googleDriveRootFolderId);
				String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
				String driverFolderId = GoogleDriveService
						.getOrCreateFolder(loadAdditionalCharges.getDriver_name().trim(), monthFolderId);

				String newFileName = CommonController.renameFileWithExtension(loadAdditionalCharges.getRoc(),
						loadAdditionalCharges.getLoad_number() + "_roc");
				// Read file data BEFORE the async block
				byte[] fileBytes = loadAdditionalCharges.getRoc().getBytes(); // ✅ Safe: temp file still exists
				String originalFileName = loadAdditionalCharges.getRoc().getOriginalFilename();
				String targetFolder = driverFolder + "/" + sub_folder_name;
				File folder = new File(targetFolder);
				if (!folder.exists()) {
					folder.mkdirs();
				}

				String newFilePath = targetFolder + "/" + newFileName;
				logger.info("FilePath: " + newFilePath);
				File savedFile = new File(newFilePath);

				source_path_for_roc = driverFolder + "/" + sub_folder_name_for_roc + "/" + newFileName;
				source_path_for_pod = driverFolder + "/" + sub_folder_name_for_pod + "/"
						+ loadAdditionalCharges.getLoad_number() + "_pod.pdf";
				// loadModel.getRoc().transferTo(savedFile);
				logger.info("LoadRepository : requestToInvoice File storing local start");
				try (FileOutputStream fos = new FileOutputStream(savedFile)) {
					fos.write(fileBytes); // ✅ Writes from in-memory bytes
				}
				logger.info("LoadRepository : requestToInvoice File storing local end");
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					try {

						// Upload to Google Drive
						try {
							String subFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name, driverFolderId);
							MultipartFile multipartFile = CommonController.convertFileToMultipartFile(savedFile);
							GoogleDriveService.uploadFileToDriveWithReplace(multipartFile, newFileName, subFolderId);

						} catch (IOException ioEx) {
							// Handle file conversion or upload errors
							logger.info("Failed during Google Drive operations: " + ioEx.getMessage());
							ioEx.printStackTrace();
							return;
						} catch (Exception driveEx) {
							logger.info("Unexpected error during Google Drive upload: " + driveEx.getMessage());
							driveEx.printStackTrace();
							return;
						}

					} catch (Exception ex) {
						System.err.println("Unexpected error in async process: " + ex.getMessage());
						ex.printStackTrace();
					}
				});

				logger.info("LoadRepository : requestToInvoice File uploding end");

				Long invoice_number = dbContextserviceBms.QueryToFirstWithLong(QueryMaster.get_next_invoice_number,
						null);
				if (load_status == STATUS_COMPLETED && load_id > 0) {
					if (loadAdditionalCharges.getRoc() != null) {
						logger.info("LoadRepository : requestToInvoice document insert start local");
						/*
						 * now here we will start the merging of the document lets make the call to get
						 * the invoice number
						 */
						logger.info("LoadRepository : requestToInvoice get invoice number start");

						logger.info("LoadRepository : requestToInvoice get invoice number end : " + invoice_number);
						logger.info("LoadRepository : requestToInvoice get company details start");
						Object params_for_company_dto[] = { loadAdditionalCharges.getCompany_id() };
						CompanyDetailsDto companyDto = dbContextserviceBms.QueryToFirstWithParam(
								QueryMaster.get_company_details_by_id, params_for_company_dto, CompanyDetailsDto.class);
						logger.info("LoadRepository : requestToInvoice get company details end");
						logger.info("LoadRepository : requestToInvoice generateInvoicePdf start");
						CommonUtility.generateInvoicePdf(companyDto, invoice_number, loadAdditionalCharges.getAmount(),
								loadAdditionalCharges.getLoad_number());
						logger.info("LoadRepository : requestToInvoice generateInvoicePdf end");
						logger.info("LoadRepository : requestToInvoice mergePDFDocuments start");
						String invoice = "D:\\NAGI_GROUP\\invoice-sample\\invoice_step2.pdf";
						invoice = "D:\\NAGI_GROUP\\invoice\\" + loadAdditionalCharges.getLoad_number() + "_invoice.pdf";
						List<String> paths = new ArrayList<String>();
						paths.add(invoice);
						paths.add(source_path_for_roc);
						paths.add(source_path_for_pod);
						String invoice_file_path = "D:\\NAGI_GROUP\\INVOICES\\" + loadAdditionalCharges.getLoad_number()
								+ "\\" + loadAdditionalCharges.getLoad_number() + ".pdf";
						CommonUtility.mergePDFDocuments(paths, invoice_file_path);
						logger.info("LoadRepository : requestToInvoice mergePDFDocuments end");
						logger.info("LoadRepository : requestToInvoice mail sending start");
						InvoiceMailModel invoiceMailModel = new InvoiceMailModel();
						invoiceMailModel.setLoad_number(loadAdditionalCharges.getLoad_number());
						invoiceMailModel.setAttachment_path(invoice_file_path);
						invoiceMailModel.setCompany_mail_id(companyDto.getEmail().trim());
						List<File> attachments = new ArrayList<>();
						ArrayList<String> filePaths = new ArrayList<String>();
						filePaths.add(invoice_file_path);
						for (String path : filePaths) {
							File file = new File(path);
							if (file.exists()) {
								attachments.add(file);
							} else {
								System.out.println("File not found: " + path);
							}
						}
						invoiceMailModel.setAttachments(attachments);
						CompletableFuture.runAsync(() -> {
							Boolean isMailSent = mailService.sendInvoiceEmailWithAttachment(invoiceMailModel);
							// whatsappService.sendFinanceRequestInsertWp(financeMailModel);
							if (isMailSent) {

								logger.info("LoadRepository : requestToInvoice mail sent successfully");
							}

						}).thenAccept(resultMail -> {//
							logger.info("LoadRepository : Load response mail end completed");
						});

					}
					logger.info("LoadRepository : markLoadComplete mail sending end");
					logger.info("LoadRepository : markLoadComplete end");
					logger.info("LoadRepository : markLoadComplete : with status : ");

				}

			}

			logger.info("LoadRepository : requestToInvoice  end");
			return new ApiResponse<Integer>(true, "Load Mark as Completed", true, 1, 1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("LoadRepository : requestToInvoice  error: " + e.getMessage());
			return new ApiResponse<Integer>(false, "Something went wrong while marking the load as complete", false, 0,
					0);
		}

	}

	public ApiResponse<Integer> cancelLoad(CancelLoadModel cancelLoadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			/*
			 *1]Cancel without TONU SELECT cancel_load_with_tonu(101, FALSE, NULL, NULL, 1);
			 *
			 *2]Cancel with TONU, charges unknown SELECT cancel_load_with_tonu(103, TRUE,NULL, 22447, 1);
			 * 
			 *3]Cancel with TONU, charges known SELECT cancel_load_with_tonu(102, TRUE,200, 22446, 1);
			 * 
			 */
			
			CommonController commonController = new CommonController();
			Object[] params;
			boolean deleteFileFromLocalAndDrive = false;
			logger.info("LoadRepository : cancelLoad : cancelLoadModel: "+cancelLoadModel);
			Double charges = cancelLoadModel.getTonu_charges();
             //1)cancel load no tonu
			if (cancelLoadModel.isTonu()==false && (charges==null || charges.equals(0.0))) {


	            String source_path_for_roc = "";
				String sub_folder_name_for_roc = PropertiesReader.getProperty("constant",
						"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
				LocalDate currentDate = LocalDate.now();
				String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
				String yearFolder = rootFolder + "_" + Year.now().getValue();
				String driverFolder = yearFolder + "/" + month + "/" + cancelLoadModel.getDriver_name();

				// Google Drive Folder Setup
				String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
				String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
						googleDriveRootFolderId);
				String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
				String driverFolderId = GoogleDriveService
						.getOrCreateFolder(cancelLoadModel.getDriver_name().trim(), monthFolderId);

				
				String targetFolder = driverFolder + "/" + sub_folder_name_for_roc;
				String filePath = targetFolder + "/" +cancelLoadModel.getOld_load_number()+"_roc.pdf";
				
				
				logger.info("LoadRepository : requestToInvoice File storing local end");

				CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
				    try {
				        String subFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name_for_roc, driverFolderId);
				        return GoogleDriveService.deleteFileFromLocalAndDrive(
				            filePath,
				            subFolderId,
				            cancelLoadModel.getOld_load_number() + "_roc.pdf"
				        );
				    } catch (Exception ex) {
				        logger.error("Error during async Google Drive deletion", ex);
				        ex.printStackTrace();
				        return false;
				    }
				});

				try {
				    // Wait for the CompletableFuture to complete and get the result
				     deleteFileFromLocalAndDrive = future.get();  // This blocks until the task is done

				    if (deleteFileFromLocalAndDrive) {
				        // Proceed with DB updates
				         params = new Object[] {
				            cancelLoadModel.getLoad_id(),
				            cancelLoadModel.isTonu(),
				            commonController.getUserDtoDataFromToken(request),
				            (cancelLoadModel.getTonu_charges() == null || cancelLoadModel.getTonu_charges() == 0) ? null : cancelLoadModel.getTonu_charges(),
				            null
				        };

				        int load_id = dbContextserviceBms.QueryToFirstWithInt(
				            QueryMaster.cancel_load,
				            params
				        );

				        Object[] param_for_doc_delete = {
				            cancelLoadModel.getOld_load_number(),
				        };

				        dbContextserviceBms.QueryToFirstWithInt(
				            QueryMaster.mark_documents_as_deleted,
				            param_for_doc_delete
				        );
				        return new ApiResponse<Integer>(true, "Load Mark as Cancelled", true, 1, 1);
				    } else {
				        logger.warn("File deletion failed. Skipping DB update.");
				        return new ApiResponse<Integer>(false, "Load Mark as Cancelled", false, 0, 0);
				    }

				} catch (InterruptedException | ExecutionException e) {
				    logger.error("Async task failed", e);
				    // Handle fallback if needed
				    return new ApiResponse<Integer>(false, "Load Mark as Cancelled", false, 0, 0);
				}

			    
			    
			}
			//2)cancel load with tonu charges unknown
			if (cancelLoadModel.isTonu() && (charges==null || charges.equals(0.0))) {
				logger.info("LoadRepository : cancelLoad : with tonu start");
				Long invoice_number = dbContextserviceBms.QueryToFirstWithLong(QueryMaster.get_next_invoice_number,
						null);
			    params = new Object[] {
			        cancelLoadModel.getLoad_id(),
			        cancelLoadModel.isTonu(),//true
			        commonController.getUserDtoDataFromToken(request),
			        (cancelLoadModel.getTonu_charges() == null || cancelLoadModel.getTonu_charges().equals(0.0)) 
			        ? null : cancelLoadModel.getTonu_charges(),
			        invoice_number  //invoice number as null 
			    };

			    int load_id = dbContextserviceBms.QueryToFirstWithInt(
			        QueryMaster.cancel_load,
			        params
			    );
			    
			    if(load_id!=0) {
			    	logger.info("LoadRepository : cancelLoad : with tonu end");
			    	return new ApiResponse<Integer>(true, "Load Mark as Cancelled with tonu", true, 1, 1);
			    }
			    else {
			    	logger.error("LoadRepository : cancelLoad : with tonu error");
			    	return new ApiResponse<Integer>(false, "Something went wron while cancelling the load with tonu", false,0 , 0);
			    }
			}
			//3)cancel load with tonu charges known
			if (cancelLoadModel.isTonu() && charges!=null && !charges.equals(0.0)) {
				
				//3]Cancel with TONU, charges known SELECT cancel_load_with_tonu(102, TRUE,200, 22446, 1);
				cancelLoadModel.setRequesting_user(commonController.getUserDtoDataFromToken(request));
				Integer data = requestToInvoiceForTonu(cancelLoadModel, request).Data;
			    if(data!=0) {
			    	return new ApiResponse<Integer>(true, "Load Mark as Cancelled With the tonu Chrages", true, 1, 1);
			    }
			    else {
			    	
			    } return new ApiResponse<Integer>(true, "Something went wron while cancelling the load with the tonu charges", true, 1, 1);
			    
			}
			else {
				return new ApiResponse<Integer>(false, "Something went wron while cancelling the load", false, 0, 0);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("LoadRepository : requestToInvoice  error: " + e.getMessage());
			return new ApiResponse<Integer>(false, "Something went wrong while marking the load as complete", false, 0,
					0);
		}

	}

	public ApiResponse<Integer> requestToInvoiceForTonu(CancelLoadModel cancelLoadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			/*already the document will be stored in the driver path*/
			String source_path_for_roc = "";
			String sub_folder_name_for_roc = PropertiesReader.getProperty("constant",
					"BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
			LocalDate currentDate = LocalDate.now();
			String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			String yearFolder = rootFolder + "_" + Year.now().getValue();
			String driverFolder = yearFolder + "/" + month + "/" + cancelLoadModel.getDriver_name();

			// Google Drive Folder Setup
			String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
			String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(),
					googleDriveRootFolderId);
			String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
			String driverFolderId = GoogleDriveService
					.getOrCreateFolder(cancelLoadModel.getDriver_name().trim(), monthFolderId);

			String newFileName = CommonController.renameFileWithExtension(cancelLoadModel.getRoc(),
					cancelLoadModel.getNew_load_number() + "_roc");
			byte[] fileBytes = cancelLoadModel.getRoc().getBytes(); // ✅ Safe: temp file still exists
			String targetFolder = driverFolder + "/" + sub_folder_name_for_roc;
			File folder = new File(targetFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			String oldFilePath = targetFolder + "/" +cancelLoadModel.getOld_load_number()+"_roc.pdf";
			File oldSavedFile = new File(oldFilePath);

			// ✅ If file already exists locally, delete it
			if (oldSavedFile.exists()) {
			    logger.info("Existing local file found. Deleting: " + oldFilePath);
			    oldSavedFile.delete();
			}
			String newFilePath = targetFolder + "/" + newFileName;
			logger.info("FilePath: " + newFilePath);
			File savedFile = new File(newFilePath);
			source_path_for_roc = driverFolder + "/" + sub_folder_name_for_roc + "/" + newFileName;
			logger.info("LoadRepository : requestToInvoice File storing local start");
			try (FileOutputStream fos = new FileOutputStream(savedFile)) {
				fos.write(fileBytes); // ✅ Writes from in-memory bytes
			}
			logger.info("LoadRepository : requestToInvoice File storing local end");
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				try {

					// Upload to Google Drive
					try {
						String subFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name_for_roc, driverFolderId);
						MultipartFile multipartFile = CommonController.convertFileToMultipartFile(savedFile);
						GoogleDriveService.deleteAndUploadFileToDrive(multipartFile, newFileName, subFolderId,cancelLoadModel.getOld_load_number() + "_roc.pdf");

					} catch (IOException ioEx) {
						// Handle file conversion or upload errors
						logger.info("Failed during Google Drive operations: " + ioEx.getMessage());
						ioEx.printStackTrace();
						return;
					} catch (Exception driveEx) {
						logger.info("Unexpected error during Google Drive upload: " + driveEx.getMessage());
						driveEx.printStackTrace();
						return;
					}

				} catch (Exception ex) {
					System.err.println("Unexpected error in async process: " + ex.getMessage());
					ex.printStackTrace();
				}
			});
			logger.info("LoadRepository : requestToInvoice File uploding end");
			Object[] params_to_get_invoice_number = {cancelLoadModel.getOld_load_number()};		
			Long invoice_number = dbContextserviceBms.QueryToFirstWithLong(QueryMaster.get_invoice_number_by_load_number,
					params_to_get_invoice_number);
			
			 invoice_number = dbContextserviceBms.QueryToFirstWithLong(QueryMaster.get_next_invoice_number, null);
			

			if (cancelLoadModel.getRoc() != null) {
				logger.info("LoadRepository : requestToInvoice document insert start local");
				/*	
				 * now here we will start the merging of the document lets make the call to get
				 * the invoice number
				 */
				logger.info("LoadRepository : requestToInvoice get invoice number start");

				logger.info("LoadRepository : requestToInvoice get invoice number end : " + invoice_number);
				logger.info("LoadRepository : requestToInvoice get company details start");
				Object params_for_company_dto[] = { cancelLoadModel.getCompany_id() };
				CompanyDetailsDto companyDto = dbContextserviceBms.QueryToFirstWithParam(
						QueryMaster.get_company_details_by_id, params_for_company_dto, CompanyDetailsDto.class);
				logger.info("LoadRepository : requestToInvoice get company details end");
				logger.info("LoadRepository : requestToInvoice generateInvoicePdf start");
				CommonUtility.generateInvoicePdf(companyDto, invoice_number, cancelLoadModel.getTonu_charges(),
				cancelLoadModel.getNew_load_number());
				logger.info("LoadRepository : requestToInvoice generateInvoicePdf end");
				logger.info("LoadRepository : requestToInvoice mergePDFDocuments start");
				String invoice = "C:\\NAGI_GROUP\\invoice-sample\\invoice_step2.pdf";
				invoice = "C:\\NAGI_GROUP\\invoice\\" + cancelLoadModel.getNew_load_number() + "_invoice.pdf";
				List<String> paths = new ArrayList<String>();
				paths.add(invoice);
				paths.add(source_path_for_roc);
				
				String invoice_file_path = "C:\\NAGI_GROUP\\INVOICES\\" + cancelLoadModel.getNew_load_number()
						+ "\\" +cancelLoadModel.getNew_load_number() + ".pdf";
				CommonUtility.mergePDFDocuments(paths, invoice_file_path);
				logger.info("LoadRepository : requestToInvoice mergePDFDocuments end");
				logger.info("LoadRepository : requestToInvoice mail sending start");
				InvoiceMailModel invoiceMailModel = new InvoiceMailModel();
				invoiceMailModel.setLoad_number(cancelLoadModel.getNew_load_number());
				invoiceMailModel.setAttachment_path(invoice_file_path);
				invoiceMailModel.setCompany_mail_id(companyDto.getEmail().trim());
				invoiceMailModel.setTonu(cancelLoadModel.isTonu());
				List<File> attachments = new ArrayList<>();
				ArrayList<String> filePaths = new ArrayList<String>();
				filePaths.add(invoice_file_path);
				for (String path : filePaths) {
					File file = new File(path);
					if (file.exists()) {
						attachments.add(file);
					} else {
						System.out.println("File not found: " + path);
					}
				}
				invoiceMailModel.setAttachments(attachments);
				CompletableFuture<Boolean> mailFuture = CompletableFuture.supplyAsync(() -> {
					return  mailService.sendInvoiceEmailWithAttachment(invoiceMailModel);
					// whatsappService.sendFinanceRequestInsertWp(financeMailModel);
				
				});
				mailFuture.thenAccept(isMailSent -> {
				    if (isMailSent) {
				        logger.info("Mail sent successfully");

				        // ✅✅ Perform your final database insert here
				        
				        try {
				        	CommonController commonController = new CommonController();
				        	
				        	long invoice_number_id = dbContextserviceBms.QueryToFirstWithLong(QueryMaster.get_invoice_number_by_load_number,
									params_to_get_invoice_number);
							
				        	long invoice_numbers = dbContextserviceBms.QueryToFirstWithLong(QueryMaster.get_next_invoice_number, null);
							
				        	Object[] params_cancel_load_with_tonu =   {
				 			        cancelLoadModel.getLoad_id(),
				 			        cancelLoadModel.isTonu(),//false
				 			        commonController.getUserDtoDataFromToken(request)==0?cancelLoadModel.getRequesting_user():commonController.getUserDtoDataFromToken(request),
				 			        cancelLoadModel.getTonu_charges(),//null or 0
				 			       invoice_number_id==0?invoice_numbers:invoice_number_id  //invoice number as null 
				 			        
				 			    };

				 			    int load_id = dbContextserviceBms.QueryToFirstWithInt(
				 			        QueryMaster.cancel_load,
				 			       params_cancel_load_with_tonu
				 			    );
				 			   /*Function to update the */
				 			    if(cancelLoadModel.getOld_load_number()!=cancelLoadModel.getNew_load_number()) {
				 			    	System.out.println("load numbers are same");
				 			    	Object[] update_load_number =   {
						 			        cancelLoadModel.getLoad_id(),
						 			        cancelLoadModel.getNew_load_number(),
						 			        cancelLoadModel.getOld_load_number(),
						 			       commonController.getUserDtoDataFromToken(request)==0?cancelLoadModel.getRequesting_user():commonController.getUserDtoDataFromToken(request),   
						 			    };

						 			    int id = dbContextserviceBms.QueryToFirstWithInt(
						 			        QueryMaster.update_load_number_by_load_id,
						 			       update_load_number
						 			    );
				 			    }
				            logger.info("Invoice record inserted successfully.");
				        } catch (Exception dbEx) {
				            logger.error("Failed to insert invoice record: " + dbEx.getMessage(), dbEx);
				        }

				    } else {
				        logger.warn("Mail sending failed, not inserting record.");
				    }
				});

			}
			logger.info("LoadRepository : markLoadComplete mail sending end");
			logger.info("LoadRepository : markLoadComplete end");
			logger.info("LoadRepository : markLoadComplete : with status : ");

		
			return new ApiResponse<Integer>(true, "Load Mark as Completed", true, 1, 1);
			
		} catch (Exception e) {
			logger.error("LoadRepository : requestToInvoice  error at : "+e.getMessage());
			return  new ApiResponse<Integer>(false, "something went wrong", false, 0, 0);			
		}
		
	}

}
