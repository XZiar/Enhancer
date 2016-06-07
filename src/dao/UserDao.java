package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import pojo.AccountBean;
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

	public AccountBean queryAccount(int uid) throws SQLException
	{
		final String sql_queryAccount = "select * from AccountInfo where uid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryAccount))
		{
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return null;
			AccountBean account = new AccountBean();
			DataInject.RSToObj(rs, account);
			return account;
		}
	}

	public AccountBean queryAccount(String un) throws SQLException
	{
		final String sql_queryAccount = "select * from AccountInfo where un=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryAccount))
		{
			ps.setString(1, un);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return null;
			AccountBean account = new AccountBean();
			DataInject.RSToObj(rs, account);
			return account;
		}
	}

	public UserBean queryUser(AccountBean account) throws SQLException
	{
		UserBean user = null;
		String sql_query = null;
		switch (account.getAccountRole())
		{
		case admin:
			user = new UserBean(account);
			user.setName(user.getUn());
			return user;
		case student:
			sql_query = "select * from StudentData where uid=?";
			user = new StudentBean(account);
			break;
		case company:
			sql_query = "select * from CompanyData where uid=?";
			user = new CompanyBean(account);
			break;
		case group:
			sql_query = "select * from UserBasicInfo where uid=?";
			final String sql_queryGroup = "select muid,role from GroupInfo where guid=?";
			user = new GroupBean(account);
			try (PreparedStatement ps = conn.prepareStatement(sql_queryGroup))
			{
				ps.setInt(1, user.getUid());
				ResultSet rs = ps.executeQuery();
				((GroupBean) (user)).getMembers().clear();
				while (rs.next())
				{
					((GroupBean) (user)).addMember(rs.getInt(0), rs.getInt(1));
				}
			}
			break;
		}
		try (PreparedStatement ps = conn.prepareStatement(sql_query))
		{
			ps.setInt(1, user.getUid());
			ResultSet rs = ps.executeQuery();
			DataInject.RSToObj(rs, user);
			return user;
		}
	}

	public ArrayList<GroupBean> queryGroups(StudentBean stu, boolean isLeader)
			throws SQLException
	{
		String sql_queryGroups;
		if (isLeader)
			sql_queryGroups = "select guid from GroupInfo where muid=? and role="
					+ GroupBean.Role.leader.ordinal();
		else
			sql_queryGroups = "select guid from GroupInfo where muid=?";
		ArrayList<GroupBean> groups = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(sql_queryGroups);)
		{
			ps.setInt(1, stu.getUid());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				GroupBean group = (GroupBean) queryUser(
						queryAccount(rs.getInt(1)));
				groups.add(group);
			}
		}
		return groups;
	}

	public ArrayList<UserBean> queryApplicants(int tid) throws SQLException
	{
		final String sql_queryApplicants = "select uid from TaskApply where tid=?";
		ArrayList<UserBean> aps = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(sql_queryApplicants))
		{
			ps.setInt(1, tid);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				AccountBean account = queryAccount(rs.getInt(1));
				UserBean user = (UserBean) queryUser(account);
				aps.add(user);
			}
		}
		return aps;
	}

	public int updateAccount(AccountBean account) throws SQLException
	{
		final String sql_updAccount = "update AccountInfo set un=? , pwd=? , role=? , status=? where uid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_updAccount))
		{
			ps.setInt(5, account.getUid());
			ps.setString(1, account.getUn());
			ps.setString(2, account.getPwd());
			ps.setInt(3, account.getRole());
			ps.setInt(4, account.getStatus());
			return ps.executeUpdate();
		}
	}

	public int updateUser(UserBean user) throws SQLException
	{
		final String sql_updUser = "update UserBasicInfo set name=? , describe=? where uid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_updUser))
		{
			ps.setInt(3, user.getUid());
			ps.setString(1, user.getName());
			ps.setString(2, user.getDescribe());
			return ps.executeUpdate();
		}
	}

	public AccountBean addAccount(AccountBean account) throws SQLException
	{
		final String sql_addAccout = "insert into Account (un,pwd,role) values(?,?,?)";
		try (PreparedStatement ps = conn.prepareStatement(sql_addAccout,
				Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, account.getUn());
			ps.setString(2, account.getPwd());
			ps.setInt(3, account.getRole());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			account.setUid(rs.getInt(1));
			return account;
		}
	}

	public UserBean addUser(UserBean user) throws SQLException
	{
		final String sql_addUser = "insert into UserBasicInfo (uid,name,describe) values(?,?,?)";
		final String sql_addStudent = "insert into StudentInfo (uid,gender,id_person,school,id_student,time_enter) values(?,?,?,?,?,?)";
		final String sql_addCompany = "insert into CompanyInfo (uid,name_legal,id_legal,pic_id,pic_coltd,cel,tel,addr) values(?,?,?,?,?,?,?,?)";
		final String sql_addGroup = "insert into GroupInfo (guid,muid,role) values(?,?,?)";
		addAccount(user);
		try (PreparedStatement ps1 = conn.prepareStatement(sql_addUser);
				PreparedStatement ps2 = conn.prepareStatement(sql_addStudent);
				PreparedStatement ps3 = conn.prepareStatement(sql_addCompany);
				PreparedStatement ps4 = conn.prepareStatement(sql_addGroup);)
		{
			ps1.setInt(1, user.getUid());
			ps1.setString(2, user.getName());
			ps1.setString(3, user.getDescribe());
			ps1.executeUpdate();
			switch (user.getAccountRole())
			{
			case student:
				StudentBean stu = (StudentBean) user;
				ps2.setInt(1, stu.getUid());
				ps2.setBoolean(2, stu.getGender());
				ps2.setString(3, stu.getId_person());
				ps2.setString(4, stu.getSchool());
				ps2.setString(5, stu.getId_student());
				ps2.setInt(6, stu.getTime_enter());
				ps2.executeUpdate();
				break;
			case company:
				CompanyBean cpn = (CompanyBean) user;
				ps3.setInt(1, cpn.getUid());
				ps3.setString(2, cpn.getName_legal());
				ps3.setString(3, cpn.getId_legal());
				try (InputStream p_id = cpn.getPic_id();
						InputStream p_coltd = cpn.getPic_coltd();)
				{
					if (p_id != null)
						ps3.setBinaryStream(4, p_id);
					else
						ps3.setObject(4, null);
					if (p_coltd != null)
						ps3.setBinaryStream(5, p_coltd);
					else
						ps3.setObject(5, null);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				ps3.setString(6, cpn.getCel());
				ps3.setString(7, cpn.getTel());
				ps3.setString(8, cpn.getAddr());
				ps3.executeUpdate();
				break;
			case group:
				GroupBean group = (GroupBean) user;
				ps4.setInt(1, group.getUid());
				ps4.setInt(2, group.getLeaderID());
				ps4.setInt(3, GroupBean.Role.leader.ordinal());
				ps4.executeUpdate();
				break;
			default:
				break;
			}
		}
		return user;
	}

}
