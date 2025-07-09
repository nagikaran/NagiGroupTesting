package com.NagiGroup.dto.load;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoadStatusSummaryDto {
	private int pendingCount;
    private int assignedCount;
    private int inProgressCount;
    private int completedCount;
    
	public int getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}
	public int getAssignedCount() {
		return assignedCount;
	}
	public void setAssignedCount(int assignedCount) {
		this.assignedCount = assignedCount;
	}
	public int getInProgressCount() {
		return inProgressCount;
	}
	public void setInProgressCount(int inProgressCount) {
		this.inProgressCount = inProgressCount;
	}
	public int getCompletedCount() {
		return completedCount;
	}
	public void setCompletedCount(int completedCount) {
		this.completedCount = completedCount;
	}
    
}
