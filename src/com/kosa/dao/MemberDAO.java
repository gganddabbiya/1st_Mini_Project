package com.kosa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.kosa.db.DBConnection;

public class MemberDAO {
    public static int registerMember(String username, String password, String email, String name) {
        Connection conn = DBConnection.getConnection();
        CallableStatement cstmt = null;
        int result = -1; // 초기화

        try {
            cstmt = conn.prepareCall("{call account_management_pkg.register_member(?, ?, ?, ?, ?)}");
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.setString(3, email);
            cstmt.setString(4, name);
            cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
            cstmt.execute();
            result = cstmt.getInt(5); // 프로시저의 결과값 가져오기
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cstmt != null) {
                    cstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
}
