package com.NagiGroup.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.load.LoadDto;
import com.NagiGroup.model.load.LoadModel;
import com.NagiGroup.model.load.LoadUpdateModel;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.service.LoadService;
import com.NagiGroup.utility.ApiResponse;
import com.NagiGroup.utility.CommonUtility;
import com.NagiGroup.utility.PropertiesReader;

@Repository
public class LoadRepository implements LoadService {
	 private static final Logger logger = LoggerFactory.getLogger(LoadRepository.class);
	 private DbContextService dbContextserviceBms;
		public LoadRepository(DbContextService dbContextserviceBms){
			this.dbContextserviceBms=dbContextserviceBms;
		}
	 
	@Override
	public ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		logger.info("LoadRepository : loadInsert start");
		int load_id=0;
		try {
			
			   int statusId = (loadModel.getAssign_to() != 0) ? 2 : 1; // 2 = Assigned, 1 = Pending
			   int previousStatus = (statusId == 2) ? 1 : 0; // If assigned at creation, previous was "Pending"
			
			   String pick_up_date_string = loadModel.getPick_up_date_string();
				String delievery_date_string = loadModel.getDelievery_date_string();
				String earliest_time_arrival_string = loadModel.getEarliest_time_arrival_string();
			
				LocalDateTime pick_up_date_time = CommonUtility.parseDateString(pick_up_date_string);
				LocalDateTime delievery_date_time = CommonUtility.parseDateString(delievery_date_string);
				LocalDateTime earliest_time_arrival = CommonUtility.parseDateString(earliest_time_arrival_string);

				logger.info("pick_up_date_time: " + pick_up_date_time);
				logger.info("delievery_date_time: " + delievery_date_time);
				logger.info("earliest_time_arrival: " + earliest_time_arrival);
				logger.info("pick_up_date_string: " + pick_up_date_string);
				
				Object param[] = {
						loadModel.getLoadNumber(),
						loadModel.getSource(),
						loadModel.getDestination(),
						pick_up_date_time,
						delievery_date_time,
						earliest_time_arrival,
						loadModel.getDriver_id(),
						loadModel.getBase_price(),
						loadModel.getFinal_price(),	
						loadModel.getAssign_to()!=0?loadModel.getAssign_to():0,
						statusId,
						CommonController.getUserDtoDataFromToken(request)
						};
							
				load_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_insert, param);
				if (load_id != 0) {
				    Object statusHistoryParam[] = {
				        load_id,   // Load ID
				        previousStatus,         // Previous status is always "Pending" (1)
				        statusId,  // New status (Pending or Assigned)
				        CommonController.getUserDtoDataFromToken(request)
				    };
				    dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_status_history_insert, statusHistoryParam);
				}
				if(load_id!=0 && loadModel.getRoc()!=null) {
					 logger.info("LoadRepository : load_id : " + load_id);
			            /*file saving work
			             * now here i want to save the roc */
			            String baseUrl = PropertiesReader.getProperty("constant", "BASEURL");
			            String folderName = PropertiesReader.getProperty("constant", "BASEURL_FOR_ROC");
			            String 	basePathForDocument = baseUrl+loadModel.getLoadNumber()+"/"+PropertiesReader.getProperty("constant", "BASEURL_FOR_ROC");
			        	boolean isCreated = CommonUtility.saveDocument(loadModel.getRoc(), loadModel.getLoadNumber(),
			        			baseUrl,folderName);
			        	if (isCreated) {
							String  fileName   = loadModel.getRoc().getOriginalFilename();
						    String	sourcePath = basePathForDocument + fileName;
						    
						    System.out.println("File deleted successfully.");
				            Object loadDocumentParam[] = {				   
				            		loadModel.getLoadNumber(),
				            		loadModel.getRoc().getOriginalFilename()         		
				            };
				        	int document_id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_document_insert, loadDocumentParam);
				            if(document_id!=0) {
				            	logger.info("File details updated successfully.");
				            }
				            else {
				            	logger.info("Failed to update File details.");
				            }
									
						 logger.info("LoadRepository : sourcePath : " + sourcePath);
						}
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

	@Override
	public ApiResponse<Integer> loadUpdate(LoadUpdateModel loadUpdateModel, HttpServletRequest request) {
		// TODO Auto-generated method stub
		int load_id = 0;
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
					CommonController.getUserDtoDataFromToken(request)
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

	@Override
	public ApiResponse<List<LoadDto>> geAlltLoads() {
		
		List<LoadDto> loadDtos = null;
		try {
			logger.info("LoadRepository : geAlltLoads Start");
			loadDtos = dbContextserviceBms.QueryToList(QueryMaster.get_all_load, LoadDto.class);
			logger.info("LoadRepository : geAlltLoads Start");
			return new ApiResponse<List<LoadDto>>(true, "Total Record " + loadDtos.size() + " ", true,
					loadDtos, loadDtos.size());
			
		} catch (Exception e) {
			logger.info("LoadRepository : Exception At : geAlltLoads :" , e);
			return new ApiResponse<List<LoadDto>>(false, e.getMessage(), false, null, 0);
		}
	}

	@Override
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

	@Override
	public ApiResponse<Integer> assignLoad(int loadId, HttpServletRequest request) {
	    logger.info("Starting load {}", loadId);
	    
	    try {
	    	Object param[] = {
	    			loadId
	    	};
	        int previousStatus = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.current_load_status,param);
	        
	        if (previousStatus != 2) {  // Only allow starting if status is Assigned
	            
	            return new ApiResponse<Integer>(false, "Load cannot be started. Current status: " + previousStatus,
						false, previousStatus, 0);
	        
	        }

	        int statusId = 2; // In Progress
	        String updatedBy = CommonController.getUserDtoDataFromToken(request);
	        
	        // Update Load Table
	        dbContextserviceBms.QueryToFirstWithInt(QueryMaster.updateLoadStatus, new Object[]{statusId, updatedBy, loadId});
	        
	        // Insert into Status History
	        Object statusHistoryParam[] = { loadId, previousStatus, statusId, updatedBy };
	        dbContextserviceBms.QueryToFirstWithInt(QueryMaster.load_status_history_insert, statusHistoryParam);
	        return new ApiResponse<Integer>(true, "Load cannot be started. Current status: " + statusId,
	        		true, previousStatus, 1);
	        

	        
	        
	        
	    } catch (Exception e) {
	        logger.error("Error in startLoad", e);
//	        return new ApiResponse<Integer>(false, "Error while starting load", false, false, 0);
//	        
	        return new ApiResponse<Integer>(true, "Load cannot be started. Current status: " + 1,
	        		true, 1, 1);
	    }}
	
	
	
	
}
