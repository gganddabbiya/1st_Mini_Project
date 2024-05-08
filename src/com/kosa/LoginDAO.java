package com.kosa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginDAO {
    public static int login(String username, String password) {
        Connection conn = DBConnection.getConnection();
        CallableStatement cstmt = null;
        int result = -1;

        try {
            cstmt = conn.prepareCall("{call login_proc(?, ?, ?)}");
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
            cstmt.execute();
            result = cstmt.getInt(3);
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
