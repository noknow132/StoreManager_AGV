package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import test.DBManager;
import test.StuInfo;

public class StuDaoImpl {

	Connection conn = null;
	PreparedStatement pstm = null;
	ResultSet rs = null;
	
	DBManager dbm = new DBManager();
	//增加
	public int addStuInfo(StuInfo stu) {
		int count =0;
		try {
			conn = dbm.getConn();
			String sql = "insert into stuInfo values(?)";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, stu.getName());
			
			
			count = pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			dbm.closeAll(conn, pstm, null);
		}
		return count;
	}
	
	//删除用户信息
	public int deleteStuInfo(int Id) {
		int count=0;
		try {
			conn = dbm.getConn();
			
			String sql = "delete from stuInfo where Id=?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, Id);
			count=pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			dbm.closeAll(conn, pstm, rs);
		}
		return count;
	}
	//查询所有用户信息
	public List<StuInfo> getAllStuInfo() {
		List<StuInfo> userList=new ArrayList<StuInfo>();
		StuInfo stu = null;
		try {
			conn = dbm.getConn();
			
			String sql = "select * from stuInfo";
			pstm = conn.prepareStatement(sql);	
			rs = pstm.executeQuery();
			
			while(rs.next())
			{
				stu = new StuInfo();
				stu.setId(rs.getInt("Id"));
				stu.setName(rs.getString("Name"));
				
				userList.add(stu);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			dbm.closeAll(conn, pstm, rs);
		}
		return userList;
	}
	//根据指定Id查询用户信息
	public StuInfo getStuById(int Id) {
		StuInfo user = null;
		try {
			conn = dbm.getConn();
			
			String sql = "select * from stuInfo where Id=?";
			pstm = conn.prepareStatement(sql);	
			pstm.setInt(1, Id);
			rs = pstm.executeQuery();
			if(rs.next())
			{
				user = new StuInfo();
				user.setId(rs.getInt("Id"));
				user.setName(rs.getString("Name"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			dbm.closeAll(conn, pstm, rs);
		}
		return user;
	}
	
	//修改用户信息
	public int updateStuInfo(StuInfo stu) {
		int count=0;
		try {
			conn = dbm.getConn();
			
			String sql = "update stuInfo set Name=? where Id=?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, stu.getName());
			pstm.setInt(2, stu.getId());
			count=pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			dbm.closeAll(conn, pstm, rs);
		}
		return count;
	}
	
}
