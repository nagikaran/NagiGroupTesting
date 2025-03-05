package com.NagiGroup.conroller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.config.RsaKeyProperties;
import com.NagiGroup.dto.UserDto;
import com.NagiGroup.dto.user.LoginResponseDto;
import com.NagiGroup.model.User;
import com.NagiGroup.model.UserMaster;
import com.NagiGroup.serviceInterface.UserServiceInterface;
import com.NagiGroup.utility.ApiResponse;
import com.NagiGroup.utility.PropertiesReader;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins = "*") // Allow requests from all origins
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
	
	@Autowired
	UserServiceInterface userServiceInterface;
	
	
	@Autowired
	AuthorizeTokenController authorizeTokenController;
	
	private final RsaKeyProperties rsaKeys;

    public UserController(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }
	
	
    @Autowired
    private JwtDecoder jwtDecoder;
	
	@GetMapping(value = "/login")
	public ApiResponse<LoginResponseDto>  userLogin(@ModelAttribute User user) {
		String authorizeToken = "";
		System.out.println("User details "+user.getLogin_user_id()+":"+user.getPassword());
		UserDto userDto  = userServiceInterface.loadUserByUsername(user);
		System.out.println("userDto: "+userDto);
		if(userDto!=null) {
			authorizeToken = authorizeTokenController.getAuthorizeToke(userDto.getLogin_user_id(),userDto.getRole_name(),userDto);
			 LoginResponseDto loginResponseDto = new LoginResponseDto();
			 loginResponseDto.setFirst_name(userDto.getFirst_name());
			 loginResponseDto.setLast_name(userDto.getLast_name());
			 loginResponseDto.setEmail_id(userDto.getEmail_id());
			 loginResponseDto.setUser_id(userDto.getUser_id());
			 loginResponseDto.setRole_id(userDto.getRole_id());
			 loginResponseDto.setRole_name(userDto.getRole_name());
			 loginResponseDto.setContact_no(userDto.getContact_no()+"");
			 loginResponseDto.setLogin_user_id(userDto.getLogin_user_id());
			 loginResponseDto.setToken(authorizeToken);
			 return new ApiResponse<LoginResponseDto>(true,
						PropertiesReader.getProperty("message", "VALID_USER_CREDENTIAL"), true, loginResponseDto, 1);
			
		}else {
			return new ApiResponse<LoginResponseDto>(false,
					PropertiesReader.getProperty("message", "INVALID_USER_CREDENTIAL"), true, null, 0);
		
		}
	
	}
	
	
	
	
	    @PostMapping("/create")
	    @Secured("ROLE_Admin")
	    public ResponseEntity<String> createUser(@RequestBody  UserMaster user) {
	        // Implementation for creating a user
	    	System.out.println("user.getFirst_name()----->>> "+user.getFirst_name());
		 int userId = userServiceInterface.createUser(user);
		 if(userId!=0) {
			 return ResponseEntity.status(HttpStatus.OK).body("User Created Successfully");
		 }else {
			 return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Something went wrong while creating the user.");
		 }
	       
	    }
	               	
	    @PostMapping("/insert")
	    public ResponseEntity<String> userInsert(@RequestBody UserMaster userMaster) {
	    	System.out.println("userMaster--->>>> "+userMaster.getFirst_name());
	        int result = userServiceInterface.methodToSaveTheUserDetails(userMaster);
	        if (result > 0) {
	            return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Creating User");
	        }
	    }
	    
	    
	    @GetMapping("/admin")
	    @Secured("ROLE_Admin")
	    public ResponseEntity<String> adminEndpoint() {
	        return ResponseEntity.ok("Admin access granted");
	    }
	    
	    
	    @GetMapping("/user")
	    @Secured("ROLE_User")
	    public ResponseEntity<String> userEndpoint() {
	        return ResponseEntity.ok("User access granted");
	    }
	    
	    
	    @GetMapping("/test")
	    public ResponseEntity<String> test(HttpServletRequest request) {
	        String token = request.getHeader("Authorization").substring(7); // Removing "Bearer " prefix
	        org.springframework.security.oauth2.jwt.Jwt jwt = jwtDecoder.decode(token);
	        Map<String, Object> claims = jwt.getClaims();
	        
	        String roleName = (String) claims.get("role_name");
	        System.out.println("Role Name: " + roleName);
	        System.out.println("Token Claims: " + claims);

	        // Perform role-based logic here
	        if ("ROLE_Admin".equals(roleName)) {
	            return ResponseEntity.ok("Admin access granted");
	        } else if ("ROLE_User".equals(roleName)) {
	            return ResponseEntity.ok("User access granted");
	        } else {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
	        }
	    }
	    
	    
	    @GetMapping(value = "/getListOfUser")
	    @Secured({"ROLE_Admin", "ROLE_User"})                                   
	    public ResponseEntity<List<UserDto>>  getListOfUser(){
	    	List<UserDto> user=userServiceInterface.getListOfUser();
	    	return ResponseEntity.ok(user);
	    	
	    }
	    
	    
	    @GetMapping("/get")
		@Operation(summary = "function = get_all_users")
		public ApiResponse<List<UserDto>> getAllUsers() {
			
			return userServiceInterface.getAllUsers();
		}
	    
	   
	    

}
