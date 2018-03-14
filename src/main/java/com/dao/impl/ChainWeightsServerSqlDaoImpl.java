package com.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import com.dao.IChainWeightsServerSqlDao;
import com.database.DBManager;
import com.entity.ChainWeights;


public class ChainWeightsServerSqlDaoImpl implements IChainWeightsServerSqlDao{
/*	private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String url = "jdbc:sqlserver://localhost:1433;DatabaseName=storemanager";
	private static final String user = "sa";
	private static final String pwd = "";*/
	/*Connection conn = null;*/
	@Override
	public int insert(ChainWeights record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(ChainWeights record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ChainWeights selectChainWeights() {
	/*	String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://localhost:1433;DatabaseName=storemanager";
		String user = "sa";
		String pwd = "";
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		ChainWeights cws=null;
		Connection conn = null;
		PreparedStatement pstm =null;
	    ResultSet rs = null;
		try {
		 conn = DBManager.getConn();
		String sql = "select * from chain_weights";
		 pstm = conn.prepareStatement(sql);	
		 rs = pstm.executeQuery();
		while(rs.next())
		{
			cws = new ChainWeights();
		/*	cws.setChainWeight1(rs.getFloat("chainWeight1"));
			cws.setChainWeight2(rs.getFloat("chainWeight2"));
			cws.setChainWeight3(rs.getFloat("chainWeight3"));
			cws.setChainWeight4(rs.getFloat("chainWeight4"));
			cws.setChainWeight5(rs.getFloat("chainWeight5"));
			cws.setChainWeight6(rs.getFloat("chainWeight6"));
			cws.setChainWeight7(rs.getFloat("chainWeight7"));
			cws.setChainWeight8(rs.getFloat("chainWeight8"));
			cws.setChainWeight9(rs.getFloat("chainWeight9"));
			cws.setChainWeight10(rs.getFloat("chainWeight10"));
			cws.setChainWeight11(rs.getFloat("chainWeight11"));*/
			cws.setChainWeight1(rs.getFloat(1));
			cws.setChainWeight2(rs.getFloat(2));
			cws.setChainWeight3(rs.getFloat(3));
			cws.setChainWeight4(rs.getFloat(4));
			cws.setChainWeight5(rs.getFloat(5));
			cws.setChainWeight6(rs.getFloat(6));
			cws.setChainWeight7(rs.getFloat(7));
			cws.setChainWeight8(rs.getFloat(8));
			cws.setChainWeight9(rs.getFloat(9));
			cws.setChainWeight10(rs.getFloat(10));
			cws.setChainWeight11(rs.getFloat(11));
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			DBManager.closeAll(conn, pstm, rs);
		}
		return cws;
	}
}
