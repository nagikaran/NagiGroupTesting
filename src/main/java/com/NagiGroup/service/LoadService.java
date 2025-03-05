package com.NagiGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.NagiGroup.dto.load.LoadDto;
import com.NagiGroup.model.load.LoadModel;
import com.NagiGroup.model.load.LoadUpdateModel;
import com.NagiGroup.utility.ApiResponse;

@Service
public interface LoadService {

	ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request);

	ApiResponse<Integer> loadUpdate(LoadUpdateModel loadUpdateModel, HttpServletRequest request);

	ApiResponse<List<LoadDto>> geAlltLoads();

	ApiResponse<LoadDto> getLoadById(int load_id);

	ApiResponse<Integer> assignLoad(int loadId, HttpServletRequest request);

}
