package com.NagiGroup.model;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class DriverDocumentManagementUpdateModel {
	
	private int driver_documents_id;
	
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

	public int getDriver_documents_id() {
		return driver_documents_id;
	}

	public void setDriver_documents_id(int driver_documents_id) {
		this.driver_documents_id = driver_documents_id;
	}

	public String getDriver_name() {
		return driver_name;
	}

	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	public String getParent_folder_name() {
		return parent_folder_name;
	}

	public void setParent_folder_name(String parent_folder_name) {
		this.parent_folder_name = parent_folder_name;
	}

	public String getSub_folder_name() {
		return sub_folder_name;
	}

	public void setSub_folder_name(String sub_folder_name) {
		this.sub_folder_name = sub_folder_name;
	}

	public int getSub_folder_id() {
		return sub_folder_id;
	}

	public void setSub_folder_id(int sub_folder_id) {
		this.sub_folder_id = sub_folder_id;
	}

	public int getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}

	public MultipartFile getRoc() {
		return roc;
	}

	public void setRoc(MultipartFile roc) {
		this.roc = roc;
	}

	public MultipartFile getPod() {
		return pod;
	}

	public void setPod(MultipartFile pod) {
		this.pod = pod;
	}

	public MultipartFile getFuel_reciept() {
		return fuel_reciept;
	}

	public void setFuel_reciept(MultipartFile fuel_reciept) {
		this.fuel_reciept = fuel_reciept;
	}

	public MultipartFile getAnnual_dot_inspection() {
		return annual_dot_inspection;
	}

	public void setAnnual_dot_inspection(MultipartFile annual_dot_inspection) {
		this.annual_dot_inspection = annual_dot_inspection;
	}

	public MultipartFile getTruck_and_trailer_repair() {
		return truck_and_trailer_repair;
	}

	public void setTruck_and_trailer_repair(MultipartFile truck_and_trailer_repair) {
		this.truck_and_trailer_repair = truck_and_trailer_repair;
	}

	public MultipartFile getIfta_quaterly() {
		return ifta_quaterly;
	}

	public void setIfta_quaterly(MultipartFile ifta_quaterly) {
		this.ifta_quaterly = ifta_quaterly;
	}

	public MultipartFile getTruck_trailer_serivices() {
		return truck_trailer_serivices;
	}

	public void setTruck_trailer_serivices(MultipartFile truck_trailer_serivices) {
		this.truck_trailer_serivices = truck_trailer_serivices;
	}

	public MultipartFile getDriver_equipment_information() {
		return driver_equipment_information;
	}

	public void setDriver_equipment_information(MultipartFile driver_equipment_information) {
		this.driver_equipment_information = driver_equipment_information;
	}
	
	
	
	

}
