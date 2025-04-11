package com.NagiGroup.repository;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.config.GoogleDriveService;
import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.companyDetails.CompanyDto;
import com.NagiGroup.dto.load.LoadDto;
import com.NagiGroup.model.load.LoadCompletionModel;
import com.NagiGroup.model.load.LoadModel;
import com.NagiGroup.model.load.LoadStatusModel;
import com.NagiGroup.model.load.LoadUpdateModel;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.utility.ApiResponse;
import com.NagiGroup.utility.CommonUtility;
import com.NagiGroup.utility.PropertiesReader;

@Repository
public class LoadRepository  {
	 private static final Logger logger = LoggerFactory.getLogger(LoadRepository.class);
	 public static String rootFolder = PropertiesReader.getProperty("constant", "BASEURL_FOR_YEAR");
		
	  public static final int STATUS_PENDING = 1;
	  public static final int STATUS_ASSIGNED = 2;
	  public static final int STATUS_IN_PROGRESS = 3;
	  public static final int STATUS_COMPLETED = 4;
	 private DbContextService dbContextserviceBms;
		public LoadRepository(DbContextService dbContextserviceBms){
			this.dbContextserviceBms=dbContextserviceBms;
		}
	 
//	@Override
	public ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		logger.info("LoadRepository : loadInsert start");
		CommonController commonController = new CommonController();
		int load_id=0;
		try {
			
			   int statusId = (loadModel.getAssign_to() != 0) ? 2 : 1; // 2 = Assigned, 1 = Pending
			   int previousStatus = (statusId == 2) ? 1 : 0; // If assigned at creation, previous was "Pending"
			
			   String pick_up_date_string = loadModel.getPick_up_date_string();
				String delievery_date_string = loadModel.getDelievery_date_string();
				//String earliest_time_arrival_string = loadModel.getEarliest_time_arrival_string();
			
				LocalDateTime pick_up_date_time = CommonUtility.parseDateString(pick_up_date_string);
				LocalDateTime delievery_date_time = CommonUtility.parseDateString(delievery_date_string);
				//LocalDateTime earliest_time_arrival = CommonUtility.parseDateString(earliest_time_arrival_string);
                System.out.println("loadModel: "+loadModel);
				logger.info("pick_up_date_time: " + pick_up_date_time);
				logger.info("delievery_date_time: " + delievery_date_time);
				//logger.info("earliest_time_arrival: " + earliest_time_arrival);
				logger.info("pick_up_date_string: " + pick_up_date_string);
				
				Object param[] = {
						loadModel.getLoadNumber(),//String
						loadModel.getSource(),//String
						loadModel.getDestination(),//String
						pick_up_date_time,//timestamp without time zone
						delievery_date_time,//timestamp without time zone
						loadModel.getDriver_id()!=0?loadModel.getDriver_id():0,//integer
						loadModel.getBase_price(),//double
						loadModel.getFinal_price(),	//double
						loadModel.getAssign_to()!=0?loadModel.getAssign_to():0,//integer
						statusId,//integer
						loadModel.getTrailer_used(),//integer
						commonController.getUserDtoDataFromToken(request),//integer
						loadModel.getCompany_id(),//String
						//earliest_time_arrival,//timestamp without time zone
						};
							
				load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_load, param);
				if (load_id != 0) {
					 logger.info("LoadRepository : load_id : " + load_id);
				    Object statusHistoryParam[] = {
				        load_id,   // Load ID
				        loadModel.getLoadNumber(),
				             // Previous status is always "Pending" (1)
				        statusId,  // New status (Pending or Assigned)
				        commonController.getUserDtoDataFromToken(request)
				    };
				    dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_load_status_history, statusHistoryParam);
				}
				if(load_id!=0 && loadModel.getRoc()!=null && loadModel.getAssign_to()==0) {
					 logger.info("LoadRepository : load_id : " + load_id);
			            /*file saving work
			             * now here i want to save the roc */
					 String fileNameWithoutExtension = CommonController.getFileNameWithoutExtension(loadModel.getRoc());
					 String newFileName = CommonController.renameFileWithExtension(loadModel.getRoc(),loadModel.getLoadNumber()+"_roc");
				      
					 String baseUrl = PropertiesReader.getProperty("constant", "BASEURL");
			            String folderName = PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD"+"/");
			            String 	basePathForDocument = baseUrl+PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD"+"/")+loadModel.getLoadNumber()+"/";
			        	boolean isCreated = CommonUtility.saveDocument(loadModel.getRoc(), loadModel.getLoadNumber(),
			        			baseUrl,folderName);
			        	if (isCreated) {
							String  fileName   = newFileName;
						    String	sourcePath = basePathForDocument + fileName;
						    System.out.println("loadNumber: "+loadModel.getLoadNumber());
						    System.out.println("fileNameWithoutExtension: "+CommonController.getNewFileNameWithoutExtension(newFileName));
						    System.out.println("basePathForDocument: "+basePathForDocument);
						    System.out.println("OriginalFilename: "+newFileName);
						    
						    System.out.println("File deleted successfully.");
				            Object loadDocumentParam[] = {				   
				            		loadModel.getLoadNumber(),
				            		CommonController.getNewFileNameWithoutExtension(newFileName),
				            		basePathForDocument,
				            		newFileName         		
				            };
				        	int document_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_load_document, loadDocumentParam);
				            if(document_id!=0) {
				            	logger.info("File details updated successfully.");
				            }
				            else {
				            	logger.info("Failed to update File details.");
				            }
									
						 logger.info("LoadRepository : sourcePath : " + sourcePath);
						}
				}
				else {
					String sub_folder_name = PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_DISPATCH_RECORD");
					LocalDate currentDate = LocalDate.now();
			        String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			        String yearFolder = rootFolder + "_" + Year.now().getValue();
			        String driverFolder = yearFolder + "/" + month + "/" + loadModel.getDriver_name();
			        
			        // Google Drive Folder Setup
			        String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
			        String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(), googleDriveRootFolderId);
			        String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
			        String driverFolderId = GoogleDriveService.getOrCreateFolder(loadModel.getDriver_name().trim().toLowerCase(), monthFolderId);
			        
			        String newFileName = CommonController.renameFileWithExtension(loadModel.getRoc(),loadModel.getLoadNumber()+"_roc");
		              

			        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			            try {
			                String targetFolder = driverFolder + "/" + sub_folder_name.toLowerCase();
			                File folder = new File(targetFolder);
			                if (!folder.exists()) {
			                    folder.mkdirs();
			                }

			                String newFilePath = targetFolder + "/" + newFileName;
			                System.out.println("newFilePath: "+newFilePath);
			                File savedFile = new File(newFilePath);
			                loadModel.getRoc().transferTo(savedFile);

			                // Upload to Google Drive
			                try {
			                    String subFolderId = GoogleDriveService.getOrCreateFolder(sub_folder_name.toLowerCase(), driverFolderId);
			                    MultipartFile multipartFile = CommonController.convertFileToMultipartFile(savedFile);
			                    GoogleDriveService.uploadFileToDrive(multipartFile, subFolderId);
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
			                        loadModel.getDriver_id()
			                    };
			                    dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document, param_for_document_insert);
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
					return new ApiResponse<Integer>(true, "Load added successfully",
							true, load_id, 1);
				} else {
					return new ApiResponse<Integer>(false, "Something went wrong while saving the data",
							true, load_id, 0);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
				logger.info("LoadRepository : Error at : loadInsert " + e);
				return new ApiResponse<Integer>(false, "Something went wrong while saving the data",
						false, load_id, 0);
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
			Object param[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getLoadNumber(),
					loadUpdateModel.getSource(),
					loadUpdateModel.getDestination(),
					pick_up_date_time,
					delievery_date_time,
					earliest_time_arrival,
					loadUpdateModel.getDriver_id(),
					loadUpdateModel.getBase_price(),
					loadUpdateModel.getFinal_price(),	
					loadUpdateModel.getAssign_to()!=0?loadUpdateModel.getAssign_to():0,
					commonController.getUserDtoDataFromToken(request)
					};
						
			load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_Update, param);

			if(load_id!=0 && loadUpdateModel.getRoc()!=null) {

				 logger.info("LoadRepository : load_id : " + load_id);
		            /*file saving work
		             * now here i want to save the roc */
		            String baseUrl = PropertiesReader.getProperty("constant", "BASEURL");
		            String folderName = PropertiesReader.getProperty("constant", "BASEURL_FOR_ROC");
		            String 	basePathForDocument = baseUrl+loadUpdateModel.getLoadNumber()+"/"+PropertiesReader.getProperty("constant", "BASEURL_FOR_ROC");
		        	boolean isCreated = CommonUtility.saveDocument(loadUpdateModel.getRoc(), loadUpdateModel.getLoadNumber(),
		        			baseUrl,folderName);
		        	if (isCreated) {
		        		
						String  fileName   = loadUpdateModel.getRoc().getOriginalFilename();
					    String	sourcePath = basePathForDocument + loadUpdateModel.getOldFileName();
					    boolean result = CommonUtility.deleteFile(sourcePath);
					    if (result) {
				            System.out.println("File deleted successfully.");
				            Object loadDocumentParam[] = {				   
				            		loadUpdateModel.getLoadNumber(),
				            		loadUpdateModel.getRoc().getOriginalFilename()         		
				            };
				        	int document_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_document_update, loadDocumentParam);
				            if(document_id!=0) {
				            	logger.info("File details updated successfully.");
				            }
				            else {
				            	logger.info("Failed to update File details.");
				            }
				        
				        } else {
				        	  logger.info("File deletion failed.");
				        }
					
					}
			
				
			}
            logger.info("LoadRepository : load_id : " + load_id);
            if (load_id != 0) {
				return new ApiResponse<Integer>(true, "Load updated successfully",
						true, load_id, 1);
			} else {
				return new ApiResponse<Integer>(false, "Something went wrong while updating  the load",
						true, load_id, 0);
			}
			
		} catch (Exception e) {
			 logger.info("LoadRepository : Error at : " + e);
			return new ApiResponse<Integer>(false, "Something went wrong while updating  the load",
					true, load_id, 0);
		}
	}

//	@Override
	public ApiResponse<List<LoadDto>> geAlltLoads() {
		
		List<LoadDto> loadDtos = null;
		try {
			logger.info("LoadRepository : geAlltLoads Start");
			loadDtos = dbContextserviceBms.QueryToList(QueryMaster.get_all_loads, LoadDto.class);
			logger.info("LoadRepository : geAlltLoads Start");
			return new ApiResponse<List<LoadDto>>(true, "Total Record " + loadDtos.size() + " ", true,
					loadDtos, loadDtos.size());
			
		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : geAlltLoads :" , e);
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
			loadDto = dbContextserviceBms.QueryToFirstWithParam(QueryMaster.get_load_by_id, params,
					LoadDto.class);
			logger.info("LoadRepository : getLoadById End");
			return new ApiResponse<LoadDto>(true, "Total Record " + 1 + " ", true, loadDto, 1);
		} catch (Exception e) {			
			logger.info("LoadRepository : Exception At : getLoadById :" , e);
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
			Object load_status_param[] = {
					loadUpdateModel.getLoadNumber()
	    	};
			latest_status_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,load_status_param);
			
			int current_status = latest_status_id;
			int updatedBy = commonController.getUserDtoDataFromToken(request);
			if (current_status == 4) {
	            return new ApiResponse<Integer>(false, "Cannot update a completed load",
		        		false, 0, 0);
			}
			Object reassigning_driver[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getAssign_to(),
					updatedBy
					
	    	};
			   // Reassign Driver (Allowed only in Pending or Assigned)
	        if (loadUpdateModel.getAssign_to() != 0 && (current_status == 1 || current_status == 2)) {
	             dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_driver, reassigning_driver);
	             isUpdated = true;
	        }
	        
	        Object update_sorce_pick_up[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getSource(),
					loadUpdateModel.getPick_up_date(),
					updatedBy
					
	    	};
	        // Update Source & Pickup Date/Time (Allowed only in Pending or Assigned)
	        if ((loadUpdateModel.getSource() != null || loadUpdateModel.getPick_up_date() != null) && (current_status == 1 || current_status == 2)) {
	              dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_source_pickup, update_sorce_pick_up);
	             isUpdated = true;
	        }
	        Object update_destination_delivery[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getDestination(),
					loadUpdateModel.getDelievery_date(),
					updatedBy
					
	    	};
	        
	        // Update Destination & Delivery Date/Time (Allowed in Pending, Assigned, or In Progress)
	        if ((loadUpdateModel.getDestination() != null || loadUpdateModel.getDelievery_date() != null) && (current_status != 4)) {
	        	 dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_destination_delivery,update_destination_delivery);
	        	isUpdated = true;
	        }

	    	Object update_final_price[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getFinal_price(),
					updatedBy
					
	    	};
	     // Update Final Price (Allowed in all except Completed)
	        if (loadUpdateModel.getFinal_price() != 0 && current_status != 4) {
	              dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_final_price, update_final_price);
	             isUpdated = true;
	        }
	        
	        Object update_base_price[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getBase_price(),
					updatedBy
					
	    	};
	     // Update Base Price (Allowed in all except Completed)
	        if (loadUpdateModel.getBase_price() != 0 && current_status != 4) {
	             dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_base_price,update_base_price);
	             isUpdated = true;
	             
	        }
	        
	        Object update_company_name[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getCompany_name(),
					updatedBy
					
	    	};
	        
	        // Update Company Name (Allowed in all except Completed)
	        if (loadUpdateModel.getCompany_name() != null && current_status != 4) {
	             dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_loads_company_name,update_company_name);
	             isUpdated = true;
	        }
	        Object update_trailer_used[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getTrailer_used(),
					updatedBy
					
	    	};
	        
	        if (loadUpdateModel.getTrailer_used() != null && current_status != 4) {
	              dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_trailer_used, update_trailer_used);
	             isUpdated = true;
	        }
	        Object update_loads_number[] = {
					loadUpdateModel.getLoad_id(),
					loadUpdateModel.getLoadNumber(),
					updatedBy
					
	    	};

	        // Update Load Number (Allowed in all except Completed)
	        if (loadUpdateModel.getLoadNumber() != null && current_status != 4) {
	              dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_number,update_loads_number);
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
	    logger.info("LoadRepository : assignLoad start");
	    CommonController commonController = new CommonController();
	    int updatedBy =0;
	    try {
	    	updatedBy = commonController.getUserDtoDataFromToken(request);
	    	Object load_status_param[] = {
	    			loadStatusModel.getLoad_number()
	    	};
			int previous_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,load_status_param);
			
		        // If load is neither PENDING nor ASSIGNED, it can't be reassigned
		        if (previous_status != STATUS_PENDING && previous_status != STATUS_ASSIGNED) {
		            return new ApiResponse<Integer>(false, "Load cannot be reassigned. Current status: " + previous_status, false, 0, 0);
		        }
	       
	        
	        // Insert into Status History
	        Object statusHistoryParam[] = 
	        	{ 
	        		loadStatusModel.getLoad_id(),
	        		STATUS_ASSIGNED,
	        		loadStatusModel.getDriver_id(), 
	        		updatedBy 
	        	};
	        int load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.update_load_status,statusHistoryParam);
	        
	        if(load_id!=0 && previous_status==1) {
	        	 logger.info("LoadRepository : assignLoad end");
	        	 return new ApiResponse<Integer>(true, "Load assigned successfully. Current status: Assigned" + 1,
	 	        		true, 1, 1);
	        }
	        if(load_id!=0 && previous_status==2) {
	        	 logger.info("LoadRepository : assignLoad end : success");
	        	 return new ApiResponse<Integer>(true, "Load re-assigned successfully. Current status: Assigned" + 1,
	 	        		true, 1, 1);
	        }
	        
	        else {
	        	logger.info("LoadRepository : assignLoad end : failed");
	        	 return new ApiResponse<Integer>(false, "Error updating load status: " + 0,
	 	        		false, 0, 0);
	        }
	             
	    } catch (Exception e) {
	        logger.error("Error in startLoad", e);
	        e.printStackTrace();
	        return new ApiResponse<Integer>(false, "Error while assigning load: " + 0,
	        		false, 0, 0);
	    }
	    
	}

//	@Override
	public ApiResponse<Integer> markLoadInProgress(LoadStatusModel loadStatusModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			logger.info("LoadRepository : markLoadInProgress end");
			 CommonController commonController = new CommonController();   
			 int updatedBy = commonController.getUserDtoDataFromToken(request);
	    	Object load_status_param[] = {
	    			loadStatusModel.getLoad_number()
	    	};
			int previous_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,load_status_param);
			
			if (previous_status != STATUS_ASSIGNED) {  
	            return new ApiResponse<Integer>(false, "Load cannot be started. Current status: Pending", false, 0, 0);
	        }
			Object in_progress_param[] = {
	    			loadStatusModel.getLoad_id(),
	    			updatedBy
	    			
	    	};
			int in_progress_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.mark_load_in_progress,in_progress_param);
			
			if(in_progress_status!=0) {
				logger.info("LoadRepository : markLoadInProgress end : success");
				return new ApiResponse<Integer>(true, "Load marked as in progress successfully. Current status: In-Progress" + 1,
	 	        		true, 1, 1);
			}else {
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
			
			logger.info("LoadRepository : markLoadComplete start");
			 CommonController commonController = new CommonController();   
			 int updatedBy = commonController.getUserDtoDataFromToken(request);
	    	Object load_status_param[] = {
	    			loadCompletionModel.getLoad_number()
	    	};
			int previous_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_load_status_id,load_status_param);
			
			if (previous_status != STATUS_IN_PROGRESS) {  
	            return new ApiResponse<Integer>(false, "Load cannot be Mark as Completed.As Current status is not In Progress", false, 0, 0);
	        }
			Object load_completion_param[] = {
					loadCompletionModel.getLoad_id(),
					loadCompletionModel.getLoad_number(),
	    			updatedBy,
	    			loadCompletionModel.getLumper_value(),
	    			loadCompletionModel.getLumper_paid_by(),
	    			loadCompletionModel.getDetention_value(),
	    			loadCompletionModel.getDetention_hours(),
	    			loadCompletionModel.getScale_value(),
	    			loadCompletionModel.getExtra_stop_charge(),
	    			loadCompletionModel.getTrailer_wash()
	    			
	    	};

			int load_complition_status = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.handle_load_completion,load_completion_param);
			if(load_complition_status==STATUS_COMPLETED) {
				/*1 we will save the pod to the required path 
				 * D:\NAGI_GROUP\YEAR_2025\MARCH\David\DISPATCH RECORD
				 * this is the path where we need to add the document
				 * here before adding the document we need to rename the document
				 * name to be like 12345_bol.pdf
				 * insert it locally 
				 * and on the cloud to 
				 * make a db call to 
				 * now we need to merge the pdf
				 * no of doc required invoice,roc,bol 
				 * now what is the data we will require to get the 
				 * */
				if(loadCompletionModel.getPod()!=null) {
					logger.info("LoadRepository : markLoadComplete document insert start local");
					String newFileName= loadCompletionModel.getLoad_number()+"_POD";	
					newFileName = CommonController.renameFileWithExtension(loadCompletionModel.getPod(),newFileName); 
					String newFileNameWithoutExtension = CommonController.getNewFileNameWithoutExtension(newFileName);
					LocalDate currentDate = LocalDate.now();
					    String month = currentDate.getMonth().toString(); // Example: "FEBRUARY"
					    String yearFolder = rootFolder + "_" + Year.now().getValue(); // Example: "documents_2025"

					    String driverFolder = yearFolder + "/" + month + "/" + loadCompletionModel.getDriver_name();
					    String targetFolder = driverFolder + "/" +PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE");;

			            // Create directories if they do not exist
			            File folder = new File(targetFolder);
			            if (!folder.exists()) {
			                folder.mkdirs();
			            }

			            // Save the file to the folder
			            File savedFile = new File(targetFolder + "/" +newFileName);
			            loadCompletionModel.getPod().transferTo(savedFile);
			            logger.info("LoadRepository : markLoadComplete document insert end local");
			            logger.info("LoadRepository : markLoadComplete document insert int DataBase Start");
			         // Insert document details into the database
			            Object[] param = {
			            	newFileNameWithoutExtension,
			                newFileName,
			                targetFolder,
			                6,
			                loadCompletionModel.getDriver_id()
			            };
			         int documentId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_document, param);
			         logger.info("LoadRepository : markLoadComplete document insert int DataBase end");
			         
//			         logger.info("LoadRepository : markLoadComplete document insert google drive start");	
//			         String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";  // Your Google Drive root folder ID
//			         String yearFolderId = GoogleDriveService.getOrCreateFolder("year"+"_"+String.valueOf(Year.now().getValue()), googleDriveRootFolderId);
//			         String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
//			         String driverFolderId = GoogleDriveService.getOrCreateFolder(loadCompletionModel.getDriver_name().trim().toLowerCase(), monthFolderId);
//			         String subFolderId = GoogleDriveService.getOrCreateFolder(PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE").trim().toLowerCase(), driverFolderId);
//	                 System.out.println("subFolderId: "+subFolderId);
//	                 MultipartFile convertFileToMultipartFile = CommonController.convertFileToMultipartFile(savedFile);
//			            
//	                 GoogleDriveService.uploadFileToDrive(convertFileToMultipartFile, subFolderId);
//	     			
//			         logger.info("LoadRepository : markLoadComplete document insert google drive start");	
			         CompletableFuture.runAsync(() -> {
			                try {
			                    logger.info("LoadRepository : markLoadComplete document insert Google Drive start");
			                    String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
			                    String yearFolderId = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(), googleDriveRootFolderId);
			                    String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
			                    String driverFolderId = GoogleDriveService.getOrCreateFolder(loadCompletionModel.getDriver_name().trim().toLowerCase(), monthFolderId);
			                    String subFolderId = GoogleDriveService.getOrCreateFolder(PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE").trim().toLowerCase(), driverFolderId);
			                    
			                    MultipartFile convertFileToMultipartFile = CommonController.convertFileToMultipartFile(savedFile);
			                    GoogleDriveService.uploadFileToDrive(convertFileToMultipartFile, subFolderId);
			                    
			                    logger.info("LoadRepository : markLoadComplete document insert Google Drive end");
			                } catch (Exception e) {
			                    logger.error("Error while uploading file to Google Drive: " + e.getMessage());
			                }
			            });		
			         
			         
			         /*now here we will start the merging of the document
			          * lets make the call to get the invoice number
			          * */
			         long invoice_number   = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.get_next_invoice_number,null);
			         Object params[]       = { loadCompletionModel.getCompany_id() };
					 CompanyDto companyDto = dbContextserviceBms.QueryToFirstWithParam(QueryMaster.get_company_details_by_id, params,
								             CompanyDto.class);					 
					 CommonUtility.generateInvoicePdf(companyDto, invoice_number, loadCompletionModel.getAmount(), loadCompletionModel.getLoad_number());
					 /*now we have all the three documents let merge them
					  * roc,bol and invoice are present at the respected path get them from the paths and merge all 
					  * tis document
					  *  will be present in the resp*/
					 String invoice = "D:\\NAGI_GROUP\\invoice-sample\\invoice_step2.pdf";
					 String roc = "D:\\NAGI_GROUP\\supporting-docs\\roc.pdf";
					 String bol = "D:\\NAGI_GROUP\\supporting-docs\\bol.pdf";
					 String finalPdf = "D:\\NAGI_GROUP\\final-invoices\\final_invoice_22446.pdf";
			         
			       	
				
				}
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

//	@Override
	public ApiResponse<Integer> saveDocument(LoadCompletionModel loadCompletionModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			  LocalDate currentDate = LocalDate.now();
		    String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);String yearFolder = rootFolder + "_" + Year.now().getValue(); // Example: "documents_2025"

		         String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";  // Your Google Drive root folder ID
		         String yearFolderId = GoogleDriveService.getOrCreateFolder("year"+"_"+String.valueOf(Year.now().getValue()), googleDriveRootFolderId);
		         String monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
		         String driverFolderId = GoogleDriveService.getOrCreateFolder(loadCompletionModel.getDriver_name().trim().toLowerCase(), monthFolderId);
		         String subFolderId = GoogleDriveService.getOrCreateFolder(PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE").trim().toLowerCase(), driverFolderId);
                 System.out.println("subFolderId: "+subFolderId);
		       
                 GoogleDriveService.uploadFileToDrive(loadCompletionModel.getPod(), subFolderId);
			
			String newFileName= loadCompletionModel.getLoad_number()+"_POD";	
			newFileName = CommonController.renameFileWithExtension(loadCompletionModel.getPod(),newFileName); 
			  
			   // String month = currentDate.getMonth().toString(); // Example: "FEBRUARY"
			    
			    String driverFolder = yearFolder + "/" + month + "/" + loadCompletionModel.getDriver_name();
			    String targetFolder = driverFolder + "/" +PropertiesReader.getProperty("constant", "BASEURL_FOR_SUB_FOLDER_POD_LUMPER_RECEIPT_SCALE");;
                System.out.println("targetFolder: "+targetFolder);
	            // Create directories if they do not exist
	            File folder = new File(targetFolder);
	            if (!folder.exists()) {
	                folder.mkdirs();
	            }
                System.out.println("targetFolder +newFileName:"+targetFolder+"/"+newFileName);
	            // Save the file to the folder
	            File savedFile = new File(targetFolder + "/" +newFileName);
	            loadCompletionModel.getPod().transferTo(savedFile);
	            
	     
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	 
	
}
