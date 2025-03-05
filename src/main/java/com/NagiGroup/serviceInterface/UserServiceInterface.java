package com.NagiGroup.serviceInterface;

import java.util.List;

import com.NagiGroup.dto.UserDto;
import com.NagiGroup.model.User;
import com.NagiGroup.model.UserMaster;
import com.NagiGroup.utility.ApiResponse;

public interface UserServiceInterface {

	int methodToSaveTheUserDetails(UserMaster userMaster);

	UserDto loadUserByUsername(User user);

	int createUser(UserMaster user);

	List<UserDto> getListOfUser();

	ApiResponse<List<UserDto>> getAllUsers();

}
