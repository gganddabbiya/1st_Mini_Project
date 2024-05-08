package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import db.DBConnection;

public class ProjectMemberDAO {
	private Connection conn = DBConnection.getConnection();
	private String runSP;
	private CallableStatement callableStatement;

	public boolean insertProjectMember(int projectId, Map<String, String> projectMemberMap) { // 수정해야 됨
		if (projectMemberMap.isEmpty()) {
			return false;
		} else {
			String runSP = "{ call project_member_pack.insert_project_member(?, ?, ?) }";

			try {

				callableStatement = conn.prepareCall(runSP);

				int leader;
				
				for (String userId : projectMemberMap.keySet()) {
					if (userId.equals("admin")) // 로그인한 사용자와 같은 아이디면 팀장
						leader = 1;
					else
						leader = 0;

					callableStatement.setString(1, userId);
					callableStatement.setInt(2, leader);					
					callableStatement.setInt(3, projectId);
					callableStatement.addBatch();
				}
				callableStatement.executeBatch();

			} catch (SQLException e) {
				System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
