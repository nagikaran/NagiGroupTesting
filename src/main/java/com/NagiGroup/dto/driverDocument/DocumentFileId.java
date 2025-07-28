package com.NagiGroup.dto.driverDocument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DocumentFileId {
private String drive_file_id;

public String getDrive_file_id() {
	return drive_file_id;
}

public void setDrive_file_id(String drive_file_id) {
	this.drive_file_id = drive_file_id;
}

}
