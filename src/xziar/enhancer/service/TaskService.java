package xziar.enhancer.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import xziar.enhancer.dao.DaoBase;
import xziar.enhancer.dao.TaskDao;
import xziar.enhancer.dao.UserDao;
import xziar.enhancer.pojo.AccountBean;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.TaskBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.pojo.AccountBean.Role;
import xziar.enhancer.util.ServRes;
import xziar.enhancer.util.ServRes.Result;

public class TaskService
{
	public ServRes<TaskBean> GetTask(int tid)
	{
		if (tid < 0)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
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
		if (from < 0)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.queryTasks(from, 10, "");
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

	public ServRes<ArrayList<TaskBean>> GetTasksByCompany(CompanyBean cpn)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.queryTasks(cpn);
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

	public ServRes<ArrayList<TaskBean>> GetTasksByStudent(StudentBean stu)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.queryTasks(stu);
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
		if (CompanyBean.class != user.getClass())
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		UserDao userdao = new UserDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
				return new ServRes<>(Result.nonexist);
			if (task.getUid() != user.getUid())
				return new ServRes<>(Result.unsatisfy);
			if (task.getTaskStatus() != TaskBean.Status.oncheck
					&& task.getTaskStatus() != TaskBean.Status.onapply)
				return new ServRes<>(Result.error);
			// start get ***
			ArrayList<UserBean> users = userdao.queryApplicants(tid);
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
		if (user.getAccountRole() != AccountBean.Role.company)// not company
			return new ServRes<>(Result.unsatisfy);
		Connection conn = DaoBase.getConnection(false);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			int tid = taskdao.addTask(task, (CompanyBean)user).getTid();
			conn.commit();
			return new ServRes<>(tid);
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

	public ServRes<Boolean> Apply(int uid, int tid, String des)
	{
		Boolean rolled = false;
		Connection conn = DaoBase.getConnection(true);
		UserDao userdao = new UserDao(conn);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<Integer> aps = taskdao.GetApplicants(tid);
			if (aps.indexOf(uid) != -1)
				return new ServRes<>(Result.exist);
			ServRes<TaskBean> res1 = GetTask(tid);
			UserBean user = userdao.queryUser(new AccountBean(uid, Role.student));
			if (user == null || res1.toEnum() != Result.success)
				return new ServRes<>(Result.error);
			TaskBean task = res1.getData();

			if (task.getLimit_people() > user.getPeople())
				return new ServRes<>(Result.unsatisfy);

			// add applicant
			conn.setAutoCommit(false);
			rolled = true;
			if (taskdao.addApplicant(tid, uid, des) != 1)
				return new ServRes<>(Result.error);
			conn.commit();
			return new ServRes<>(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (true == rolled)
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
		if (CompanyBean.class != user.getClass())
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
				return new ServRes<>(Result.nonexist);
			if (task.getUid() != user.getUid())// not own this task
				return new ServRes<>(Result.unsatisfy);
			if (task.getTaskStatus() != TaskBean.Status.oncheck
					&& task.getTaskStatus() != TaskBean.Status.onapply)
				return new ServRes<>(Result.wrongstatus);
			// start attempt
			conn.setAutoCommit(false);
			rolled = true;
			taskdao.acceptApply(tid, uid);
			conn.commit();
			return new ServRes<>(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (rolled)
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
		if (StudentBean.class != user.getClass())
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
				return new ServRes<>(Result.nonexist);
			if (task.getUid() != user.getUid())// not own this task
				return new ServRes<>(Result.unsatisfy);
			if (task.getTaskStatus() != TaskBean.Status.onliscene)
				return new ServRes<>(Result.wrongstatus);
			// start attempt
			conn.setAutoCommit(false);
			rolled = true;
			taskdao.comfirmApply(tid, user.getUid(), task.getUid());
			conn.commit();
			return new ServRes<>(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (rolled)
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
