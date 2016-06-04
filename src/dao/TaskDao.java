package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pojo.TaskBean;
import pojo.UserBean;
import util.DataInject;

public class TaskDao
{
	Connection conn = null;
	
	public TaskDao(Connection c)
	{
		conn = c;
	}
	
	public TaskBean FindTaskByTID(int tid) throws SQLException
	{
		final String sql1 = "select * from TaskInfo ti,Task t where t.TID=ti.TID and t.TID=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1))
		{
			ps1.setInt(1, tid);
			ResultSet rs1 = ps1.executeQuery();
			if(!rs1.next())//no match
				return null;
			else
			{
				TaskBean task = new TaskBean(tid);
				DataInject.RSToObj(rs1, task);
				return task;
			}
		}
	}
	
	public ArrayList<TaskBean> FindTaskByUID(int uid) throws SQLException
	{
		final String sql1 = "select * from Task t where t.UID=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1))
		{
			ps1.setInt(1, uid);
			ResultSet rs1 = ps1.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<>();
			while(rs1.next())
			{
				TaskBean task = new TaskBean(uid);
				DataInject.RSToObj(rs1, task);
				tasks.add(task);
			}
			return tasks;
		}
	}
	
	public ArrayList<TaskBean> FindApplyTaskByUID(int uid) throws SQLException
	{
		final String sql1 = "select * from Task t where t.TID in (select TID from TaskApply where UID=?)";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1))
		{
			ps1.setInt(1, uid);
			ResultSet rs1 = ps1.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<>();
			while(rs1.next())
			{
				TaskBean task = new TaskBean(uid);
				DataInject.RSToObj(rs1, task);
				tasks.add(task);
			}
			return tasks;
		}
	}
	
	public ArrayList<TaskBean> GetTasks(int from, int size, String order) throws SQLException
	{
		final String sql1 = "select top " + size + " * from Task where TID not in (select top " + from + " TID from Task order by time_start desc) order by time_start";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1))
		{
			//System.out.println(sql1);
			ResultSet rs1 = ps1.executeQuery();
			ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();
			while(rs1.next())
			{
				TaskBean task = new TaskBean();
				DataInject.RSToObj(rs1, task);
				tasks.add(task);
			}
			return tasks;
		}
	}
	
	public ArrayList<Integer> GetApplicants(int tid) throws SQLException
	{
		final String sql1 = "select uid from TaskApply where tid=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1))
		{
			ArrayList<Integer> aps = new ArrayList<>();
			ps1.setInt(1, tid);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next())
			{
				aps.add(rs1.getInt(1));
			}
			return aps;
		}
	}
	
	private Integer IncTID() throws SQLException
	{
		final String sql1 = "select _val from CountLimit where _key='TID'";
		final String sql2 = "update CountLimit set _val=_val+1 where _key='TID'";
		int uid;
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);)
			{
				ResultSet rs1 = ps1.executeQuery();
				if(!rs1.next())//no match
					return null;
				else
				{
					uid = rs1.getInt(1);
					ps2.executeUpdate();
				}
			}
		return uid;
	}
	public Integer AddTask(TaskBean task, UserBean user) throws SQLException
	{
		final String sql1 = "insert into Task (TID,UID,title,launcher,time_start,time_modify) values(?,?,?,?,?,0)";
		final String sql2 = "insert into TaskInfo (TID,describe,payment,limit_people,limit_score,time_last) values(?,?,?,?,?,?)";
		Integer tid = IncTID();
		System.out.println("tid=" + tid);
		if(tid == null)
			return null;
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);)
		{
			ps1.setInt(1, tid);
			ps2.setInt(1, tid);
			ps1.setInt(2, user.getUID());
			ps1.setString(3, task.getTitle());
			ps1.setString(4, user.getName());
			Long time = System.currentTimeMillis();
			ps1.setLong(5, time);
			
			ps2.setString(2, task.getDescribe());
			ps2.setInt(3, task.getPayment());
			ps2.setInt(4, task.getLimit_people());
			ps2.setInt(5, task.getLimit_score());
			ps2.setInt(6, 987654321);
			
			ps1.executeUpdate();
			ps2.executeUpdate();
		}
		return tid;
	}
	
	public int AddApplicant(int tid, int uid, String des) throws SQLException
	{
		final String sql1 = "insert into TaskApply (UID,TID,describe) values(?,?,?)";
		final String sql2 = "update Task set applycount=applycount+1 where TID=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2))
		{
			ps1.setInt(1, uid);
			ps1.setInt(2, tid);
			ps2.setInt(1, tid);
			ps1.setString(3,des);
			ps2.executeUpdate();
			return ps1.executeUpdate();
		}
	}
	
	public void AcceptApply(int tid, int uid) throws SQLException
	{
		final String sql1 = "update TaskApply set status=1 where UID=? and TID=?";
		final String sql2 = "update Task set status=2 where TID=?";
		final String sql3 = "updata UserInfo set task_progress=task_progress+1 where UID=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			PreparedStatement ps3 = conn.prepareStatement(sql3);)
		{
			ps1.setInt(1, uid);
			ps1.setInt(2, tid);
			ps2.setInt(1, tid);
			ps3.setInt(1, uid);
			int a1 = ps1.executeUpdate(),
				a2 = ps2.executeUpdate(),
				a3 = ps3.executeUpdate();
			if(a1 != a2 || a2 != a3 || a3 != 1)
				throw new SQLException("ans not equal to 1");
			return;
		}
	}
	
	public void ComfirmApply(int tid, int suid, int cuid) throws SQLException
	{
		final String sql1 = "select UID from TaskApply where UID=? and TID=? and status=1";
		final String sql2 = "update Task set status=3 where TID=?";
		final String sql3 = "insert into TaskResult(TID,sUID,cUID) values(?,?,?)";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
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
			if(!rs.next())
				throw new SQLException("wrong status");
			int a1 = ps2.executeUpdate(),
				a2 = ps3.executeUpdate();
			if(a1 != a2 || a1 != 1)
				throw new SQLException("ans not equal ton 1");
			return;
		}
	}
}
