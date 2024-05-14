package com.kosa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kosa.db.DBConnection;

import oracle.jdbc.OracleTypes;

public class ProjectDAO {
	private Connection conn = DBConnection.getConnection();
	private String runSP;
	private CallableStatement callableStatement;

	public int insertProject(String projectName) { // insert한 후 project_id 리턴 
		runSP = "{ call project_pack.insert_project(?, ?) }";
		
		int projectId = 0;

		try {

			callableStatement = conn.prepareCall(runSP);

			callableStatement.setString(1, projectName);
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);

			callableStatement.executeUpdate();
				
			projectId = callableStatement.getInt(2);
		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectId;
	}

	public Map<Integer, String> getProjectsByUserId(String userId) {
		runSP = "{ call project_pack.get_projets_by_user_id(?, ?) }";

		Map<Integer, String> projects = new HashMap<>();
		
		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setString(1, userId);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

			try {
				callableStatement.executeQuery();
				ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

				while (resultSet.next()) {
					projects.put(resultSet.getInt(1), resultSet.getString(2));
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
		return projects;
	}

}
