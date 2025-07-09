package com.NagiGroup.dto.load;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoadAssignmentDocumentDto {
	    private Integer loadId;
	    private String loadNumber;
	    private String documentPath;
	    private String originalDocumentName;
	    private String driverName;
	    private int driver_documents_id;
		public Integer getLoadId() {
			return loadId;
		}
		public void setLoadId(Integer loadId) {
			this.loadId = loadId;
		}
		public String getLoadNumber() {
			return loadNumber;
		}
		public void setLoadNumber(String loadNumber) {
			this.loadNumber = loadNumber;
		}
		public String getDocumentPath() {
			return documentPath;
		}
		public void setDocumentPath(String documentPath) {
			this.documentPath = documentPath;
		}
		public String getOriginalDocumentName() {
			return originalDocumentName;
		}
		public void setOriginalDocumentName(String originalDocumentName) {
			this.originalDocumentName = originalDocumentName;
		}
		public String getDriverName() {
			return driverName;
		}
		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}
		public int getDriver_documents_id() {
			return driver_documents_id;
		}
		public void setDriver_documents_id(int driver_documents_id) {
			this.driver_documents_id = driver_documents_id;
		}
	    
	    
	    
	    
}
