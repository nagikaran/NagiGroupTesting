
package com.NagiGroup.utility;

import com.NagiGroup.config.GoogleDriveService;

public class Test {
	public static void main(String[] args) {
//		 String credentialsPath = System.getenv("GOOGLE_DRIVE_CREDENTIALS");
//	        System.out.println("Google Drive Credentials Path: " + credentialsPath);
//	        System.out.println("Env Variable: " + System.getenv("GOOGLE_DRIVE_CREDENTIALS"));
	        
		try {
            // Replace this with your actual Google Drive root folder ID
            String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";

            // Step 1: Create a "2025" Year folder inside the root folder
            String yearFolderId = GoogleDriveService.createFolder("2025", googleDriveRootFolderId);
            System.out.println("✅ Year Folder Created: " + yearFolderId);

            // Step 2: Create a "March" Month folder inside the "2025" folder
            String monthFolderId = GoogleDriveService.createFolder("March", yearFolderId);
            System.out.println("✅ Month Folder Created: " + monthFolderId);

            // Step 3: Create a "John_Doe" Driver folder inside the "March" folder
            String driverFolderId = GoogleDriveService.createFolder("John_Doe", monthFolderId);
            System.out.println("✅ Driver Folder Created: " + driverFolderId);

            // Step 4: Create a "POD" Subfolder inside the "John_Doe" folder
            String subFolderId = GoogleDriveService.createFolder("POD", driverFolderId);
            System.out.println("✅ Subfolder Created: " + subFolderId);

        } catch (Exception e) {
            e.printStackTrace();
        }
	        
	}

}
