package com.NagiGroup.conroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.NagiGroup.service.LoadService;
import com.NagiGroup.utility.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value="/api/load")
public class LoadController {
	@Autowired
	public LoadService loadService;
	
//	public LoadController(LoadService loadService) {
//		this.loadService=loadService;
//		
//	}
	
	@PostMapping(value = "/insert" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = insert_load")
	public ApiResponse<Integer>  loadInsert(@ModelAttribute LoadModel loadModel,HttpServletRequest request) {
		return loadService.loadInsert(loadModel,request);		 
	
	}
	
	@PutMapping(value = "/update" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = update_load")
	public ApiResponse<Integer>  loadUpdate(@ModelAttribute LoadUpdateModel loadUpdateModel,HttpServletRequest request) {
		return loadService.loadUpdate(loadUpdateModel,request);		 
	
	}
	@PutMapping(value = "/updateLoad" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = update_load")
	public ApiResponse<Integer>  updateLoad(@ModelAttribute LoadUpdateModel loadUpdateModel,HttpServletRequest request) {
		return loadService.updateLoad(loadUpdateModel,request);		 
	
	}
	
	@GetMapping("/get")
	@Operation(summary = "function = get_all_loads")
	public ApiResponse<List<LoadDto>> geAlltLoads() {
		
		return loadService.geAlltLoads();
	}
	
	@GetMapping("/get/id/{load_id}")
	@Operation(summary = "function = get_load_by_id")
	public ApiResponse<LoadDto> getLoadById(@PathVariable int load_id) {
		
		return loadService.getLoadById(load_id);
	}
	
	@PostMapping(value = "/assign"/* ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE */)	
	@Operation(summary = "function = update_load_status")
	public ApiResponse<Integer>  assignLoad(@RequestBody LoadStatusModel loadStatusModel,HttpServletRequest request) {
		return loadService.assignLoad(loadStatusModel,request);		 
	
	} 
	
	@PostMapping(value = "/in_progress/{load_id}/{load_number}")	
	@Operation(summary = "function = mark_load_in_progress")
	public ApiResponse<Integer>  markLoadInProgress(@PathVariable int load_id,@PathVariable String load_number,HttpServletRequest request) {
		LoadStatusModel loadStatusModel = new LoadStatusModel();
		loadStatusModel.setLoad_id(load_id);
		loadStatusModel.setLoad_number(load_number);
		return loadService.markLoadInProgress(loadStatusModel,request);		 
	
	} 
	
	@PostMapping(value = "/completion",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = handle_load_completion")
	public ApiResponse<Integer>  markLoadComplete(@ModelAttribute LoadCompletionModel loadCompletionModel,HttpServletRequest request) {
		
		return loadService.markLoadComplete(loadCompletionModel,request);		 
	
	} 
	
	
	@PostMapping(value = "/save_doc",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = handle_load_completion")
	public ApiResponse<Integer>  saveDocument(@ModelAttribute LoadCompletionModel loadCompletionModel,HttpServletRequest request) {
		return loadService.saveDocument(loadCompletionModel,request);		 
	
	} 
	
	
	
	@GetMapping("/get/status_id/{status_id}")
	@Operation(summary = "function = get_loads_by_status")
	public ApiResponse<List<LoadDto>> getLoadByStatusId(@PathVariable int status_id) {
		
		return loadService.getLoadByStatusId(status_id);
	}
	
	
	@GetMapping("/get/loads/count/")
	@Operation(summary = "function = get_all_loads")
	public ApiResponse<LoadStatusSummaryDto> getLoadCountAsPerTheStatus() {
		
		return loadService.getLoadCountAsPerTheStatus();
	}
	
	@GetMapping("/get_all_company_name")
	@Operation(summary = "function = get_all_company_name")
	public ApiResponse<List<CompanyNameDto>> geAllCompanyName() {
		
		return loadService.geAllCompanyName();
	}
	
	@GetMapping("/get/assignment_details/{load_id}/{load_number}")
	@Operation(summary = "function = get_load_details_for_driver_assignment")
	public ApiResponse<LoadAssignmentDocumentDto> getLoadDetailsForAssignment(@PathVariable int load_id,@PathVariable String load_number) {
		
		return loadService.getLoadDetailsForAssignment(load_id,load_number);
	}
	
	@PostMapping(value = "/request_to_invoice",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = request_to_invoice")
	public ApiResponse<Integer>  requestToInvoice(@ModelAttribute LoadAdditionalCharges loadAdditionalCharges,HttpServletRequest request) {
		return loadService.requestToInvoice(loadAdditionalCharges,request);		 
	
	}
	
	
	@PostMapping(value = "/cancel_load",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = request_to_invoice")
	public ApiResponse<Integer>  cancelLoad(@ModelAttribute CancelLoadModel cancelLoadModel,HttpServletRequest request) {
		return loadService.cancelLoad(cancelLoadModel,request);		 
	
	}
	
	@PostMapping(value = "/request_to_invoice_for_tonu",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = request_to_invoice")
	public ApiResponse<Integer>  requestToInvoiceForTonu(@ModelAttribute CancelLoadModel cancelLoadModel,HttpServletRequest request) {
		return loadService.requestToInvoiceForTonu(cancelLoadModel,request);		 
	
	}
	@PostMapping(value = "/request_to_manage_no_log_doc",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = handle_load_completion")
	public ApiResponse<Integer>  requestToManageNoLogDoc(@ModelAttribute LoadCompletionModel loadCompletionModel,HttpServletRequest request) {
		return loadService.requestToManageNoLogDoc(loadCompletionModel,request);		 
	
	} 
			
}
