package xziar.enhancer.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import xziar.enhancer.dao.DaoBase;
import xziar.enhancer.dao.TaskDao;
import xziar.enhancer.dao.UserDao;
import xziar.enhancer.pojo.AccountBean;
import xziar.enhancer.pojo.AccountBean.Role;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.TaskBean;
import xziar.enhancer.pojo.UserBean;
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

	public ServRes<ArrayList<TaskBean>> GetTasks(CompanyBean cpn, TaskBean.Status status)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.queryTasks(cpn, status);
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

	public ServRes<ArrayList<TaskBean>> GetFinTasks(CompanyBean cpn)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.queryFinTasks(cpn);
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

	public ServRes<ArrayList<TaskBean>> GetTasks(StudentBean stu, TaskBean.Status status)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			ArrayList<TaskBean> tasks = taskdao.queryTasks(stu, status);
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

	public ServRes<ArrayList<UserBean>> GetApplicants(CompanyBean cpn, int tid)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		UserDao userdao = new UserDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
				return new ServRes<>(Result.nonexist);
			if (task.getUid() != cpn.getUid())
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
			int tid = taskdao.addTask(task, (CompanyBean) user).getTid();
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
		boolean rolled = false;
		Connection conn = DaoBase.getConnection(true);
		UserDao userdao = new UserDao(conn);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			if (taskdao.testHasApply(tid, uid))
				return new ServRes<>(Result.exist);
			ServRes<TaskBean> res1 = GetTask(tid);
			UserBean user = userdao.queryUser(new AccountBean(uid, Role.student));
			if (user == null || res1.toEnum() != Result.success)
				return new ServRes<>(Result.error);
			TaskBean task = res1.getData();

			if (task.getTaskStatus() != TaskBean.Status.onapply)
				return new ServRes<>(Result.wrongstatus);

			if (task.getLimit_people() > user.getPeople())
				return new ServRes<>(Result.unsatisfy);

			// add applicant
			conn.setAutoCommit(false);
			rolled = true;
			if (taskdao.addApplicant(tid, uid, des) != 1)
			{
				rolled = false;
				conn.rollback();
			}
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

	public ServRes<Boolean> AcceptApply(int uid, int tid, UserBean user)
	{
		boolean rolled = false;
		if (user.getAccountRole() != Role.company)
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
			if (task.getTaskStatus() != TaskBean.Status.onapply)
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
		if (user.getAccountRole() != Role.student)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
				return new ServRes<>(Result.nonexist);
			if (!taskdao.testHasAccept(tid, user.getUid()))// not own this task
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

	public ServRes<TaskBean> FinishTask(int tid)
	{
		Connection conn = DaoBase.getConnection(false);
		TaskDao taskdao = new TaskDao(conn);
		try
		{
			TaskBean task = taskdao.queryTask(tid);
			if (task == null)
				return new ServRes<>(Result.nonexist);
			if (task.getTaskStatus() != TaskBean.Status.ongoing)
				return new ServRes<>(Result.wrongstatus);
			task.setTaskStatus(TaskBean.Status.onfinish);
			if (taskdao.updateTask(task) == -1)
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

	public ServRes<TaskBean> TaskComment(int tid, String comment, UserBean user)
	{
		Connection conn = DaoBase.getConnection(true);
		TaskDao taskdao = new TaskDao(conn);

		boolean isS2C = (user.getAccountRole() == Role.student);
		try
		{
			int res = taskdao.addComment(tid, user.getUid(), comment, isS2C);
			if (res != 1)
				return new ServRes<>(Result.error);
			else
				return new ServRes<>(Result.success);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
}
