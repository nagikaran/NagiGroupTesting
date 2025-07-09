package com.NagiGroup.serviceimpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.NagiGroup.dto.driverSummary.DriverMonthlySummaryDTO;
import com.NagiGroup.model.driverSummary.DriverSummaryModel;
import com.NagiGroup.repository.DriverSummaryRepository;
import com.NagiGroup.service.DriverSummaryServiceInterface;
import com.NagiGroup.utility.ApiResponse;

@Service
public class DriverSummaryServiceImpl implements DriverSummaryServiceInterface{
private DriverSummaryRepository driverSummaryRepository;
	
	public DriverSummaryServiceImpl(DriverSummaryRepository driverSummaryRepository) {
		this.driverSummaryRepository=driverSummaryRepository;
	}

	@Override
	public ApiResponse<List<DriverMonthlySummaryDTO>> geDriverSummaryDetails(Integer driverId, String yearMonth) {
		// TODO Auto-generated method stub
		return driverSummaryRepository.geDriverSummaryDetails(driverId,yearMonth);
	}

	@Override
	public ApiResponse<Integer> insertDriverFinancialSummary(DriverSummaryModel driverSummaryModel,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return driverSummaryRepository.insertDriverFinancialSummary(driverSummaryModel,request);
	}

}
