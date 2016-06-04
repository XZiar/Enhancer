package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DaoBase;
import dao.TaskDao;
import dao.UserDao;
import pojo.CompanyBean;
import pojo.StudentBean;
import pojo.TaskBean;
import pojo.TaskBean.TaskStatus;
import pojo.UserBean;
import util.ServRes;
import util.ServRes.Result;

public class TaskService
{
	public ServRes<TaskBean> GetTask(int tid)
	{
		if(tid < 0)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.FindTaskByTID(tid);
			if(task == null)
				return new ServRes<>(Result.nonexist);
			else
			{
				return new ServRes<>(task);
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
	
	public ServRes<ArrayList<TaskBean>> GetTasks(int from)
	{
		if(from < 0)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.GetTasks(from, 10, "");
			return new ServRes<>(tasks);
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
	
	public ServRes<ArrayList<TaskBean>> GetTasksByCompany(UserBean user)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.FindTaskByUID(user.getUID());
			return new ServRes<>(tasks);
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
	
	public ServRes<ArrayList<TaskBean>> GetTasksByStudent(UserBean user)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.FindApplyTaskByUID(user.getUID());
			return new ServRes<>(tasks);
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
	
	public ServRes<ArrayList<UserBean>> GetApplyersByTID(UserBean user, int tid)
	{
		if(CompanyBean.class != user.getClass())
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		UserDao userdao = new UserDao(conn);
		try
		{
			TaskBean task = taskdao.FindTaskByTID(tid);
			if(task == null)
				return new ServRes<>(Result.nonexist);
			if(task.getUID() != user.getUID())
				return new ServRes<>(Result.unsatisfy);
			if(task.getStatus() != TaskStatus.oncheck && task.getStatus() != TaskStatus.onapply)
				return new ServRes<>(Result.error);
			//start get ***
			ArrayList<UserBean> users = userdao.GetApplicantsDetailByTID(tid);
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
	
	public ServRes<Integer> PostTask(TaskBean task, UserBean user)
	{
		if(user.getPeople() != 0)//not company
			return new ServRes<>(Result.unsatisfy);
		Connection conn = DaoBase.getConnection(false);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			int tid = taskdao.AddTask(task, user);
			conn.commit();
			return new ServRes<>(tid);
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
	
	public ServRes<Boolean> Apply(int uid, int tid, String des)
	{
		Boolean rolled = false;
		Connection conn = DaoBase.getConnection(true);
		UserDao userdao = new UserDao(conn);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<Integer> aps = taskdao.GetApplicants(tid);
			if(aps.indexOf(uid) != -1)
				return new ServRes<>(Result.exist);
			ServRes<TaskBean> res1 = GetTask(tid);
			UserBean user = userdao.GetUserByUID(uid);
			if(user == null || res1.toEnum() != Result.success)
				return new ServRes<>(Result.error);
			TaskBean task = res1.getData();
			
			if(task.getLimit_people() > user.getPeople())
				return new ServRes<>(Result.unsatisfy);
		
			//add applicant
			conn.setAutoCommit(false);
			rolled = true;
			if(taskdao.AddApplicant(tid, uid, des) != 1)
				return new ServRes<>(Result.error);
			conn.commit();
			return new ServRes<>(true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			if(true == rolled)
			{
				try
				{
				conn.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
	
	public ServRes<Boolean> AcceptApply(int uid, int tid, UserBean user)
	{
		boolean rolled = false;
		if(CompanyBean.class != user.getClass())
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.FindTaskByTID(tid);
			if(task == null)
				return new ServRes<>(Result.nonexist);
			if(task.getUID() != user.getUID())//not own this task
				return new ServRes<>(Result.unsatisfy);
			if(task.getStatus() != TaskStatus.oncheck && task.getStatus() != TaskStatus.onapply)
				return new ServRes<>(Result.wrongstatus);
			// start attempt
			conn.setAutoCommit(false);
			rolled = true;
			taskdao.AcceptApply(tid, uid);
			conn.commit();
			return new ServRes<>(true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			if(rolled)
			{
				try
				{
				conn.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
	
	public ServRes<Boolean> ComfirmApply(int tid, UserBean user)
	{
		boolean rolled = false;
		if(StudentBean.class != user.getClass())
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.FindTaskByTID(tid);
			if(task == null)
				return new ServRes<>(Result.nonexist);
			if(task.getUID() != user.getUID())//not own this task
				return new ServRes<>(Result.unsatisfy);
			if(task.getStatus() != TaskStatus.onliscene)
				return new ServRes<>(Result.wrongstatus);
			// start attempt
			conn.setAutoCommit(false);
			rolled = true;
			taskdao.ComfirmApply(tid, user.getUID(),task.getUID());
			conn.commit();
			return new ServRes<>(true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			if(rolled)
			{
				try
				{
				conn.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
}
