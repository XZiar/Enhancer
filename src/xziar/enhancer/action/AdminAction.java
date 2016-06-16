package xziar.enhancer.action;

import java.util.ArrayList;

import xziar.enhancer.pojo.AccountBean;
import xziar.enhancer.pojo.AccountBean.Role;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.service.AdminService;
import xziar.enhancer.util.ServRes;

public class AdminAction extends ActionUtil
{
	private int uid = -1;
	private int tid = -1;
	private int status = -1;
	private Boolean pass;
	private UserBean user;
	private ArrayList<UserBean> users;
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
		if(pass)
		{
			res = adminServ.ChangeStatus(uid,AccountBean.Status.pass);
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

	public void ChgUserState()
	{
		if (!check())
			return;
		AccountBean.Status newStatus = AccountBean.Status.values()[status];
		ServRes<AccountBean> res = adminServ.ChangeStatus(uid, newStatus);
		switch (res.toEnum())
		{
		case wrongun:
			Response(false, "ÓÃ»§Ãû´íÎó");
			return;
		case wrongpwd:
			Response(false, "ÃÜÂë´íÎó");
			return;
		case success:
			// user = res.getData();
			session.setAttribute("user", user);
			Response(true, "»¶Ó­Äú£¬" + user.getName());
			return;
		case error:
		default:
			Response(false, "ÏµÍ³´íÎó");
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
}
