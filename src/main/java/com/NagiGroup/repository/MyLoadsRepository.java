package com.NagiGroup.repository;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.dto.load.MyLoadDTO;
import com.NagiGroup.dto.load.MyLoadsCountDto;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.utility.ApiResponse;

@Repository
public class MyLoadsRepository {
	private DbContextService dbContextserviceBms;
	private static final Logger logger = LoggerFactory.getLogger(MyLoadsRepository.class);
public MyLoadsRepository(DbContextService dbContextserviceBms) {
	this.dbContextserviceBms=dbContextserviceBms;
}
	public ApiResponse<List<MyLoadDTO>> geMyLoads() {

		List<MyLoadDTO> myLoadDtos = null;
		try {
			logger.info("MyLoadsRepository : geMyLoads Start");
			myLoadDtos = dbContextserviceBms.QueryToList(QueryMaster.get_my_loads, MyLoadDTO.class);
			logger.info("MyLoadsRepository : geMyLoads End");
			return new ApiResponse<List<MyLoadDTO>>(true, "Total Record " + myLoadDtos.size() + " ", true, myLoadDtos,
					myLoadDtos.size());

		} catch (Exception e) {
			logger.info("MyLoadsRepository : Exception At : geMyLoads :", e);
			return new ApiResponse<List<MyLoadDTO>>(false, e.getMessage(), false, null, 0);
		}
	}
	public ApiResponse<List<MyLoadDTO>> getLoadsAsPerPickupDate(String pick_up_date) {


		List<MyLoadDTO> myLoadDTOs= null;
		try {
			logger.info("MyLoadsRepository : getLoadsAsPerPickupDate Start");
			Date sqlDate = Date.valueOf(pick_up_date);
			Object param[] = { sqlDate};
			myLoadDTOs = dbContextserviceBms.QueryToListWithParam(QueryMaster.get_loads_by_destinations_order_as_per_load_type, param, MyLoadDTO.class);
			logger.info("MyLoadsRepository : getLoadsAsPerPickupDate end");
			return new ApiResponse<List<MyLoadDTO>>(true, "Total Record " + myLoadDTOs.size() + " ", true, myLoadDTOs,
					myLoadDTOs.size());

		} catch (Exception e) {
			logger.info("MyLoadsRepository : Exception At : getLoadsAsPerPickupDate :", e);
			e.printStackTrace();
			return new ApiResponse<List<MyLoadDTO>>(false, e.getMessage(), false, null, 0);
		}
	
	}
	public ApiResponse<MyLoadsCountDto> getmyLoadsCount() {
		// TODO Auto-generated method stub
		logger.info("MyLoadsRepository : getmyLoadsCount Start");
		MyLoadsCountDto myLoadsCountDto = null;
		try {

			myLoadsCountDto = dbContextserviceBms.QueryToFirst(QueryMaster.get_load_status_summary,
					MyLoadsCountDto.class);
			logger.info("MyLoadsRepository : getmyLoadsCount End");
			return new ApiResponse<MyLoadsCountDto>(true, "Total Record " + 1 + " ", true, myLoadsCountDto,
					1);
		} catch (Exception e) {
			logger.info("MyLoadsRepository : Exception At : getmyLoadsCount :", e);
			return new ApiResponse<MyLoadsCountDto>(false, "No record found", false, null, 0);
		}
	}
	
	
	

}
