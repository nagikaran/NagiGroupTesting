package com.NagiGroup.dto.subFolder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubFolderDto {
	
	private int  sub_folder_id;
	private String sub_folder_name;
	public int getSub_folder_id() {
		return sub_folder_id;
	}
	public void setSub_folder_id(int sub_folder_id) {
		this.sub_folder_id = sub_folder_id;
	}
	public String getSub_folder_name() {
		return sub_folder_name;
	}
	public void setSub_folder_name(String sub_folder_name) {
		this.sub_folder_name = sub_folder_name;
	}
	
	

}
