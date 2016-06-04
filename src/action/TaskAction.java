package action;

import java.util.ArrayList;
import java.util.HashMap;

import pojo.TaskBean;
import pojo.UserBean;
import service.TaskService;
import service.UserService;
import util.ServRes;

public class TaskAction extends ActionUtil
{
	int tid = -1;
	Integer uid = null;
	int from = 0;
	String des = "ÔÝÎÞ";
	TaskBean task;
	ArrayList<TaskBean> tasks;

	TaskService taskServ = new TaskService();
	UserService userServ = new UserService();
	
	public String View()
	{
		if(OnMethod("POST",null))
			return "error";
		ServRes<TaskBean> res = taskServ.GetTask(tid);
		switch(res.toEnum())
		{
		case nonexist:
			return "404";
		case success:
			task = res.getData();
			return "success";
		case error:
		default:
			return "error";
		}
	}
	
	public String List()
	{
		if(OnMethod("POST",null))
			return "error";
		ServRes<ArrayList<TaskBean>> res = taskServ.GetTasks(from);
		switch(res.toEnum())
		{
		case success:
			tasks = res.getData();
			return "success";
		case error:
		default:
			return "error";
		}
	}
	
	public void PostTask()
	{
		if(OnMethod("get",null))
		{
			Response(false,"error");
			return;
		}
		UserBean user = (UserBean)session.getAttribute("user");
		if(user == null)
		{
			Response(false,"unlogin");
			return;
		}
		ServRes<Integer> res = taskServ.PostTask(task, user);
		switch(res.toEnum())
		{
		case unsatisfy:
			Response(false,"nopermission");
			return;
		case success:
			Response(true,res.getData().toString());
			return;
		case error:
		default:
			Response(false,"error");
			return;
		}

	}
	
	public void Apply()
	{
		if(OnMethod("GET","task"))
			return;
		if(uid == null)
		{
			//get members
			UserBean user = (UserBean)session.getAttribute("user");
			if(user == null)
			{
				Response(false,"unlogin");return;
			}
			ServRes<ArrayList<UserBean>> res = userServ.GetMyApplicants(user.getUID());
			switch(res.toEnum())
			{
			case success:
				ArrayList<HashMap<String,Object>> applicants = new ArrayList<>();
				for(UserBean u : res.getData())
				{
					HashMap<String,Object> appl = new HashMap<>();
					appl.put("uid", u.getUID());
					appl.put("name", u.getName());
					appl.put("people", u.getPeople());
					applicants.add(appl);
				}
				datmap.put("applicants", applicants);
				Response(true,"");return;
			case error:
			default:
				Response(false,"error");return;
			}
		}
		else
		{
			//try apply
			//System.out.print(des);
			ServRes<Boolean> res = taskServ.Apply(uid, tid, des);
			switch(res.toEnum())
			{
			case exist:
				Response(false,"already");return;
			case unsatisfy:
				Response(false,"unsatify");return;
			case success:
				Response(true,"");return;
			case error:
			default:
				Response(false,"error");return;
			}
		}
	}
	
	public void GetApplyers()
	{
		if(OnMethod("GET","403.jsp"))
			return;
		UserBean user = (UserBean)session.getAttribute("user");
		if(user == null)
		{
			Response(false,"unlogin");
			return;
		}
		ServRes<ArrayList<UserBean>> res = taskServ.GetApplyersByTID(user, tid);
		switch(res.toEnum())
		{
		case success:
			ArrayList<HashMap<String,Object>> applicants = new ArrayList<>();
			for(UserBean u : res.getData())
			{
				HashMap<String,Object> appl = new HashMap<>();
				appl.put("uid", u.getUID());
				appl.put("name", u.getName());
				appl.put("people", u.getPeople());
				appl.put("des", u.getDescribe());
				applicants.add(appl);
			}
			datmap.put("applicants", applicants);
			Response(true,"");return;
		case error:
		default:
			Response(false,"error");
			return;
		}
	}
	
	public void AcceptApply()
	{
		if(OnMethod("GET","403.jsp"))
			return;
		UserBean user = (UserBean)session.getAttribute("user");
		if(user == null)
		{
			Response(false,"unlogin");
			return;
		}
		ServRes<Boolean> res = taskServ.AcceptApply(uid, tid, user);
		switch(res.toEnum())
		{
		case success:
			Response(true,"");return;
		case error:
		default:
			Response(false,"error");
			return;
		}
	}
	
	public void ComfirmApply()
	{
		if(OnMethod("GET","403.jsp"))
			return;
		UserBean user = (UserBean)session.getAttribute("user");
		if(user == null)
		{
			Response(false,"unlogin");
			return;
		}
		ServRes<Boolean> res = taskServ.ComfirmApply(tid, user);
		switch(res.toEnum())
		{
		case success:
			Response(true,"");return;
		case error:
		default:
			Response(false,"error");
			return;
		}
	}
	
	public TaskBean getTask()
	{
		return task;
	}
	public void setTask(TaskBean task)
	{
		this.task = task;
	}
	public void setTid(int tid)
	{
		this.tid = tid;
	}
	public void setUid(int uid)
	{
		this.uid = uid;
	}
	public void setFrom(int from)
	{
		this.from = from;
	}
	public void setDes(String des)
	{
		this.des = des;
	}
	
	public ArrayList<TaskBean> getTasks()
	{
		return tasks;
	}
}
