package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;

import db.DBConnection;
import dto.AccountDTO;

public class AccountDAO {
	private Connection conn = DBConnection.getConnection();
	private String runSP;
	private CallableStatement callableStatement;
	private ResultSet resultSet;
	
//	public AccountDTO getAccountByUserId(String userId) { 
//		runSP = "{ call account_pack.get_account_by_user_id(?, ?) }";
//    	
//		AccountDTO loginAccount = new AccountDTO();
//		
//    	try {
//			callableStatement = conn.prepareCall(runSP);
//	
//			callableStatement.setString(1, userId);
//			
//			Struct accountRowType = conn.createStruct("ACCOUNT_ROWTYPE", new Object[0]); 
//			
//			callableStatement.registerOutParameter(2, oracle.jdbc.OracleTypes.STRUCT, "ACCOUNT_ROWTYPE");
//			
//			try {
//				callableStatement.executeQuery();
//				
//				Struct accountStruct = (Struct) callableStatement.getObject(2);
//				Object[] attributes = accountStruct.getAttributes();
//				
//				loginAccount.setUserId((String) attributes[0]);
//				loginAccount.setUserName((String) attributes[1]);
//				loginAccount.setEmail((String) attributes[2]);
//				loginAccount.setPassword((String) attributes[3]);
//	
//			} catch (SQLException e) {
//				System.out.println("프로시저에서 에러 발생!");
//				System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	return loginAccount;
//	}
	
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
