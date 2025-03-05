package com.NagiGroup.serviceImplementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.NagiGroup.connection.web.DbContextService;
import com.NagiGroup.dto.UserDto;
import com.NagiGroup.dto.subFolder.SubFolderDto;
import com.NagiGroup.model.User;
import com.NagiGroup.model.UserMaster;
import com.NagiGroup.query.QueryMaster;
import com.NagiGroup.repository.UserRepository;
import com.NagiGroup.serviceInterface.UserServiceInterface;
import com.NagiGroup.utility.ApiResponse;


@Service
public class UserServiceImplementationClass implements UserServiceInterface {

	private DbContextService dbContextserviceBms;
	private UserRepository userRepository; 
	public UserServiceImplementationClass(UserRepository userRepository,DbContextService dbContextserviceBms)
	{
	
		this.userRepository=userRepository;
		this.dbContextserviceBms=dbContextserviceBms;
	}
	@Override
	public int methodToSaveTheUserDetails(UserMaster userMaster) {
		// TODO Auto-generated method stub
		Object[] parameters = {userMaster.getUser_name(),userMaster.getUser_password()};
		int userId = userRepository.createUser(userMaster);
		return userId;
	}

	@Override
	public UserDto loadUserByUsername(User user) {
		// TODO Auto-generated method stub
		UserDto userDto = userRepository.userLogin(user);
	System.out.println("userDto service "+userDto);
		return userDto;
	}
	@Override
	public int createUser(UserMaster user) {
		// TODO Auto-generated method stub
		int userId = userRepository.createUser(user);
		return userId;
	}
	@Override
	public List<UserDto> getListOfUser() {
		// TODO Auto-generated method stub
		List<UserDto> user = userRepository.getListOfUser();
		return user;
	}
	@Override
	public ApiResponse<List<UserDto>> getAllUsers() {
		
		List<UserDto> userDtos = null;
		try {
			userDtos = dbContextserviceBms.QueryToList(QueryMaster.get_all_users, UserDto.class);
			
			return new ApiResponse<List<UserDto>>(true, "Total Record " + userDtos.size() + " ", true,
					userDtos, userDtos.size());
		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResponse<List<UserDto>>(false, e.getMessage(), false, null, 0);
		}
	}

}
