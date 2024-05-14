package com.kosa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.kosa.db.DBConnection;


public class TimeDAO {
	private Connection conn = DBConnection.getConnection();
	private String runSP;
	private CallableStatement callableStatement;
	
	public int getTotalTimeByMemberId(int memberId) {		
		runSP = "{ call time_pack.get_total_time_by_member_id(?, ?) }";
		
		int totalTimeSecond = 0;
		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, memberId);
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);

			callableStatement.executeQuery();
			
			totalTimeSecond = callableStatement.getInt(2);
		}
		catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalTimeSecond;
		
	}
}
