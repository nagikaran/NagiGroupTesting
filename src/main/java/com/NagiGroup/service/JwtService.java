package com.NagiGroup.service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.NagiGroup.dto.UserDto;

@Service
public class JwtService {
	 private final JwtEncoder jwtEncoder;

	    public JwtService(JwtEncoder jwtEncoder) {
	        this.jwtEncoder = jwtEncoder;
	    }

	    public String generateToken(String username,String role_name,UserDto userDto) {
	        Instant now = Instant.now();
	        JwtClaimsSet claims = JwtClaimsSet.builder()
	                .issuer("self")
	                .issuedAt(now)
	                .expiresAt(now.plus(1, ChronoUnit.HOURS))
	                .subject(username)
	                .claim("role_name", "ROLE_" +role_name)
	                .claim("scope", "read")
	                .claim("userId", userDto.getUser_id())
	                .build();

	        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	    }

		public String generateAuthorizeToken(String user_login_id, String password) {
			Instant now = Instant.now();
	        JwtClaimsSet claims = JwtClaimsSet.builder()
	                .issuer("self")
	                .issuedAt(now)
	                .expiresAt(now.plus(1, ChronoUnit.HOURS))
	                .subject(user_login_id)
	                .subject(password)
	                .claim("scope", "read")
	                .build();

	        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		}
}
