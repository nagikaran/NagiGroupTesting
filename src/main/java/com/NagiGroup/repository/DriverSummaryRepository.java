package com.NagiGroup.repository;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.driverSummary.DriverMonthlySummaryDTO;
import com.NagiGroup.model.driverSummary.DriverSummaryModel;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.utility.ApiResponse;

@Repository
public class DriverSummaryRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(DriverSummaryRepository.class);
	private DbContextService dbContextserviceBms;
	public DriverSummaryRepository(DbContextService dbContextserviceBms) {
		this.dbContextserviceBms = dbContextserviceBms;
		
	}

	public ApiResponse<List<DriverMonthlySummaryDTO>> geDriverSummaryDetails(Integer driverId, String yearMonth) {


		List<DriverMonthlySummaryDTO> driverMonthlySummaryList= null;
		try {
			logger.info("DriverSummaryRepository : geDriverSummaryDetails Start");
			Object param[] = { driverId,yearMonth };
			driverMonthlySummaryList = dbContextserviceBms.QueryToListWithParam(QueryMaster.get_driver_monthly_summary_for_loads, param, DriverMonthlySummaryDTO.class);
			logger.info("LoadRepository : geDriverSummaryDetails end");
			return new ApiResponse<List<DriverMonthlySummaryDTO>>(true, "Total Record " + driverMonthlySummaryList.size() + " ", true, driverMonthlySummaryList,
					driverMonthlySummaryList.size());

		} catch (Exception e) {
			logger.info("DriverSummaryRepository : Exception At : geDriverSummaryDetails :", e);
			e.printStackTrace();
			return new ApiResponse<List<DriverMonthlySummaryDTO>>(false, e.getMessage(), false, null, 0);
		}
	
	}

	public ApiResponse<Integer> insertDriverFinancialSummary(DriverSummaryModel driverSummaryModel,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			logger.info("driverSummaryModel: " + driverSummaryModel);
			logger.info("insertDriverFinancialSummary : markLoadInProgress end");
			CommonController commonController = new CommonController();
			int updatedBy = commonController.getUserDtoDataFromToken(request);
			logger.info("updatedBy: " + updatedBy);
			
			Object param[] = { 
					driverSummaryModel.getDriver_id(),
					driverSummaryModel.getMonth_year(),
					driverSummaryModel.getDispatch(),
					driverSummaryModel.getEld_fee(),
					driverSummaryModel.getParking(),
					driverSummaryModel.getIfta_paid(),
					driverSummaryModel.getTrailer_used(),
                    driverSummaryModel.getInsurance(),
                    driverSummaryModel.getLumper(),
                    driverSummaryModel.getScale(),
                    driverSummaryModel.getTrailer_used_by_company(),
                    driverSummaryModel.getTotal_before_misc(),
                    driverSummaryModel.getTotal_after_misc(),
                    driverSummaryModel.getPaid_amount(),
                    driverSummaryModel.getPaid_date(),
                    driverSummaryModel.getCheck_number(),
                    driverSummaryModel.getReturn_money_to_company(),
                    driverSummaryModel.getBorrowed_amount(),
                    updatedBy
			};
			int id = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_driver_financial_summary,
					param);

			if (id != 0) {
				logger.info("DriverSummaryRepository : insertDriverFinancialSummary end : success");
				return new ApiResponse<Integer>(true,
						"Driver finance summary inserted successfully", true, 1, 1);
			} else {
				logger.info("DriverSummaryRepository : insertDriverFinancialSummary end : fail");
				return new ApiResponse<Integer>(false, "Failed to insert finance summary",
						false, 0, 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error in insertDriverFinancialSummary", e);
			e.printStackTrace();
			return new ApiResponse<Integer>(false, "Error while inserting the driver financial summary",
					false, 0, 0);
		}

	}
	
}
