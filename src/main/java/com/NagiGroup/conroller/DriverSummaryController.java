package com.NagiGroup.conroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.dto.driverSummary.DriverMonthlySummaryDTO;
import com.NagiGroup.model.driverSummary.DriverSummaryModel;
import com.NagiGroup.service.DriverSummaryServiceInterface;
import com.NagiGroup.utility.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value="/api/driver_summary")
public class DriverSummaryController {
	 private static final Logger logger = LoggerFactory.getLogger(DriverSummaryController.class);
	 
	 @Autowired
	 public DriverSummaryServiceInterface driverSummaryServiceInterface;
	
	
	@GetMapping("/get/{driver_id}/{year_month}")
	@Operation(summary = "function = get_driver_monthly_summary")
	public ApiResponse<List<DriverMonthlySummaryDTO>> geAlltLoads(
			@PathVariable("driver_id") Integer driverId,
	        @PathVariable("year_month") String yearMonth) {
		
		return driverSummaryServiceInterface.geDriverSummaryDetails(driverId,yearMonth);
	}
	
	
	@PostMapping(value = "/insert_driver_financial_summary")	
	@Operation(summary = "function = insert_driver_financial_summary")
	public ApiResponse<Integer>  insertDriverFinancialSummary(@RequestBody DriverSummaryModel driverSummaryModel,HttpServletRequest request) {
		return driverSummaryServiceInterface.insertDriverFinancialSummary(driverSummaryModel,request);		 
	
	}
	
	
}
