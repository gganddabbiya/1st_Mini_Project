package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnection;
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

	public ArrayList<String> getProjectNameByUserId(String userId) {
		runSP = "{ call project_pack.get_projet_name_by_user_id(?, ?) }";

		ArrayList<String> projectNameList = new ArrayList<>();
		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setString(1, userId);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

			try {
				callableStatement.executeQuery();
				ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

				while (resultSet.next()) {
					projectNameList.add(resultSet.getString(1));
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
		return projectNameList;
	}

}
