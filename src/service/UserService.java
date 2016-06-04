package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DaoBase;
import dao.UserDao;
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
			ArrayList<UserBean> users = userdao.GetAllApplicantByUID(uid);
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
			UserBean user = userdao.GetAccountByUserName(un);
			if(user == null)
				return new ServRes<>(Result.wrongun);
			else if(pwd.equals(user.getPwd()))
			{
				user = userdao.GetUserByUID(user.getUID());
				user.setUn(un);
				user.setPwd(pwd);
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
			if(userdao.GetAccountByUserName(user.getUn())!=null)
				return new ServRes<>(Result.exist);
			String ans = userdao.AddUser(user);
			if("error".equals(ans))
				return new ServRes<>(Result.error);
			
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
			UserBean user = userdao.GetUserByUID(uid);
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
	
	public ServRes<UserBean> ChangeInfo(UserBean user, String oldpwd, String newpwd, String des)
	{
		Connection conn = DaoBase.getConnection(false);
		userdao = new UserDao(conn);
		try
		{
			if(!"".equals(newpwd))
			{
				//change password
				UserBean acc = userdao.GetAccountByUID(user.getUID());
				if(acc == null)
					return new ServRes<>(Result.nonexist);
				if(!acc.getPwd().equals(oldpwd))
					return new ServRes<>(Result.wrongpwd);
				acc.setPwd(newpwd);
				if(userdao.SetAccountByUID(acc) != 1)
					return new ServRes<>(Result.error);
				user.setPwd(newpwd);
			}
			user.setDescribe(des);
			if(userdao.SetUserByUID(user) != 1)
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
