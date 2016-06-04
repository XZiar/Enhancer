package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pojo.CompanyBean;
import pojo.GroupBean;
import pojo.StudentBean;
import pojo.UserBean;
import util.DataInject;

public class UserDao
{
	Connection conn = null;
	
	public UserDao(Connection c)
	{
		conn = c;
	}
	
	public UserBean GetUserByUID(int uid) throws SQLException
	{
		final String sql1 = "select * from UserInfo where UID=?";
		final String sql2 = "select * from CompanyInfo where UID=?";
		final String sql3 = "select * from StudentInfo where UID=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			PreparedStatement ps3 = conn.prepareStatement(sql3);)
		{
			ps1.setInt(1, uid);
			ps2.setInt(1, uid);
			ps3.setInt(1, uid);
			ResultSet rs1 = ps1.executeQuery();
			ResultSet rs2 = null;
			if(!rs1.next())//no match
				return null;
			
			UserBean user;
			int pn = rs1.getInt("people");
			if(pn < 0)//admin
			{
				user = new UserBean();
			}
			else if(pn == 0)//company
			{
				user = new CompanyBean();
				rs2 = ps2.executeQuery();
			}
			else if(pn == 1)//student
			{
				user = new StudentBean();
				rs2 = ps3.executeQuery();
			}
			else//group
			{
				user = new GroupBean();
			}
			DataInject.RSToObj(rs1, user);
			
			if(rs2 != null && rs2.next())
			{
				DataInject.RSToObj(rs2, user);
			}
			return user;
		}
	}
	
	public UserBean GetAccountByUserName(String un) throws SQLException
	{
		final String sql = "select UID,pwd from Account where un=?";
		try(PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setString(1, un);
			ResultSet rs=ps.executeQuery();
			if(!rs.next())//no match
				return null;
			else
			{
				UserBean user = new UserBean(un,rs.getString(2));
				user.setUID(rs.getInt(1));
				return user;
			}
		}
	}
	
	public UserBean GetAccountByUID(int uid) throws SQLException
	{
		final String sql = "select un,pwd from Account where UID=?";
		try(PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setInt(1, uid);
			ResultSet rs=ps.executeQuery();
			if(!rs.next())//no match
				return null;
			else
			{
				UserBean user = new UserBean(rs.getString(1),rs.getString(2));
				user.setUID(uid);
				return user;
			}
		}
	}
	
	public int SetAccountByUID(UserBean user) throws SQLException
	{
		final String sql = "update Account set un=? , pwd=? where UID=?";
		try(PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setInt(3, user.getUID());
			ps.setString(1, user.getUn());
			ps.setString(2, user.getPwd());
			return ps.executeUpdate();
		}
	}
	
	public int SetUserByUID(UserBean user) throws SQLException
	{
		final String sql = "update UserInfo set describe=? where UID=?";
		try(PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setInt(2, user.getUID());
			ps.setString(1, user.getDescribe());
			return ps.executeUpdate();
		}
	}
	
	public ArrayList<UserBean> GetAllApplicantByUID(int uid) throws SQLException
	{
		final String sql1 = "select gUID from GroupTable where mUID=? and role=1";
		ArrayList<UserBean> users = new ArrayList<>();
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);)
		{
			ps1.setInt(1, uid);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next())
			{
				int guid = rs1.getInt(1);
				users.add(GetUserByUID(guid));
			}
		}
		users.add(GetUserByUID(uid));//self
		return users;
	}
	
	public ArrayList<UserBean> GetApplicantsDetailByTID(int tid) throws SQLException
	{
		final String sql1 = "select ui.UID,name,people,ta.describe from UserInfo ui,TaskApply ta where ui.UID=ta.UID and TID=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1))
		{
			ArrayList<UserBean> aps = new ArrayList<>();
			ps1.setInt(1, tid);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next())
			{
				UserBean user = new UserBean();
				DataInject.RSToObj(rs1, user);
				aps.add(user);
			}
			return aps;
		}
	}
	
	private Integer IncUID() throws SQLException
	{
		final String sql1 = "select _val from CountLimit where _key='UID'";
		final String sql2 = "update CountLimit set _val=_val+1 where _key='UID'";
		int uid;
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);)
			{
				ResultSet rs1 = ps1.executeQuery();
				if(!rs1.next())//no match
					return null;
				else
				{
					uid = rs1.getInt(1);
					ps2.executeUpdate();
				}
			}
		return uid;
	}
	public String AddUser(UserBean user) throws SQLException
	{
		final String sql1 = "insert into Account (UID,un,pwd) values(?,?,?)";
		final String sql2 = "insert into UserInfo (UID,name,gender,people,describe) values(?,?,?,?,?)";
		final String sql3 = "insert into StudentInfo (UID,name,ID,school,studentID,initTime) values(?,?,?,?,?,?)";
		final String sql4 = "insert into CompanyInfo (UID,name,legal_name,legal_ID,cel,tel,addr,pic_ID,pic_CoLtd) values(?,?,?,?,?,?,?,?,?)";
		Integer uid = IncUID();
		System.out.println("uid="+uid);
		if(uid == null)
			return "error";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			PreparedStatement ps3 = conn.prepareStatement(sql3);
			PreparedStatement ps4 = conn.prepareStatement(sql4);)
		{
			ps1.setInt(1, uid);
			ps2.setInt(1, uid);
			ps1.setString(2, user.getUn());
			ps1.setString(3, user.getPwd());
			ps2.setString(2, user.getName());
			ps2.setBoolean(3, user.getGender());
			ps2.setString(5, "这个人很懒，暂时不提供简介设置");
			if(user.getClass() == StudentBean.class)
			{
				ps2.setInt(4, 1);
				ps3.setInt(1, uid);
				ps3.setString(2, user.getName());
				StudentBean stu = (StudentBean) user;
				ps3.setString(3, stu.getID());
				ps3.setString(4, stu.getSchool());
				ps3.setString(5, stu.getStudentID());
				ps3.setInt(6, stu.getInitTime());
				
				ps2.executeUpdate();
				ps1.executeUpdate();
				ps3.executeUpdate();
			}
			else if(user.getClass() == CompanyBean.class)
			{
				ps2.setInt(4, 0);
				ps4.setInt(1, uid);
				ps4.setString(2, user.getName());
				CompanyBean cpn = (CompanyBean) user;
				ps4.setString(3, cpn.getLegal_name());
				ps4.setString(4, cpn.getLegal_ID());
				ps4.setString(5, cpn.getCel());
				ps4.setString(6, cpn.getTel());
				ps4.setString(7, cpn.getAddr());
				try(InputStream p_id = cpn.getPic_ID();
					InputStream	p_coltd = cpn.getPic_CoLtd();)
				{
					if(p_id != null)
						ps4.setBinaryStream(8, p_id);
					else
						ps4.setObject(8, null);
					if(p_coltd != null)
						ps4.setBinaryStream(9, p_coltd);
					else
						ps4.setObject(9, null);
					
					ps2.executeUpdate();
					ps1.executeUpdate();
					ps4.executeUpdate();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
				return "error";
			user.setUID(uid);
		}
		return "true";
	}
}
