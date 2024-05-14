package com.kosa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskDTO {
	private int taskId;
	private String taskName;
	private String priority;
	private boolean completion;
	private int projectId;
	private int memberId;
	private int feedbackTotalScore;
	private boolean retry;
}
