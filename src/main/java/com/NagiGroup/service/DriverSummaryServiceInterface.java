package com.NagiGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.NagiGroup.dto.driverSummary.DriverMonthlySummaryDTO;
import com.NagiGroup.model.driverSummary.DriverSummaryModel;
import com.NagiGroup.utility.ApiResponse;

public interface DriverSummaryServiceInterface {

	ApiResponse<List<DriverMonthlySummaryDTO>> geDriverSummaryDetails(Integer driverId, String yearMonth);

	ApiResponse<Integer> insertDriverFinancialSummary(DriverSummaryModel driverSummaryModel,
			HttpServletRequest request);

}
