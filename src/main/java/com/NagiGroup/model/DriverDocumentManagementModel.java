package com.NagiGroup.model;

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
public class DriverDocumentManagementModel {
	@Nullable
	private String driver_name; // integer,
	@Nullable
	private String parent_folder_name; // integer,

	@Nullable
	private String sub_folder_name; // character varying,
	
	@Nullable
	private int sub_folder_id; // character varying,
	

	@Nullable
	private int driver_id; // character varying,
	
	@Nullable
	private MultipartFile roc; // character varying,
	
	@Nullable
	private MultipartFile pod; // character varying,
	
	@Nullable
	private MultipartFile fuel_reciept; // character varying,
	
	@Nullable
	private MultipartFile annual_dot_inspection; // character varying,
	
	
	@Nullable
	private MultipartFile truck_and_trailer_repair; // character varying,
	
	@Nullable
	private MultipartFile ifta_quaterly; // character varying,
	
	@Nullable
	private MultipartFile truck_trailer_serivices; // character varying,
	
	@Nullable
	private MultipartFile driver_equipment_information; // character varying,
	

}
