package com.kosa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kosa.db.DBConnection;
import com.kosa.dto.ProjectMemberDTO;
import com.kosa.session.Session;

import oracle.jdbc.OracleTypes;

public class ProjectMemberDAO {
	private Connection conn = DBConnection.getConnection();
	private String runSP;
	private CallableStatement callableStatement;

	public boolean insertProjectMember(int projectId, Map<String, String> projectMemberMap) {
		if (projectMemberMap.isEmpty()) {
			return false;
		} else {
			runSP = "{ call project_member_pack.insert_project_member(?, ?, ?) }";

			try {

				callableStatement = conn.prepareCall(runSP);

				int leader;

				for (String userId : projectMemberMap.keySet()) {
					if (userId.equals(Session.getUserID())) // 로그인한 사용자와 같은 아이디면 팀장
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

	public ArrayList<ProjectMemberDTO> getProjectMembersByProjectId(int projectId) {
		ArrayList<ProjectMemberDTO> projectMembers = new ArrayList<>();

		runSP = "{ call project_member_pack.get_project_members_by_project_id(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, projectId);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

			try {
				callableStatement.executeQuery();
				ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

				while (resultSet.next()) {
					ProjectMemberDTO projectMember = new ProjectMemberDTO(resultSet.getInt(1), resultSet.getBoolean(2),
							resultSet.getString(3), projectId);
					projectMembers.add(projectMember);
				}

			} catch (SQLException e) {
				System.out.println("프로시저에서 에러 발생!");

				System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectMembers;
	}

	public int checkProjectMemberInProject(int projectId, String userId) {
		
		runSP = "{ call project_member_pack.check_project_member_in_project(?, ?, ?) }";

		int isExist = -1;
		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, projectId);
			callableStatement.setString(2, userId);
			callableStatement.registerOutParameter(3, java.sql.Types.NUMERIC);

			callableStatement.executeQuery();

			isExist = callableStatement.getInt(3);

		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}

	public void insertProjectMember(int projectId, String userId) {
		
		runSP = "{ call project_member_pack.insert_project_member(?, ?) }";

		try {

			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, projectId);
			callableStatement.setString(2, userId);

			callableStatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
