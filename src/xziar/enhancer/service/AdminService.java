package xziar.enhancer.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import xziar.enhancer.dao.DaoBase;
import xziar.enhancer.dao.PostDao;
import xziar.enhancer.dao.TaskDao;
import xziar.enhancer.dao.UserDao;
import xziar.enhancer.pojo.AccountBean;
import xziar.enhancer.pojo.TaskBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.pojo.AccountBean.Role;
import xziar.enhancer.util.ServRes;
import xziar.enhancer.util.ServRes.Result;

public class AdminService
{
	UserDao userdao = null;
	TaskDao taskdao = null;
	PostDao postdao = null;

	public ServRes<Boolean> BlockUser(int uid)
	{
		Connection conn = DaoBase.getConnection(false);
		userdao = new UserDao(conn);
		try
		{
			UserBean user = userdao.queryUser(userdao.queryAccount(uid));
			user.setAccountStatus(AccountBean.Status.banned);
			user.setUn(" "+System.currentTimeMillis());
			user.setName("(ÒÑÉ¾³ý)" + user.getName());
			int i = userdao.updateAccount(user), j = userdao.updateUser(user);
			System.out.println("block:" + i + "," + j);
			if (i + j == 2)
			{
				conn.commit();
				return new ServRes<>(true);
			}
			return new ServRes<>(Result.error);
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

	public ServRes<Boolean> DeleteUser(int uid)
	{
		Connection conn = DaoBase.getConnection(true);
		userdao = new UserDao(conn);
		try
		{
			int user = userdao.deleteUser(userdao.queryAccount(uid));
			return new ServRes<>((user != -1));
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

	public ServRes<ArrayList<UserBean>> GetUserChecks()
	{
		Connection conn = DaoBase.getConnection(true);
		userdao = new UserDao(conn);
		try
		{
			ArrayList<UserBean> users = userdao.queryUsers(AccountBean.Status.unchecked);
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

	public ServRes<ArrayList<UserBean>> GetUsers(Role role)
	{
		Connection conn = DaoBase.getConnection(true);
		userdao = new UserDao(conn);
		try
		{
			ArrayList<UserBean> users = userdao.queryUsers(role);
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

	public ServRes<TaskBean> ChangeStatus(int tid, TaskBean.Status newStatus)
	{
		Connection conn = DaoBase.getConnection(false);
		taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
				return new ServRes<>(Result.nonexist);
			task.setTaskStatus(newStatus);
			if (taskdao.updateTask(task) != -1)
				return new ServRes<>(Result.error);
			conn.commit();
			return new ServRes<>(task);
		}
		catch (Exception e)
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

	public ServRes<AccountBean> ChangeStatus(int uid, AccountBean.Status newStatus)
	{
		Connection conn = DaoBase.getConnection(false);
		userdao = new UserDao(conn);
		try
		{
			AccountBean account = userdao.queryAccount(uid);
			if (account == null)
				return new ServRes<>(Result.nonexist);
			account.setAccountStatus(newStatus);
			if (userdao.updateAccount(account) != 1)
				return new ServRes<>(Result.error);
			conn.commit();
			return new ServRes<>(account);
		}
		catch (Exception e)
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
