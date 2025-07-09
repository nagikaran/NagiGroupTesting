package com.NagiGroup.dto.LoadPendingCharges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class LoadPendingChargesDTO {
	 private Integer loadPendingChargesId;
	    private Integer loadId;
	    private String loadNumber;
	    private Double lumperValue;
	    private Integer lumperPaidBy;	
	    private Double detentionValue;
	    private Double scaleValue;
	    private Double extraStopCharge;
	    private Double trailerWashValue;
	    private Boolean isDetention;
	    private Boolean isLayover;
	    private Double layover;
}
