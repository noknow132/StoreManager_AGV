package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.entity.ChainWeights;

public class Test {
	private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String url = "jdbc:sqlserver://192.168.5.41:1433;DatabaseName=storemanager";
	private static final String user = "sa";
	private static final String pwd = "";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Connection conn = DriverManager.getConnection(url, user, pwd);
			String sql = "select * from chain_weights";
			PreparedStatement pstm = conn.prepareStatement(sql);	
		    ResultSet rs = null;
            rs = pstm.executeQuery();
			while(rs.next())
			{
				ChainWeights cws = new ChainWeights();
				//cws.setChainWeight1(rs.getFloat("chainWeight1"));
				/*cws.setChainWeight2(rs.getFloat("chainWeight2"));
				cws.setChainWeight3(rs.getFloat("chainWeight3"));
				cws.setChainWeight4(rs.getFloat("chainWeight4"));
				cws.setChainWeight5(rs.getFloat("chainWeight5"));
				cws.setChainWeight6(rs.getFloat("chainWeight6"));
				cws.setChainWeight7(rs.getFloat("chainWeight7"));
				cws.setChainWeight8(rs.getFloat("chainWeight8"));
				cws.setChainWeight9(rs.getFloat("chainWeight9"));
				cws.setChainWeight10(rs.getFloat("chainWeight10"));
				cws.setChainWeight11(rs.getFloat("chainWeight11"));*/
				System.out.println(rs.getFloat(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
