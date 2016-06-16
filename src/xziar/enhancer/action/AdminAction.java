package xziar.enhancer.action;

import java.util.ArrayList;

import xziar.enhancer.pojo.AccountBean.Role;
import xziar.enhancer.pojo.AccountBean.Status;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.TaskBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.service.AdminService;
import xziar.enhancer.util.ServRes;

public class AdminAction extends ActionUtil
{
	private int uid = -1;
	private int tid = -1;
	private int pid = -1;
	private int rid = -1;
	private int status = -1;
	private String type = "";
	private Boolean pass;
	private UserBean user;
	private ArrayList<UserBean> users;
	private ArrayList<TaskBean> tasks;
	private StudentBean stu;
	private CompanyBean cpn;

	AdminService adminServ = new AdminService();

	private boolean check()
	{
		if (OnMethod("GET", "login.jsp"))
			return false;
		user = (UserBean) session.getAttribute("user");
		if (user == null || user.getAccountRole() != Role.admin)
		{
			Response(false, "unlogin");
			return false;
		}
		return true;
	}

	public void GetUserChecks()
	{
		if (!check())
			return;
		ServRes<ArrayList<UserBean>> res = adminServ.GetUserChecks();
		switch (res.toEnum())
		{
		case success:
			users = res.getData();
			datmap.put("uchecks", users);
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
		}
	}

	public void GetTaskChecks()
	{
		if (!check())
			return;
		ServRes<ArrayList<TaskBean>> res = adminServ.GetSomeTasks(TaskBean.Status.oncheck);
		switch (res.toEnum())
		{
		case success:
			tasks = res.getData();
			datmap.put("tchecks", tasks);
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}

	public void GetTaskMans()
	{
		if (!check())
			return;
		ServRes<ArrayList<TaskBean>> res = adminServ.GetSomeTasks(TaskBean.Status.ongoing);
		switch (res.toEnum())
		{
		case success:
			tasks = res.getData();
			datmap.put("tmans", tasks);
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}

	public void GetAllUsers()
	{
		if (!check())
			return;
		Role role = Role.valueOf(type);
		ServRes<ArrayList<UserBean>> res = adminServ.GetUsers(role);
		switch (res.toEnum())
		{
		case success:
			users = res.getData();
			datmap.put("users", users);
			Response(true, role == Role.company ? "��ҵ" : "ѧ��");
			return;
		case error:
		default:
			Response(false, "error");
		}
	}

	public void DoUserCheck()
	{
		if (!check())
			return;
		if (pass == null)
		{
			Response(false, "error");
			return;
		}
		ServRes<?> res;
		if (pass)
		{
			res = adminServ.ChangeStatus(uid, Status.pass);
		}
		else
		{
			res = adminServ.DeleteUser(uid);
		}
		switch (res.toEnum())
		{
		case success:
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
		}
	}

	public void DoTaskCheck()
	{
		if (!check())
			return;
		if (pass == null)
		{
			Response(false, "error");
			return;
		}
		ServRes<?> res;
		if (pass)
		{
			res = adminServ.ChangeStatus(tid, TaskBean.Status.onapply);
		}
		else
		{
			res = adminServ.DeleteTask(tid);
		}
		switch (res.toEnum())
		{
		case success:
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
		}
	}

	public void DoTaskMan()
	{
		if (!check())
			return;
		if (pass == null)
		{
			Response(false, "error");
			return;
		}
		if (!pass)//close task
		{
			DeleteTask();
			return;
		}
		ServRes<?> res = adminServ.ChangeStatus(tid, TaskBean.Status.onfinish);
		switch (res.toEnum())
		{
		case success:
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}

	public void DeleteUser()
	{
		if (!check())
			return;
		ServRes<Boolean> res = adminServ.BlockUser(uid);
		switch (res.toEnum())
		{
		case success:
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}

	public void DeleteTask()
	{
		if (!check())
			return;
		ServRes<TaskBean> res = adminServ.ChangeStatus(tid, TaskBean.Status.closed);
		switch (res.toEnum())
		{
		case success:
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}
	
	public void DeletePost()
	{
		if (!check())
			return;
		ServRes<Boolean> res = adminServ.DeletePost(pid);
		switch (res.toEnum())
		{
		case success:
			Response(true, "post");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}
	
	public void DeleteReply()
	{
		if (!check())
			return;
		ServRes<Boolean> res = adminServ.DeleteReply(rid);
		switch (res.toEnum())
		{
		case success:
			Response(true, "reply");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}

	public int getUid()
	{
		return uid;
	}

	public void setUid(int uid)
	{
		this.uid = uid;
	}

	public int getTid()
	{
		return tid;
	}

	public void setTid(int tid)
	{
		this.tid = tid;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public UserBean getUser()
	{
		return user;
	}

	public void setUser(UserBean user)
	{
		this.user = user;
	}

	public StudentBean getStu()
	{
		return stu;
	}

	public void setStu(StudentBean stu)
	{
		this.stu = stu;
	}

	public CompanyBean getCpn()
	{
		return cpn;
	}

	public void setCpn(CompanyBean cpn)
	{
		this.cpn = cpn;
	}

	public ArrayList<UserBean> getUsers()
	{
		return users;
	}

	public void setUsers(ArrayList<UserBean> users)
	{
		this.users = users;
	}

	public Boolean getPass()
	{
		return pass;
	}

	public void setPass(Boolean pass)
	{
		this.pass = pass;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public ArrayList<TaskBean> getTasks()
	{
		return tasks;
	}

	public void setTasks(ArrayList<TaskBean> tasks)
	{
		this.tasks = tasks;
	}

	public void setRid(int rid)
	{
		this.rid = rid;
	}
	
	public void setPid(int pid)
	{
		this.pid = pid;
	}
}
