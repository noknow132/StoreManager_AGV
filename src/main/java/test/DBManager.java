package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String url = "jdbc:sqlserver://localhost:1433;DatabaseName=storemanager";
	private static final String user = "sa";
//	private static final String pwd = "sa";
	private static final String pwd = "";
	Connection conn = null;

	public Connection getConn() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public void closeAll(Connection conn, Statement sm, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (sm != null) {
				sm.close();
				sm = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	// public void closeConn(Connection conn) {
	// if (conn != null) {
	// try {
	// conn.close();
	// conn = null;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public void closeStatement(Statement sm) {
	// if (sm != null) {
	// try {
	// sm.close();
	// sm = null;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public void closeResultSet(ResultSet rs) {
	// if (rs != null) {
	// try {
	// rs.close();
	// rs = null;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
}
