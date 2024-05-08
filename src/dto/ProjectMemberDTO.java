package dto;

import lombok.Data;

@Data
public class ProjectMemberDTO {
	private int memberId;
	private boolean leader;
	private String userId;
	private int projectId;
}
