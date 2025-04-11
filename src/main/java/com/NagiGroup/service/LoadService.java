package com.NagiGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.NagiGroup.dto.load.LoadDto;
import com.NagiGroup.model.load.LoadCompletionModel;
import com.NagiGroup.model.load.LoadModel;
import com.NagiGroup.model.load.LoadStatusModel;
import com.NagiGroup.model.load.LoadUpdateModel;
import com.NagiGroup.utility.ApiResponse;


public interface LoadService {

	ApiResponse<Integer> loadInsert(LoadModel loadModel, HttpServletRequest request);

	ApiResponse<Integer> loadUpdate(LoadUpdateModel loadUpdateModel, HttpServletRequest request);

	ApiResponse<List<LoadDto>> geAlltLoads();

	ApiResponse<LoadDto> getLoadById(int load_id);

	ApiResponse<Integer> assignLoad(LoadStatusModel loadStatusModel, HttpServletRequest request);

	ApiResponse<Integer> updateLoad(LoadUpdateModel loadUpdateModel, HttpServletRequest request);

	ApiResponse<Integer> markLoadInProgress(LoadStatusModel loadStatusModel, HttpServletRequest request);

	ApiResponse<Integer> markLoadComplete(LoadCompletionModel loadCompletionModel, HttpServletRequest request);

	ApiResponse<Integer> saveDocument(LoadCompletionModel loadCompletionModel, HttpServletRequest request);

}
