package com.NagiGroup.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.dto.LicenceKeyDto;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.service.LicenceKeyService;

@Repository
public class LicenceKeyRepository implements LicenceKeyService {
	
	 private static final Logger logger = LoggerFactory.getLogger(LicenceKeyRepository.class);
		
		private DbContextService dbContextserviceBms;
		public LicenceKeyRepository(DbContextService dbContextserviceBms){
			this.dbContextserviceBms=dbContextserviceBms;
		}

		
		

		@Override
		public LicenceKeyDto getLicenceKey() {
			// TODO Auto-generated method stub
			logger.info("LicenceKeyDto : getLoadById Start");
			LicenceKeyDto licenceKeyDto = null;
			try {
				Object params[] = { 1 };
				licenceKeyDto = dbContextserviceBms.QueryToFirstWithParam(QueryMaster.get_licence_key_by_id, params,
						LicenceKeyDto.class);
				logger.info("LoadRepository : getLoadById End");
				
				 if (licenceKeyDto == null) {
			            logger.warn("No license key found in the database!");
			            return null;
			        }

			        logger.info("LicenceKeyDto : getLicenceKey Success");
			        return licenceKeyDto;
			        
			    } catch (Exception e) {            
			        logger.error("Exception in getLicenceKey: ", e);
			        return null;
			    }
		}
}
