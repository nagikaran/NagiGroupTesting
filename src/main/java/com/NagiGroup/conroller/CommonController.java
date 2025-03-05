package com.NagiGroup.conroller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import java.util.Map;


import com.NagiGroup.dto.UserDto;

public class CommonController {
	 private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	 private static  JwtDecoder jwtDecoder;
	
	 public static String getFileNameWithoutExtension(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            return null;
        }
        
        String originalFileName = file.getOriginalFilename();
        int lastDotIndex = originalFileName.lastIndexOf(".");
        
        if (lastDotIndex == -1) {
            return originalFileName; // No extension found, return as is
        }
        
        return originalFileName.substring(0, lastDotIndex);
    }
	
	public static String getUserDtoDataFromToken(HttpServletRequest request) {		
		logger.info("CommonController : " + request.getRequestURI());
		UserDto uDto = null;
		try {
			final String requestTokenHeader = request.getHeader("Authorization");
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				 requestTokenHeader.substring(7);
				return extractUserId(requestTokenHeader.substring(7));
			}
			else {
				return "0";
			}
			
		} catch (Exception e) {			
			logger.info("CommonController : Error At : getUserDtoDataFromToken : " + e);
			return "0";
		}
	}
	public static Map<String, Object> extractClaims(String token) {
	    Jwt jwt = jwtDecoder.decode(token);
	    return jwt.getClaims();
	}

	public static String extractUserId(String token) {
	    return extractClaims(token).get("userId").toString();
	}
}
