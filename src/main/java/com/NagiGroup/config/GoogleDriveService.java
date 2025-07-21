package com.NagiGroup.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleDriveService.class);
	

    private static final String APPLICATION_NAME = "NagiGroupLive";
    private static final String SERVICE_ACCOUNT_KEY_PATH = System.getenv("GOOGLE_DRIVE_CREDENTIALS"); 

    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        if (SERVICE_ACCOUNT_KEY_PATH == null || SERVICE_ACCOUNT_KEY_PATH.isEmpty()) {
            throw new IllegalStateException("GOOGLE_DRIVE_CREDENTIALS environment variable is not set!");
        }

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        try (InputStream credentialsStream = Files.newInputStream(Paths.get(SERVICE_ACCOUNT_KEY_PATH))) {
            GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));

            return new Drive.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
    }

    public static String uploadFileToGoogleDrive(java.io.File file, String mimeType, String parentFolderId)
            throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(file.getName());

        if (parentFolderId != null) {
            fileMetadata.setParents(Collections.singletonList(parentFolderId));
        }

        FileContent mediaContent = new FileContent(mimeType, file);
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute();

        return uploadedFile.getId();
    }
    
    
    public static String getFolderIdByName(String folderName, String parentFolderId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        String query = "mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'";
        if (parentFolderId != null) {
            query += " and '" + parentFolderId + "' in parents";
        }

        Drive.Files.List request = driveService.files().list()
                .setQ(query)
                .setFields("files(id)")
                .setPageSize(1);

        List<File> files = request.execute().getFiles();

        if (!files.isEmpty()) {
            return files.get(0).getId();  // Folder already exists, return its ID
        }

        return null;  // Folder doesn't exist
    }

    
    public static String createOrGetFolder(String folderName, String parentFolderId) throws IOException, GeneralSecurityException {
        String folderId = getFolderIdByName(folderName, parentFolderId);

        if (folderId != null) {
            System.out.println("📁 Folder '" + folderName + "' already exists. ID: " + folderId);
            return folderId;  // Return existing folder ID
        }	

        Drive driveService = getDriveService();

        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");

        if (parentFolderId != null) {
            folderMetadata.setParents(Collections.singletonList(parentFolderId));
        }

        File folder = driveService.files().create(folderMetadata)
                .setFields("id")
                .execute();

        System.out.println("✅ Folder Created: " + folderName + " | ID: " + folder.getId());
        return folder.getId();  // Return new folder ID
    }
    public static String uploadFileToDynamicFolder(java.io.File file, String mimeType, String parentFolderName)
            throws IOException, GeneralSecurityException {

        // Ensure the parent folder exists (or create it)
        String parentFolderId = createOrGetFolder(parentFolderName, null);

        Drive driveService = getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        fileMetadata.setParents(Collections.singletonList(parentFolderId));

        FileContent mediaContent = new FileContent(mimeType, file);
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute();

        System.out.println("✅ File Uploaded: " + file.getName() + " | ID: " + uploadedFile.getId());
        return uploadedFile.getId();
    }

    public static String createFolder(String folderName, String parentFolderId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");

        if (parentFolderId != null) {
            folderMetadata.setParents(Collections.singletonList(parentFolderId));
        }

        File folder = driveService.files().create(folderMetadata)
                .setFields("id")
                .execute();

        return folder.getId();
    }

    public static String uploadFileToDrive(MultipartFile file, String parentFolderId) throws IOException, GeneralSecurityException
    
    {
        // Convert MultipartFile to InputStream
        InputStream inputStream = file.getInputStream();
        Drive driveService = getDriveService();
        // Set metadata
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename()); // File name from request
        fileMetadata.setParents(Collections.singletonList(parentFolderId)); // Parent folder ID

        // Define file content
        InputStreamContent mediaContent = new InputStreamContent(file.getContentType(), inputStream);

        // Upload file
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, name, webViewLink")
                .execute();

        return uploadedFile.getId();
    }

    public static String getOrCreateFolder(String folderName, String parentFolderId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        // Query to find an existing folder by name and parent ID
        String query = String.format("mimeType='application/vnd.google-apps.folder' and name='%s' and '%s' in parents and trashed=false", 
                                      folderName, parentFolderId);

        FileList result = driveService.files().list()
                .setQ(query)
                .setFields("files(id, name)")
                .execute();

        if (!result.getFiles().isEmpty()) {
            // Pick the first folder found to avoid duplicate creations
            return result.getFiles().get(0).getId();
        }

        // Folder does not exist, create a new one
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");
        folderMetadata.setParents(Collections.singletonList(parentFolderId));

        File createdFolder = driveService.files().create(folderMetadata)
                .setFields("id")
                .execute();

        return createdFolder.getId(); // Return the new folder ID
    }

    public static String findFileIdInFolder(String fileName, String folderId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        String query = String.format("name='%s' and '%s' in parents and trashed=false", fileName, folderId);
        FileList result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<com.google.api.services.drive.model.File> files = result.getFiles();

        if (files != null && !files.isEmpty()) {
            String fileId = files.get(0).getId();
            logger.info("✅ Found file: '" + fileName + "' with ID: " + fileId + " in folder: " + folderId);
            return fileId;
        } else {
        	logger.info("❌ File not found: '" + fileName + "' in folder: " + folderId);
        }

        return null;
    }

    public static void moveFileToFolder(String fileId, String newFolderId, String fileName) {
        try {
            Drive driveService = getDriveService();

            // Get current parent folders of the file
            File file = driveService.files().get(fileId)
                    .setFields("parents")
                    .execute();

            List<String> previousParents = file.getParents();
            String previousParentsString = previousParents != null ? String.join(",", previousParents) : "";

            // Move the file to the new folder
            driveService.files().update(fileId, null)
                    .setAddParents(newFolderId)
                    .setRemoveParents(previousParentsString)
                    .setFields("id, parents")
                    .execute();

            logger.info("✅ File '" + fileName + "' moved successfully to folder ID: " + newFolderId);
        } catch (IOException e) {
        	logger.info("❌ Failed to move file '" + fileName + "' to folder ID: " + newFolderId);
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    public static void uploadFileToDriveWithReplace(MultipartFile file, String fileName, String folderId) throws IOException, GeneralSecurityException {
        Drive service = getDriveService(); // assumes you have a method for this

        // 1. Check if a file with the same name exists in the folder
        String query = String.format("name = '%s' and '%s' in parents and trashed = false", fileName, folderId);
        FileList result = service.files().list().setQ(query).setFields("files(id, name)").execute();

        // 2. If found, delete the existing file
        for (com.google.api.services.drive.model.File existingFile : result.getFiles()) {
            service.files().delete(existingFile.getId()).execute();
        }

        // 3. Upload the new file
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        FileContent mediaContent = new FileContent(file.getContentType(), convertMultiPartToFile(file));
        service.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
    }
    public static java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
        java.io.File convFile = java.io.File.createTempFile("upload_", "_" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public static void deleteAndUploadFileToDrive(MultipartFile file, String newFileName, String folderId, String oldFileName) throws IOException, GeneralSecurityException {
        Drive service = getDriveService();

        // Step 1: Delete old file (if exists)
        if (oldFileName != null && !oldFileName.isEmpty()) {
            String oldQuery = String.format("name = '%s' and '%s' in parents and trashed = false", oldFileName, folderId);
            FileList oldResult = service.files().list().setQ(oldQuery).setFields("files(id, name)").execute();
            for (com.google.api.services.drive.model.File oldFile : oldResult.getFiles()) {
                service.files().delete(oldFile.getId()).execute();
            }
        }

        // Step 2: Delete existing new file (if it somehow exists)
        String query = String.format("name = '%s' and '%s' in parents and trashed = false", newFileName, folderId);
        FileList result = service.files().list().setQ(query).setFields("files(id, name)").execute();
        for (com.google.api.services.drive.model.File existingFile : result.getFiles()) {
            service.files().delete(existingFile.getId()).execute();
        }

        // Step 3: Upload the new file
        File fileMetadata = new File();
        fileMetadata.setName(newFileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        FileContent mediaContent = new FileContent(file.getContentType(), convertMultiPartToFile(file));
        service.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
    }
    
    
    public static boolean deleteFileFromLocalAndDrive(String localFilePath, String driveFolderId, String fileNameInDrive) {
        boolean isLocalDeleted = false;
        boolean isDriveDeleted = false;

        // 1. Delete from local system
        try {
        	java.io.File localFile = new java.io.File(localFilePath);
            if (localFile.exists()) {
                isLocalDeleted = localFile.delete();
                logger.info("Local file deleted: " + localFilePath);
            } else {
                logger.warn("Local file not found: " + localFilePath);
            }
        } catch (Exception e) {
            logger.error("Error deleting local file: " + e.getMessage(), e);
        }

        // 2. Delete from Google Drive
        try {
            String fileId = GoogleDriveService.searchFileIdInFolder(fileNameInDrive, driveFolderId);
            if (fileId != null) {
                GoogleDriveService.deleteFile(fileId);
                isDriveDeleted = true;
                logger.info("Google Drive file deleted: " + fileNameInDrive);
            } else {
                logger.warn("File not found in Google Drive: " + fileNameInDrive);
            }
        } catch (Exception e) {
            logger.error("Error deleting file from Google Drive: " + e.getMessage(), e);
        }

        return isLocalDeleted && isDriveDeleted;
    }
    public static String searchFileIdInFolder(String fileName, String parentFolderId) throws IOException, GeneralSecurityException {
    	 Drive service = getDriveService();
        String query = "name = '" + fileName + "' and '" + parentFolderId + "' in parents and trashed = false";
        FileList result = service.files().list()
            .setQ(query)
            .setFields("files(id, name)")
            .execute();
        List<com.google.api.services.drive.model.File> files = result.getFiles();
        return files.isEmpty() ? null : files.get(0).getId();
    }
    public static void deleteFile(String fileId) throws IOException, GeneralSecurityException {
        Drive service = getDriveService();
        service.files().delete(fileId).execute();
    }


    
}
