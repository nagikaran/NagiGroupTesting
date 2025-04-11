package com.NagiGroup.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.NagiGroup.dto.driverDocument.DriverDocumentManagementDto;
import com.NagiGroup.dto.load.LoadDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.model.DriverDocumentManagementModel;
import com.NagiGroup.model.DriverDocumentManagementUpdateModel;
import com.NagiGroup.model.load.LoadCompletionModel;
import com.NagiGroup.model.load.LoadModel;
import com.NagiGroup.model.load.LoadStatusModel;
import com.NagiGroup.model.load.LoadUpdateModel;
import com.NagiGroup.repository.DriverDocumentManagementRepository;
import com.NagiGroup.repository.LoadRepository;
import com.NagiGroup.service.DriverDocumentManagementService;
import com.NagiGroup.service.LoadService;
import com.NagiGroup.utility.ApiResponse;
@Configuration
public class AppConfig {
	
//	private DriverDocumentManagementRepository documentManagementRepository;
//	private LoadRepository loadRepository;
//	@Autowired
//	public AppConfig(DriverDocumentManagementRepository documentManagementRepository, LoadRepository loadRepository) {
//		this.documentManagementRepository=documentManagementRepository;
//		this.loadRepository=loadRepository;
//	}
//	
//	@Bean
//    public DriverDocumentManagementService driverDocumentManagementService() {
//        return new DriverDocumentManagementService() {
//
//			@Override
//			public ApiResponse<Integer> driverDocumentManagemeInsert(
//					DriverDocumentManagementModel driverDocumentManagementModel, HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return documentManagementRepository.driverDocumentManagemeInsert(driverDocumentManagementModel, request);
//			}
//
//			@Override
//			public ApiResponse<List<DriverDocumentManagementDto>> getDriverDocuments() {
//				// TODO Auto-generated method stub
//				return documentManagementRepository.getDriverDocuments();
//			}
//
//			@Override
//			public ApiResponse<List<SubFolderDto>> getsubFolderMaster() {
//				// TODO Auto-generated method stub
//				return documentManagementRepository.getsubFolderMaster();
//			}
//
//			@Override
//			public ApiResponse<Integer> driverDocumentManagemeUpdate(
//					DriverDocumentManagementUpdateModel documentManagementUpdateModel, HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return documentManagementRepository.driverDocumentManagemeUpdate(documentManagementUpdateModel, request);
//			}
//
//			@Override
//			public ApiResponse<DriverDocumentManagementDto> getDriverDocumentManagementById(int driver_documents_id) {
//				// TODO Auto-generated method stub
//				return documentManagementRepository.getDriverDocumentManagementById(driver_documents_id);
//			}
//            
//        };
//    }
//	
//	@Bean
//    public LoadService loadService() {
//        return new LoadService() {
//
//			@Override
//			public ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return loadRepository.loadInsert(loadModel, request);
//			}
//
//			@Override
//			public ApiResponse<Integer> loadUpdate(LoadUpdateModel loadUpdateModel, HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return loadRepository.loadUpdate(loadUpdateModel, request);
//			}
//
//			@Override
//			public ApiResponse<List<LoadDto>> geAlltLoads() {
//				// TODO Auto-generated method stub
//				return loadRepository.geAlltLoads();
//			}
//
//			@Override
//			public ApiResponse<LoadDto> getLoadById(int load_id) {
//				// TODO Auto-generated method stub
//				return loadRepository.getLoadById(load_id);
//			}
//
//			@Override
//			public ApiResponse<Integer> assignLoad(LoadStatusModel loadStatusModel, HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return loadRepository.assignLoad(loadStatusModel, request);
//			}
//
//			@Override
//			public ApiResponse<Integer> updateLoad(LoadUpdateModel loadUpdateModel, HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return loadRepository.updateLoad(loadUpdateModel, request);
//			}
//
//			@Override
//			public ApiResponse<Integer> markLoadInProgress(LoadStatusModel loadStatusModel,
//					HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return loadRepository.markLoadInProgress(loadStatusModel, request);
//			}
//
//			@Override
//			public ApiResponse<Integer> markLoadComplete(LoadCompletionModel loadCompletionModel,
//					HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return loadRepository.markLoadComplete(loadCompletionModel, request);
//			}
//
//			@Override
//			public ApiResponse<Integer> saveDocument(LoadCompletionModel loadCompletionModel,
//					HttpServletRequest request) {
//				// TODO Auto-generated method stub
//				return loadRepository.saveDocument(loadCompletionModel, request);
//			}
//            
//        };
//    }
//	
	

}
