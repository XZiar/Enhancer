package xziar.enhancer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import xziar.enhancer.pojo.AccountBean;
import xziar.enhancer.pojo.AccountBean.Status;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.GroupBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.util.DataInject;

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
			if (!rs.next())
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
			if (!rs.next())
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
			if (!rs.next())
				return null;
			DataInject.RSToObj(rs, user);
			return user;
		}
	}

	public ArrayList<GroupBean> queryGroups(StudentBean stu, boolean isLeader) throws SQLException
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
				GroupBean group = (GroupBean) queryUser(queryAccount(rs.getInt(1)));
				groups.add(group);
			}
		}
		return groups;
	}

	public ArrayList<UserBean> queryUsers(Status status) throws SQLException
	{
		final String sql_queryAccount = "select * from AccountInfo where status=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql_queryAccount))
		{
			ps1.setInt(1, status.ordinal());
			ResultSet rs1 = ps1.executeQuery();
			ArrayList<UserBean> users = new ArrayList<>();
			while (rs1.next())
			{
				AccountBean account = new AccountBean();
				DataInject.RSToObj(rs1, account);
				UserBean user = queryUser(account);
				users.add(user);
			}
			return users;
		}
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

	public int deleteUser(AccountBean account) throws SQLException
	{
		final String sql_delAccount = "delete from AccountInfo where uid=?";
		final String sql_delCompany = "delete from CompanyInfo where uid=?";
		final String sql_delStudent = "delete from StudentInfo where uid=?";
		final String sql_delUser = "delete from UserBasicInfo where uid=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql_delAccount);
				PreparedStatement ps2 = conn.prepareStatement(sql_delUser);
				PreparedStatement ps3 = conn.prepareStatement(sql_delCompany);
				PreparedStatement ps4 = conn.prepareStatement(sql_delStudent);)
		{
			ps1.setInt(1, account.getUid());
			ps2.setInt(1, account.getUid());
			ps3.setInt(1, account.getUid());
			ps4.setInt(1, account.getUid());

			int i = ps3.executeUpdate() + ps4.executeUpdate();
			int j = ps2.executeUpdate() + ps1.executeUpdate();
			if (i < 1 || j != 2)
			{
				System.out.println("delete res:" + i + "," + j);
				return -1;
			}
			return i + j;
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
		final String sql_addAccout = "insert into AccountInfo (un,pwd,role,status) values(?,?,?,"
				+ AccountBean.Status.unchecked.ordinal() + ")";
		try (PreparedStatement ps = conn.prepareStatement(sql_addAccout,
				Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, account.getUn());
			ps.setString(2, account.getPwd());
			ps.setInt(3, account.getRole());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (!rs.next())
				throw new SQLException("no result set");
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
				cpn.setPic_id(ImageDao.addImage(cpn.getImg_id()));
				ps3.setString(4, cpn.getPic_id());
				cpn.setPic_coltd(ImageDao.addImage(cpn.getImg_coltd()));
				ps3.setString(5, cpn.getPic_coltd());
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
