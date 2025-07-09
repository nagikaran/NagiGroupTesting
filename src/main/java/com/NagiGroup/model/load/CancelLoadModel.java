package com.NagiGroup.model.load;

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
public class CancelLoadModel {
	
	private int load_id;
	private String old_load_number;
	private String new_load_number;
	private boolean tonu;
	private Double tonu_charges;
	private String driver_name;
	private int company_id;
	private int requesting_user;
	@Nullable
	private MultipartFile roc;

}
