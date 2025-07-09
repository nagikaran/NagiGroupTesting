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
public class LoadAdditionalCharges {
	
	private int load_id;
	private String load_number;
	private double lumper_value;
	private double detention_value;
	private double scale_value;
	private double extra_stop_charge;
	private double trailer_wash;
	private String driver_name;
	private String driver_id;
	private int company_id;
	private double amount;
	private double layover;
	

	@Nullable
	private MultipartFile roc;
	

}
