package com.NagiGroup.conroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.NagiGroup.dto.UserDto;
import com.NagiGroup.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthorizeTokenController {
	
	private final JwtService jwtService;

    public AuthorizeTokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/token")
    public String getToken(@RequestParam String username) {
		return ""; 
		/* return jwtService.generateToken(username); */
    }
    
    public String getAuthorizeToke(String user_login_id,String role_name,UserDto userDto) {
    	
    	return jwtService.generateToken(user_login_id,role_name,userDto);
    	
    }
    
    
    
    
}
