package com.NagiGroup.conroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.dto.driverSummary.DriverMonthlySummaryDTO;
import com.NagiGroup.dto.load.LoadStatusSummaryDto;
import com.NagiGroup.dto.load.MyLoadDTO;
import com.NagiGroup.dto.load.MyLoadsCountDto;
import com.NagiGroup.service.MyLoadsServiceInterface;
import com.NagiGroup.utility.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value="/api/my_loads")
public class MyLoadsController {
	@Autowired
	public MyLoadsServiceInterface myLoadsServiceInterface;
	
	@GetMapping("/get_my_loads")
	@Operation(summary = "function = get_my_loads")
	public ApiResponse<List<MyLoadDTO>> geMyLoads() {
		
		return myLoadsServiceInterface.geMyLoads();
	}
	
	@GetMapping("/get_my_loads/{pick_up_date}")
	@Operation(summary = "function = get_loads_by_destinations_order_as_per_load_type")
	public ApiResponse<List<MyLoadDTO>> getLoadsAsPerPickupDate(
	        @PathVariable("pick_up_date") String pick_up_date) {
		
		return myLoadsServiceInterface.getLoadsAsPerPickupDate(pick_up_date);
	}
	
	@GetMapping("/get/my_loads/count/")
	@Operation(summary = "function = get_my_loads_count")
	public ApiResponse<MyLoadsCountDto> getmyLoadsCount() {
		
		return myLoadsServiceInterface.getmyLoadsCount();
	}
}
