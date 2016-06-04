package action;

import java.util.ArrayList;

import pojo.CompanyBean;
import pojo.StudentBean;
import pojo.TaskBean;
import pojo.UserBean;
import service.TaskService;
import service.UserService;
import util.ServRes;
import util.ServRes.Result;

public class UserInfoAction extends ActionUtil
{
	UserBean user = null;
	ArrayList<TaskBean> tasks = null;
	int uid = -1;
	String oldpwd,newpwd,des;

	UserService userServ = new UserService();
	TaskService taskServ = new TaskService();
	
	public String Overall()
	{
		user = (UserBean)session.getAttribute("user");
		if(user == null)
			return "unlogin";
		
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
		user = (UserBean)session.getAttribute("user");
		if(user == null)
			Response(false,"unlogin");
		ServRes<UserBean> res = userServ.ChangeInfo(user, oldpwd, newpwd, des);
		switch(res.toEnum())
		{
		case wrongpwd:
			Response(false,"√‹¬Î¥ÌŒÛ");return;
		case success:
			user = res.getData();
			session.setAttribute("user", user);
			Response(true,"");
		case error:
		default:
			Response(false,"œµÕ≥¥ÌŒÛ");return;
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
			res = taskServ.GetTasksByCompany(user);
			if(res.toEnum() == Result.success)
			{
				tasks = res.getData();
				return "company";
			}
		}
		else if(StudentBean.class == user.getClass())
		{
			res = taskServ.GetTasksByStudent(user);
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
