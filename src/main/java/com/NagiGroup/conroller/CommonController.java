package com.NagiGroup.conroller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.NagiGroup.dto.UserDetailsDto;
import com.NagiGroup.dto.UserDto;
import com.NagiGroup.repository.LicenceKeyRepository;
import com.NagiGroup.security.TokenDecoder;
import com.NagiGroup.service.LicenceKeyService;
import com.google.gson.Gson;


@Component
public class CommonController {
	 private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	 public static String getFileNameWithoutExtension(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            return null;
        }
        
        String originalFileName = file.getOriginalFilename();
        int lastDotIndex = originalFileName.lastIndexOf(".");
        
        if (lastDotIndex == -1) {
            return originalFileName; // No extension found, return as is
        }
        
        String substring = originalFileName.substring(0, lastDotIndex);
        
        return originalFileName.substring(0, lastDotIndex);
    }
	
	public  int getUserDtoDataFromToken(HttpServletRequest request) {		
		logger.info("CommonController : " + request.getRequestURI());
		UserDto uDto = null;
		String jwtToken = null;
		Gson g = new Gson();
		LicenceKeyService licenceKeyService= null;
		new LicenceKeyRepository(null);
//		TokenDecoder tokenDecoder = new TokenDecoder(licenceKeyService);
		
		try {
			final String requestTokenHeader = request.getHeader("Authorization");
			System.out.println("requestTokenHeader: "+requestTokenHeader);
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken= requestTokenHeader.substring(7);
				System.out.println("jwtToken: "+jwtToken);
				UserDetailsDto userDtoDataFromToken = TokenDecoder.getUserDtoDataFromToken(jwtToken);
//				uDto = g.fromJson(this.getAllClaimsFromToken(jwtToken).get("2").toString(),
//						UserDto.class);
				System.out.println("UserDetailsDto: "+userDtoDataFromToken);
				return userDtoDataFromToken.getUser_id();
			}
			else {
				return 0;
			}
			
		} catch (Exception e) {	
			e.printStackTrace();
			logger.info("CommonController : Error At : getUserDtoDataFromToken : " + e);
			return 0;
		}
	}
	
	
	 public static String renameFileWithExtension(MultipartFile file,String newFileName) {
		 logger.info("CommonController : renameFileWithExtension start ");
	        if (file == null || file.getOriginalFilename() == null) {
	            return null;
	        }
	        if (file == null || file.getOriginalFilename() == null || newFileName == null || newFileName.isEmpty()) {
	            return null;
	        }
	        
	        String originalFileName = file.getOriginalFilename();
	        int lastDotIndex = originalFileName.lastIndexOf(".");
	        
	        if (lastDotIndex == -1) {
	            return newFileName; // No extension found, return new name as is
	        }
	        
	        String extension = originalFileName.substring(lastDotIndex); // Extract the extension
	        logger.info("CommonController : newFileName : "+newFileName+extension);
	        logger.info("CommonController : renameFileWithExtension end ");
	        return newFileName + extension; // Append the new name with the extension
	    }

	public static String getNewFileNameWithoutExtension(String newFileName) {
		 logger.info("CommonController : getNewFileNameWithoutExtension start ");

        if (newFileName == null || newFileName.equals("")) {
            return null;
        }
        
        int lastDotIndex = newFileName.lastIndexOf(".");
        
        if (lastDotIndex == -1) {
            return newFileName; // No extension found, return as is
        }
        
        String substring = newFileName.substring(0, lastDotIndex);
        logger.info("CommonController : newFileName withouth extension : "+substring);
        logger.info("CommonController : getNewFileNameWithoutExtension end ");
        return newFileName.substring(0, lastDotIndex);
    
		
	}
	public static MultipartFile convertFileToMultipartFile(File file) throws IOException {
        FileItem fileItem = new DiskFileItem(
            "file", 
            "application/octet-stream", 
            true, 
            file.getName(), 
            (int) file.length(), 
            file.getParentFile()
        );

        try {
            fileItem.getOutputStream().write(FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            throw new IOException("Error converting file to MultipartFile", e);
        }

        return new CommonsMultipartFile(fileItem);
    }
	
	
}
