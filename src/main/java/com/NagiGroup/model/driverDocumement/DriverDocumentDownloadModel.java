package com.NagiGroup.model.driverDocumement;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.model.load.LoadCompletionModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverDocumentDownloadModel {
	
	private String year;
	private String month;
	private String sub_folder_name;
	private String driver_name;
	private String document_name;

}
