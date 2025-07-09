package com.NagiGroup.connection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbContextService implements com.NagiGroup.connection.web.DbContextService {
	@Autowired
	@Qualifier("jdbcTemplateRoleBasedAuthentication")
	private JdbcTemplate jdbcTemplateRoleBasedAuthentication;
	
	
	
	@Override
	@SuppressWarnings("deprecation")
	public int QueryToFirstWithInt(String query, Object[] paramsArray) {
		int result = 0;
		try {
			result = jdbcTemplateRoleBasedAuthentication.queryForObject(query, paramsArray, Integer.class);
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}				
		return result;
	}



	@Override
	/**
	 * @param - 1 = query : to call function
	 * @param - 2 = paramsArray : function's Parameter
	 * @param - 3 = typeRef : Model / DTO.class object
	 * @return List of appropriate Model / DTO class 
	 */
	public <T> List<T> QueryToListWithParam(String query, Object[] paramsArray, Class<T> typeRef) {
		try {
			return (List<T>) jdbcTemplateRoleBasedAuthentication.query(query, 
					         BeanPropertyRowMapper.newInstance(typeRef), 
					         paramsArray);
		} catch (Exception e) {
			throw e;
		}
	}



	@Override
	/**
	 * @param - 1 = query : to call function
	 * @param - 2 = paramsArray : function's Parameter
	 * @param - 3 = typeRef : Model / DTO.class object
	 * @return first [one] record of appropriate Model / DTO class
	 */
	public <T> T QueryToFirstWithParam(String query, Object[] paramsArray, Class<T> typeRef) {
		try {
			T resultSetString = jdbcTemplateRoleBasedAuthentication.queryForObject(query, 
								BeanPropertyRowMapper.newInstance(typeRef),
								paramsArray);
			return resultSetString;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * @param - 1 = query : to call function
	 * @param - 2 = typeRef : Model / DTO.class object
	 * @return List of appropriate Model / DTO class 
	 */
	public <T> List<T> QueryToList(String query, Class<T> typeRef) {
		try {
			return (List<T>) jdbcTemplateRoleBasedAuthentication.query(query, 
							 BeanPropertyRowMapper.newInstance(typeRef));
		} catch (Exception e) {
			throw e;
		}
	}



	@Override
	public <T> T QueryToFirst(String query, Class<T> typeRef) {
		try {
			T resultSetString = jdbcTemplateRoleBasedAuthentication.queryForObject(query, 
								BeanPropertyRowMapper.newInstance(typeRef));
			return resultSetString;
		} catch (Exception e) {
			throw e;
		}
	}



	@Override
	@SuppressWarnings("deprecation")
	public long QueryToFirstWithLong(String query, Object[] paramsArray) {
		long result = 0L;
	    try {
	        result = jdbcTemplateRoleBasedAuthentication.queryForObject(query, paramsArray, Long.class);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        throw ex;
	    }
	    return result;
	}
	

}
