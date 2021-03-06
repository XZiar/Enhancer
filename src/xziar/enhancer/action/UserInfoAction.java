package xziar.enhancer.action;

import java.util.ArrayList;

import xziar.enhancer.pojo.AccountBean;
import xziar.enhancer.pojo.AccountBean.Role;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.TaskBean;
import xziar.enhancer.pojo.TaskBean.Status;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.service.TaskService;
import xziar.enhancer.service.UserService;
import xziar.enhancer.util.ServRes;
import xziar.enhancer.util.ServRes.Result;

public class UserInfoAction extends ActionUtil
{
	UserBean user = null;
	ArrayList<TaskBean> tasks = null;
	int uid = -1;
	String oldpwd,newpwd,des;

	UserService userServ = new UserService();
	TaskService taskServ = new TaskService();
	
	protected boolean check()
	{
		if (OnMethod("GET", "login.jsp"))
			return false;
		user = (UserBean) session.getAttribute("user");
		if (user == null)
		{
			Response(false, "unlogin");
			return false;
		}
		return true;
	}

	public String Overall()
	{
		user = (UserBean)session.getAttribute("user");
		if(user == null)
			return "unlogin";
		if(user.getAccountRole() == AccountBean.Role.admin)
			return "admin";
		return "success";
	}
	
	public String ViewOther()
	{
		ServRes<UserBean> res = userServ.GetUserInfo(uid);
		switch(res.toEnum())
		{
		case success:
			user = res.getData();
			return "success";
		case error:
		default:
			return "error";
		}
	}

	public void ChangeBasic()
	{
		if (!check())
			return;
		ServRes<UserBean> res = userServ.ChangeInfo(user, oldpwd, newpwd, des);
		switch(res.toEnum())
		{
		case wrongpwd:
			Response(false, "�������");
			break;
		case success:
			user = res.getData();
			session.setAttribute("user", user);
			Response(true,"");
			break;
		case error:
		default:
			Response(false, "ϵͳ����");
			break;
		}
	}
	
	public void OngoingTasks()
	{
		if (!check())
			return;
		ServRes<ArrayList<TaskBean>> res = null;
		if (user.getAccountRole() == Role.company)
		{
			res = taskServ.GetTasks((CompanyBean) user, Status.onapply);
		}
		else if (user.getAccountRole() == Role.student)
		{
			res = taskServ.GetTasks((StudentBean) user, Status.onliscene);
		}
		else
			return;
		switch (res.toEnum())
		{
		case success:
			tasks = res.getData();
			datmap.put("ogtasks", tasks);
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}

	public void FinishTasks()
	{
		if (!check())
			return;
		ServRes<ArrayList<TaskBean>> res = null;
		if (user.getAccountRole() == Role.company)
		{
			res = taskServ.GetFinTasks((CompanyBean) user);
		}
		else if (user.getAccountRole() == Role.student)
		{
			res = taskServ.GetTasks((StudentBean) user, Status.onfinish);
		}
		else
			return;
		switch (res.toEnum())
		{
		case success:
			tasks = res.getData();
			datmap.put("fntasks", tasks);
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}
	}

	public String TaskOngoing()
	{
		user = (UserBean)session.getAttribute("user");
		if(user == null)
			return "unlogin";
		ServRes<ArrayList<TaskBean>> res = null;
		if(CompanyBean.class == user.getClass())
		{
			res = taskServ.GetTasks((CompanyBean) user, Status.onapply);
			if(res.toEnum() == Result.success)
			{
				tasks = res.getData();
				return "company";
			}
		}
		else if(StudentBean.class == user.getClass())
		{
			res = taskServ.GetTasks((StudentBean) user, Status.ongoing);
			if(res.toEnum() == Result.success)
			{
				tasks = res.getData();
				return "student";
			}
		}
		else
			return "error";
		switch(res.toEnum())
		{
		case error:
		default:
			return "error";
		}
	}
	
	
	public UserBean getUser()
	{
		return user;
	}
	public ArrayList<TaskBean> getTasks()
	{
		return tasks;
	}
	public void setUid(int uid)
	{
		this.uid = uid;
	}
	public void setUser(UserBean user)
	{
		this.user = user;
	}
	public void setOldpwd(String oldpwd)
	{
		this.oldpwd = oldpwd;
	}
	public void setNewpwd(String newpwd)
	{
		this.newpwd = newpwd;
	}
	public void setDes(String des)
	{
		this.des = des;
	}
}
