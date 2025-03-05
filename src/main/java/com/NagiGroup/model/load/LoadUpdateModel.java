package com.NagiGroup.model.load;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoadUpdateModel {
	

	private int load_id;
	private String loadNumber;
	private String source;
	private String destination;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  pick_up_date; // timestamp without time zone,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  delievery_date; // timestamp without time zone,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  earliest_time_arrival; // timestamp without time zone,
	private String pick_up_date_string;
	private String delievery_date_string;
	private String earliest_time_arrival_string;
	private int driver_id;
	private String driver_name;
	private double base_price;
	private double final_price;
	private int assign_to;
	private String oldFileName;
	 @Nullable
	  private MultipartFile roc;
	
	



}
