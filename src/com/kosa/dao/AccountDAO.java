package com.kosa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.kosa.db.DBConnection;

public class AccountDAO {
	private Connection conn = DBConnection.getConnection();
	private String runSP;
	private CallableStatement callableStatement;
	
	public String getUserNameByUserId(String userId) {
		runSP = "{ call account_pack.get_user_name_by_user_id(?, ?) }";
    	
		String userName = "";
    	try {
			callableStatement = conn.prepareCall(runSP);
	
			callableStatement.setString(1, userId);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			
			try {
				callableStatement.executeQuery();
				
				userName = callableStatement.getString(2);
	
			} catch (SQLException e) {
				System.out.println("프로시저에서 에러 발생!");
				System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return userName;
	}
}
