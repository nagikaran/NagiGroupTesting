package com.NagiGroup.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.NagiGroup.dto.load.MyLoadDTO;
import com.NagiGroup.dto.load.MyLoadsCountDto;
import com.NagiGroup.repository.MyLoadsRepository;
import com.NagiGroup.service.MyLoadsServiceInterface;
import com.NagiGroup.utility.ApiResponse;

@Service
public class MyLoadsSerivceIMp implements MyLoadsServiceInterface {
	
private MyLoadsRepository myLoadsRepository;
	
	public MyLoadsSerivceIMp(MyLoadsRepository myLoadsRepository) {
		this.myLoadsRepository=myLoadsRepository;
	}


	@Override
	public ApiResponse<List<MyLoadDTO>> geMyLoads() {
		// TODO Auto-generated method stub
		return myLoadsRepository.geMyLoads();
	}


	@Override
	public ApiResponse<List<MyLoadDTO>> getLoadsAsPerPickupDate(String pick_up_date) {
		// TODO Auto-generated method stub
		return myLoadsRepository.getLoadsAsPerPickupDate(pick_up_date);
	}


	@Override
	public ApiResponse<MyLoadsCountDto> getmyLoadsCount() {
		// TODO Auto-generated method stub
		return myLoadsRepository.getmyLoadsCount();
	}
	

}
