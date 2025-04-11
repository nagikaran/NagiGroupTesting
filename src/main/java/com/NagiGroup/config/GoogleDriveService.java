package com.NagiGroup.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

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

        return "Uploaded File ID: " + uploadedFile.getId() + " | Link: " + uploadedFile.getWebViewLink();
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

    
}
