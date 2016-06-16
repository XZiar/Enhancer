package xziar.enhancer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

	public ArrayList<TaskBean> queryTasks(CompanyBean cpn) throws SQLException
	{
		final String sql_queryTasks = "select * from TaskSimpleData where uid=?";
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

	public ArrayList<TaskBean> queryTasks(StudentBean stu) throws SQLException
	{
		final String sql_queryTasks = "select tid from TaskApply where uid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTasks))
		{
			ps.setInt(1, stu.getUid());
			ResultSet rs = ps.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<>();
			while (rs.next())
			{
				TaskBean task = queryTask(rs.getInt(1));
				tasks.add(task);
			}
			return tasks;
		}
	}

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
	
	public ArrayList<TaskBean> queryTasks(int from, int size, String order)
			throws SQLException
	{
		final String sql_queryRangeTasks = "select top " + size
				+ " * from TaskSimpleData where tid not in (select top " + from
				+ " tid from TaskSimpleData order by time_start desc) order by time_start";
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

	public ArrayList<Integer> GetApplicants(int tid) throws SQLException
	{
		final String sql1 = "select uid from TaskApply where tid=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql1))
		{
			ArrayList<Integer> aps = new ArrayList<>();
			ps1.setInt(1, tid);
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next())
			{
				aps.add(rs1.getInt(1));
			}
			return aps;
		}
	}

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

	public int deleteTask(int tid) throws SQLException
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
				return -1;
			}
			return i + j;
		}
	}
	
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

	public void acceptApply(int tid, int uid) throws SQLException
	{
		final String sql1 = "update TaskApply set status=1 where uid=? and tid=?";
		final String sql2 = "update TaskInfo set status="
				+ TaskBean.Status.onliscene.ordinal() + " where tid=?";
		final String sql3 = "update UserBasicInfo set task_progress=task_progress+1 where uid=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);)
		{
			ps1.setInt(1, uid);
			ps1.setInt(2, tid);
			ps2.setInt(1, tid);
			ps3.setInt(1, uid);
			int a1 = ps1.executeUpdate(), a2 = ps2.executeUpdate(),
					a3 = ps3.executeUpdate();
			if (a1 != a2 || a2 != a3 || a3 != 1)
				throw new SQLException("ans not equal to 1");
			return;
		}
	}

	public void comfirmApply(int tid, int suid, int cuid) throws SQLException
	{
		final String sql1 = "select uid from TaskApply where uid=? and tid=? and status=1";
		final String sql2 = "update TaskInfo set status="
				+ TaskBean.Status.ongoing.ordinal() + " where tid=?";
		final String sql3 = "insert into TaskResult(tid,suid,cuid) values(?,?,?)";
		try (PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);)
		{
			ps1.setInt(1, suid);
			ps1.setInt(2, tid);
			ps2.setInt(1, tid);
			ps3.setInt(1, tid);
			ps3.setInt(2, suid);
			ps3.setInt(3, cuid);
			ResultSet rs = ps1.executeQuery();
			if (!rs.next())
				throw new SQLException("wrong status");
			int a1 = ps2.executeUpdate(), a2 = ps3.executeUpdate();
			if (a1 != a2 || a1 != 1)
				throw new SQLException("ans not equal ton 1");
			return;
		}
	}

}
