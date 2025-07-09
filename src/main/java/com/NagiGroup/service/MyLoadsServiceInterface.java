package com.NagiGroup.service;

import java.util.List;

import com.NagiGroup.dto.load.MyLoadDTO;
import com.NagiGroup.dto.load.MyLoadsCountDto;
import com.NagiGroup.utility.ApiResponse;

public interface MyLoadsServiceInterface {

	ApiResponse<List<MyLoadDTO>> geMyLoads();

	ApiResponse<List<MyLoadDTO>> getLoadsAsPerPickupDate(String pick_up_date);

	ApiResponse<MyLoadsCountDto> getmyLoadsCount();
	

}
