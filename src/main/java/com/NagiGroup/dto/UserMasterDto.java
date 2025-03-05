package com.NagiGroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class UserMasterDto {
	
	private int user_id;
	private String userName;
	private String user_password;

}
