package com.NagiGroup.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.dto.UserDto;
import com.NagiGroup.model.User;
import com.NagiGroup.model.UserMaster;
import com.NagiGroup.query.QueryMaster;
@Repository
public class UserRepository {
	private DbContextService dbContextserviceBms;
	public UserRepository(DbContextService dbContextserviceBms) {
		this.dbContextserviceBms=dbContextserviceBms;
	}
	
	   public UserDto userLogin(User user) {
	        Object[] param = { user.getLogin_user_id(), user.getPassword() };
	        try {
	            // Query the database for user login and return UserDto
	            UserDto userDto = dbContextserviceBms.QueryToFirstWithParam(QueryMaster.user_login, param, UserDto.class);
	            System.out.println("userDto---->>>> " + userDto);
	            return userDto;
	        } catch (Exception e) {
	        	e.printStackTrace();
	            System.err.println("Error during user login: " + e.getMessage());
	            return null;  // Return null or throw a custom exception
	        }
	    }

	   public int createUser(UserMaster user) {
		   
		    Object[] param = {
		        user.getUser_name(),
		        user.getUser_password(),  // Ensure this is hashed
		        user.getEmail_id(),
		        user.getFirst_name(),
		        user.getLast_name(),
		        user.getContact_no(),
		        user.getCity_name(),
		        user.getState_name(),
		        user.getAddress(),
		        user.getLogin_user_id(),
		        user.getRole_id()
		    };
		    
		    List<Object> asList = Arrays.asList(param);
		    System.out.println("asList---->>>>  " + asList);
		    
		    try {
		        // Insert user details into the database and get the new user ID
		        int newUserId = dbContextserviceBms.QueryToFirstWithInt(QueryMaster.insert_user, param);
		        System.out.println("Inserted user ID----->>>>> " + newUserId);
		        return newUserId;  // Return the newly inserted user ID
		    } catch (Exception e) {
		        System.err.println("Error during user creation: " + e.getMessage());
		        return 0;  // Return 0 or throw a custom exception
		    }
		}

	public List<UserDto> getListOfUser() {
		// TODO Auto-generated method stub
		Object[] param= {};
		List<UserDto> userList = dbContextserviceBms.QueryToListWithParam(QueryMaster.get_all_users, param, UserDto.class);
		return userList;
	}


}
