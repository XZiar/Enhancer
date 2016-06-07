package action;

import pojo.AccountBean.Role;
import pojo.CompanyBean;
import pojo.StudentBean;
import pojo.UserBean;
import service.UserService;
import util.ServRes;

public class UserAction extends ActionUtil
{
	private String un="";
	private String pwd="";
	private UserBean user;
	private StudentBean stu;
	private CompanyBean cpn;
	
	UserService userServ = new UserService();
	
	
	public void Login()
	{
		if(OnMethod("GET","login.jsp"))
			return;
		ServRes<UserBean> res = userServ.Login(un,pwd);
		switch(res.toEnum())
		{
		case wrongun:
			Response(false,"用户名错误");return;
		case wrongpwd:
			Response(false,"密码错误");return;
		case success:
			user = res.getData();
			session.setAttribute("user", user);
			Response(true,"欢迎您，" + user.getName());
			return;
		case error:
		default:
			Response(false,"系统错误");return;
		}
	}
	public String Logout()
	{
		session.setAttribute("user", null);
		return "success";
	}
	public void Register()
	{
		if(OnMethod("GET","register.jsp"))
			return;
		if(stu == null)
		{
			user = cpn;
			user.setAccountRole(Role.company);
		}
		else
		{
			user = stu;
			user.setAccountRole(Role.student);
		}
		ServRes<UserBean> res = userServ.Register(user);
		switch(res.toEnum())
		{
		case exist:
			Response(false,"用户名已存在");return;
		case success:
			Response(true,"请牢记您的用户名：" + user.getUn());return;
		case error:
		default:
			Response(false,"系统错误");return;
		}
	}
	
	
	
	public String getUn()
	{
		return un;
	}

	public void setUn(String un)
	{
		this.un = un;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
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
}
