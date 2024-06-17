package com.kosa.db;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class JdbcConnection {
	@SuppressWarnings("unused")
	public static void main(String args[]) throws SQLException {

		OracleDataSource ods = new OracleDataSource();

		Connection conn = DBConnection.getConnection();
	}
}