package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DaoBase;
import dao.UserDao;
import pojo.AccountBean;
import pojo.StudentBean;
import pojo.UserBean;
import util.ServRes;
import util.ServRes.Result;

public class UserService
{
	UserDao userdao = null;

	public ServRes<ArrayList<UserBean>> GetMyApplicants(int uid)
	{
		Connection conn = DaoBase.getConnection(true);
		userdao = new UserDao(conn);
		try
		{
			ArrayList<UserBean> users = new ArrayList<>();
			StudentBean student = (StudentBean) userdao
					.queryUser(new AccountBean(uid));
			users.add(student);
			users.addAll(userdao.queryGroups(student, true));
			return new ServRes<>(users);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<UserBean> Login(String un, String pwd)
	{
		Connection conn = DaoBase.getConnection(true);
		userdao = new UserDao(conn);
		try
		{
			AccountBean account = userdao.queryAccount(un);
			if (account == null)
				return new ServRes<>(Result.wrongun);
			else if (pwd.equals(account.getPwd()))
			{
				UserBean user = userdao.queryUser(account);
				return new ServRes<>(user);
			}
			else
			{
				return new ServRes<>(Result.wrongpwd);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<UserBean> Register(UserBean user)
	{
		Connection conn = DaoBase.getConnection(false);
		userdao = new UserDao(conn);
		try
		{
			System.out.println("register:" + user.getClass().getName());
			if (userdao.queryAccount(user.getUn()) != null)
				return new ServRes<>(Result.exist);
			userdao.addUser(user);
			conn.commit();
			return new ServRes<>(Result.success);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<UserBean> GetUserInfo(int uid)
	{
		Connection conn = DaoBase.getConnection(true);
		userdao = new UserDao(conn);
		try
		{
			UserBean user = userdao.queryUser(userdao.queryAccount(uid));
			return new ServRes<>(user);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<UserBean> ChangeInfo(UserBean user, String oldpwd,
			String newpwd, String des)
	{
		Connection conn = DaoBase.getConnection(false);
		userdao = new UserDao(conn);
		try
		{
			if (!"".equals(newpwd))
			{
				// change password
				AccountBean account = userdao.queryAccount(user.getUid());
				if (account == null)
					return new ServRes<>(Result.nonexist);
				if (!account.getPwd().equals(oldpwd))
					return new ServRes<>(Result.wrongpwd);
				account.setPwd(newpwd);
				if (userdao.updateAccount(account) != -1)
					return new ServRes<>(Result.error);
				user.setPwd(newpwd);
			}
			user.setDescribe(des);
			if (userdao.updateUser(user) != 1)
				return new ServRes<>(Result.error);

			conn.commit();
			return new ServRes<>(user);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
}
