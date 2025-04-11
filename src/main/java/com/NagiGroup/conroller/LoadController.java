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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.dto.load.LoadDto;
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
	public LoadService loadService;
	@Autowired
	public LoadController(LoadService loadService) {
		this.loadService=loadService;
		
	}
	
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
	@Operation(summary = "function = get_all_loads")
	public ApiResponse<LoadDto> getLoadById(@PathVariable int load_id) {
		
		return loadService.getLoadById(load_id);
	}
	
	@PostMapping(value = "/assign",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
	@Operation(summary = "function = update_load_status")
	public ApiResponse<Integer>  assignLoad(@ModelAttribute LoadStatusModel loadStatusModel,HttpServletRequest request) {
		return loadService.assignLoad(loadStatusModel,request);		 
	
	} 
	
	@PostMapping(value = "/in_progress")	
	@Operation(summary = "function = mark_load_in_progress")
	public ApiResponse<Integer>  markLoadInProgress(@ModelAttribute LoadStatusModel loadStatusModel,HttpServletRequest request) {
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
	
	
	
	
	
	
}
