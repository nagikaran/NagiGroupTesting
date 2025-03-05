package com.NagiGroup.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.repository.LoadRepository;

public class CommonUtility {
	 private static final Logger logger = LoggerFactory.getLogger(CommonUtility.class);
	public static boolean saveDocument(MultipartFile documentPath, String loadNumber, String basePath,String folderName) {
		String fileName = "";
		String sourcePath = "";
		basePath=basePath+loadNumber+"/"+folderName+"/";
		System.out.println("basePath: "+basePath);
		boolean isCreated = false;
		if (documentPath != null) {
			if (loadNumber != null) {
				isCreated = createDynamicFolder("", basePath);
				fileName = documentPath.getOriginalFilename();
				if (isCreated) {
					sourcePath = basePath + fileName;
					System.out.println("sourcePath: "+sourcePath);
					try {
						documentPath.transferTo(new File(sourcePath));
					} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Folder Not Created : Tender Id Is Null");
				isCreated = false;
			}
		}
		return isCreated;
	}
	
	public static boolean createDynamicFolder(String folderName, String basePath) {
		boolean isCreated = false;
		try {
			System.out.println("basePath : " + basePath);
			String fullPath = basePath + folderName;
			System.out.println("fullPath : " + fullPath);
			File directory = new File(fullPath);
			System.out.println("Directory : " + directory.getPath());
			if (!directory.exists()) {
				if (directory.mkdirs()) {
					isCreated = true;
					System.out.println("Directory created successfully: " + fullPath);
				} else {
					isCreated = false;
					System.out.println("Failed to create directory: " + fullPath);
				}
			} else {
				isCreated = true;
				System.out.println("Directory already exists: " + fullPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error At : createDynamicFolder : " + e);
		}
		return isCreated;
	}
	
	public static LocalDateTime parseDateString(String dateString) {
	    if (dateString == null || dateString.isEmpty()) {
	        return null; // Handle null or empty case if needed
	    }
	    
	    dateString = dateString.replace("%20", " ").trim();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    
	    return LocalDateTime.parse(dateString, formatter);
	}
	 public static boolean deleteFile(String filePath) {
	        try {
	            Path path = Paths.get(filePath);
	            File file = path.toFile();

	            if (file.exists()) {
	                boolean deleted = Files.deleteIfExists(path);
	                if (deleted) {
	                    logger.info("File deleted successfully: " + filePath);
	                    return true;
	                } else {
	                    logger.warn("Failed to delete file: " + filePath);
	                    return false;
	                }
	            } else {
	                logger.warn("File not found: " + filePath);
	                return false;
	            }
	        } catch (Exception e) {
	            logger.warn("Error deleting file: " + e.getMessage());
	            return false;
	        }
	    }
	
}
