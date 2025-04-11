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
public class LoadStatusModel {
	private int load_id;
	private String load_number;
	private int driver_id;
	@Nullable
	 private MultipartFile roc;
	
}
