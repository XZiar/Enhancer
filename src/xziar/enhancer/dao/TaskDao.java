package xziar.enhancer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import xziar.enhancer.pojo.CommentBean;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.TaskBean;
import xziar.enhancer.pojo.TaskBean.Status;
import xziar.enhancer.util.DataInject;

public class TaskDao
{
	Connection conn = null;

	public TaskDao(Connection c)
	{
		conn = c;
	}

	/**
	 * @param tid
	 * @return Task of given tid
	 * @throws SQLException
	 */
	public TaskBean queryTask(int tid) throws SQLException
	{
		final String sql_queryTask = "select * from TaskData where tid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTask))
		{
			ps.setInt(1, tid);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())// no match
				return null;
			else
			{
				TaskBean task = new TaskBean();
				DataInject.RSToObj(rs, task);
				return task;
			}
		}
	}

	/**
	 * @param cpn
	 * @return List of tasks with doer of given company(state only ongoing or
	 *         onfinish)
	 * @throws SQLException
	 */
	public ArrayList<TaskBean> queryFinTasks(CompanyBean cpn) throws SQLException
	{
		final String sql_queryTasks = "select * from ApplyData where uid=? and applystatus=1 and (status="
				+ Status.ongoing.ordinal() + " or status=" + Status.onfinish.ordinal() + ")";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTasks))
		{
			ps.setInt(1, cpn.getUid());
			ResultSet rs = ps.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<>();
			while (rs.next())
			{
				TaskBean task = new TaskBean();
				DataInject.RSToObj(rs, task);
				tasks.add(task);
			}
			return tasks;
		}
	}

	/**
	 * @param cpn
	 * @param status
	 * @return List of tasks of given company and given state
	 * @throws SQLException
	 */
	public ArrayList<TaskBean> queryTasks(CompanyBean cpn, Status status) throws SQLException
	{
		final String sql_queryTasks = "select * from TaskSimpleData where uid=? and status=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTasks))
		{
			ps.setInt(1, cpn.getUid());
			ps.setInt(2, status.ordinal());
			ResultSet rs = ps.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<>();
			while (rs.next())
			{
				TaskBean task = new TaskBean();
				DataInject.RSToObj(rs, task);
				tasks.add(task);
			}
			return tasks;
		}
	}

	/**
	 * @param stu
	 * @param accepted
	 * @param status
	 * @return List of tasks applyed of given student and given state
	 * @throws SQLException
	 */
	public ArrayList<TaskBean> queryTasks(StudentBean stu, boolean accepted, Status status)
			throws SQLException
	{
		final String sql_queryTasks = "select * from ApplyData where aid=? and applystatus=? and status=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTasks))
		{
			ps.setInt(1, stu.getUid());
			ps.setInt(2, accepted ? 1 : 0);
			ps.setInt(3, status.ordinal());
			ResultSet rs = ps.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<>();
			while (rs.next())
			{
				TaskBean task = new TaskBean();
				DataInject.RSToObj(rs, task);
				tasks.add(task);
			}
			return tasks;
		}
	}

	/**
	 * @param status
	 * @return List of tasks of given state
	 * @throws SQLException
	 */
	public ArrayList<TaskBean> queryTasks(Status status) throws SQLException
	{
		final String sql_queryTasks = "select * from TaskSimpleData where status=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTasks))
		{
			ps.setInt(1, status.ordinal());
			ResultSet rs = ps.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<>();
			while (rs.next())
			{
				TaskBean task = new TaskBean();
				DataInject.RSToObj(rs, task);
				tasks.add(task);
			}
			return tasks;
		}
	}

	/**
	 * @param from
	 * @param size
	 * @param order
	 * @return List of tasks of given range in given order
	 * @throws SQLException
	 */
	public ArrayList<TaskBean> queryTasks(int from, int size, String order) throws SQLException
	{
		final String sql_queryRangeTasks = "select top " + size
				+ " * from TaskSimpleData where tid not in (select top " + from
				+ " tid from TaskSimpleData order by time_start desc) order by time_start desc";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryRangeTasks))
		{
			ResultSet rs = ps.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();
			while (rs.next())
			{
				TaskBean task = new TaskBean();
				DataInject.RSToObj(rs, task);
				tasks.add(task);
			}
			return tasks;
		}
	}

	/**
	 * @param tid
	 * @param uid
	 * @return whether exist the applyment of given tid and uid
	 * @throws SQLException
	 */
	public boolean testHasApply(int tid, int uid) throws SQLException
	{
		final String sql_querHasApply = "select uid from TaskApply where tid=? and uid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_querHasApply))
		{
			ps.setInt(1, tid);
			ps.setInt(2, uid);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	/**
	 * @param task
	 * @param cpn
	 * @return task after insert successfully
	 * @throws SQLException
	 */
	public TaskBean addTask(TaskBean task, CompanyBean cpn) throws SQLException
	{
		final String sql_info = "insert into TaskInfo (uid,title,time_start,time_modify,status) values(?,?,?,0,"
				+ TaskBean.Status.oncheck.ordinal() + ")";
		final String sql_detail = "insert into TaskDetail (tid,describe,payment,limit_people,limit_score,time_last) values(?,?,?,?,?,?)";
		try (PreparedStatement ps1 = conn.prepareStatement(sql_info,
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement ps2 = conn.prepareStatement(sql_detail);)
		{
			ps1.setInt(1, cpn.getUid());
			ps1.setString(2, task.getTitle());
			Long time = System.currentTimeMillis();
			ps1.setLong(3, time);
			ps1.executeUpdate();

			ResultSet rs = ps1.getGeneratedKeys();
			rs.next();
			task.setTid(rs.getInt(1));

			ps2.setInt(1, task.getTid());
			ps2.setString(2, task.getDescribe());
			ps2.setInt(3, task.getPayment());
			ps2.setInt(4, task.getLimit_people());
			ps2.setInt(5, task.getLimit_score());
			ps2.setInt(6, 987654321);

			ps2.executeUpdate();
		}
		return task;
	}

	/**
	 * @param tid
	 * @return whether successfully deleted
	 * @throws SQLException
	 */
	public boolean deleteTask(int tid) throws SQLException
	{
		final String sql_delInfo = "delete from TaskInfo where tid=?";
		final String sql_delDetail = "delete from TaskDetail where tid=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql_delDetail);
				PreparedStatement ps2 = conn.prepareStatement(sql_delInfo))
		{
			ps1.setInt(1, tid);
			ps2.setInt(1, tid);

			int i = ps1.executeUpdate(), j = ps2.executeUpdate();
			if (i + j != 2)
			{
				System.out.println("delete res:" + i + "," + j);
				return false;
			}
			return true;
		}
	}

	/**
	 * @param task
	 * @return number of task be influnced
	 * @throws SQLException
	 */
	public int updateTask(TaskBean task) throws SQLException
	{
		final String sql_updAccount = "update TaskInfo set title=? , time_modify=? , status=? where tid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_updAccount))
		{
			task.setTime_modify((int) (System.currentTimeMillis() - task.getTime_start()));
			ps.setInt(4, task.getTid());
			ps.setString(1, task.getTitle());
			ps.setInt(2, task.getTime_modify());
			ps.setInt(3, task.getStatus());
			return ps.executeUpdate();
		}
	}

	/**
	 * @param tid
	 * @param uid
	 * @param des
	 * @return number of applicant be influnced
	 * @throws SQLException
	 */
	public int addApplicant(int tid, int uid, String des) throws SQLException
	{
		final String sql = "insert into TaskApply (uid,tid,describe) values(?,?,?)";
		try (PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setInt(1, uid);
			ps.setInt(2, tid);
			ps.setString(3, des);
			return ps.executeUpdate();
		}
	}

	/**
	 * @param tid
	 * @param uid
	 * @return whther succeed
	 * @throws SQLException
	 */
	public boolean acceptApply(int tid, int uid) throws SQLException
	{
		final String sql1 = "update TaskApply set status=1 where uid=? and tid=?";
		final String sql2 = "update TaskInfo set status=" + TaskBean.Status.onliscene.ordinal()
				+ " where tid=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2))
		{
			ps1.setInt(1, uid);
			ps1.setInt(2, tid);
			ps2.setInt(1, tid);
			int a1 = ps1.executeUpdate(), a2 = ps2.executeUpdate();
			if (a1 != a2 || a1 != 1)
			{
				System.err.println("ans is " + a1 + "," + a2);
				return false;
			}
			return true;
		}
	}

	/**
	 * @param tid
	 * @param suid
	 * @param cuid
	 * @return whether is able to comfirm
	 * @throws SQLException
	 */
	public boolean comfirmApply(int tid, int suid, int cuid) throws SQLException
	{
		final String sql_querHasApply = "select uid from TaskApply where uid=? and tid=? and status=1";
		final String sql2 = "update TaskInfo set status=" + TaskBean.Status.ongoing.ordinal()
				+ " where tid=?";
		final String sql3 = "insert into TaskResult(tid,suid,cuid) values(?,?,?)";
		try (PreparedStatement ps1 = conn.prepareStatement(sql_querHasApply);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3))
		{
			ps1.setInt(1, suid);
			ps1.setInt(2, tid);
			ps2.setInt(1, tid);
			ps3.setInt(1, tid);
			ps3.setInt(2, suid);
			ps3.setInt(3, cuid);
			ResultSet rs = ps1.executeQuery();
			if (!rs.next())
				return false;
			int a1 = ps2.executeUpdate(), a2 = ps3.executeUpdate();
			if (a1 != a2 || a1 != 1)
				return false;
			return true;
		}
	}

	/**
	 * @param tid
	 * @param uid
	 * @param comment
	 * @param score
	 * @param isS2C
	 *            whether is student towards company
	 * @return number of record be influenced
	 * @throws SQLException
	 */
	public int addComment(int tid, int uid, String comment, int score, boolean isS2C)
			throws SQLException
	{
		final String sql = isS2C
				? "update TaskResult set stoc=?,cscore=(case when cscore is null then ? else cscore end) where tid=? and suid=?"
				: "update TaskResult set ctos=?,sscore=(case when sscore is null then ? else sscore end) where tid=? and cuid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setString(1, comment);
			ps.setInt(2, score);
			ps.setInt(3, tid);
			ps.setInt(4, uid);
			return ps.executeUpdate();
		}
	}

	/**
	 * @param tid
	 * @return comment of given task
	 * @throws SQLException
	 */
	public CommentBean queryComment(int tid) throws SQLException
	{
		final String sql = "select * from TaskResult where tid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setInt(1, tid);
			CommentBean comment = new CommentBean();
			ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return null;
			DataInject.RSToObj(rs, comment);
			return comment;
		}
	}
}
