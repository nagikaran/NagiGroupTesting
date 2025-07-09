package com.NagiGroup.model;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverDocumentManagementBulkInsertModel {
	@Nullable
	private String driver_name; // integer,
	@Nullable
	private String load_number;
	
	
	
	@Nullable
	private String month;

	@Nullable
	private int driver_id; // character varying,
	
	@Nullable
    private List<MultipartFile> files;
	
	@Nullable
    private List<MultipartFile> POD;
}
