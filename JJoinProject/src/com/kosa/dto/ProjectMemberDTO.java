package com.kosa.dto;

import javax.swing.JProgressBar;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ProjectMemberDTO {
	private int memberId;
	private boolean leader;
	private String userId;
	private int projectId;
	
	private JProgressBar progressBar;

	public ProjectMemberDTO() {
		
	};
	
	public ProjectMemberDTO(int memberId, boolean leader, String userId, int projectId) {
		this.memberId = memberId;
		this.leader = leader;
		this.userId = userId;
		this.projectId = projectId;
	}
}
