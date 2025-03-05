package com.NagiGroup.helperClass;

import org.springframework.web.multipart.MultipartFile;

public class DocumentDetails {
	private   String documentName;
    private   String originalDocumentName;
    private   String subFolderName; 
    private MultipartFile file;
    private int subFolderId;
    

    public DocumentDetails(String documentName, String originalDocumentName,String subFolderName,MultipartFile file,int subFolderId) {
        this.documentName = documentName;
        this.originalDocumentName = originalDocumentName;
        this.subFolderName=subFolderName;
        this.file=file;
        this.subFolderId=subFolderId;
    }

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getOriginalDocumentName() {
		return originalDocumentName;
	}

	public void setOriginalDocumentName(String originalDocumentName) {
		this.originalDocumentName = originalDocumentName;
	}

	public String getSubFolderName() {
		return subFolderName;
	}

	public void setSubFolderName(String subFolderName) {
		this.subFolderName = subFolderName;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public int getSubFolderId() {
		return subFolderId;
	}

	public void setSubFolderId(int subFolderId) {
		this.subFolderId = subFolderId;
	}
    
    
    
}
