package xziar.enhancer.action;

import java.util.ArrayList;
import java.util.HashMap;

import xziar.enhancer.pojo.AccountBean.Role;
import xziar.enhancer.pojo.CompanyBean;
import xziar.enhancer.pojo.GroupBean;
import xziar.enhancer.pojo.StudentBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.service.UserService;
import xziar.enhancer.util.ServRes;

public class UserAction extends ActionUtil
{
	private String un = "";
	private String pwd = "";
	private String name = "";
	private UserBean user;
	private StudentBean stu;
	private CompanyBean cpn;

	UserService userServ = new UserService();

	protected boolean check()
	{
		user = (UserBean) session.getAttribute("user");
		if (user == null || user.getAccountRole() != Role.student)
		{
			Response(false, "unlogin");
			return false;
		}
		return true;
	}

	public void Login()
	{
		if (OnMethod("GET", "login.jsp"))
			return;
		ServRes<UserBean> res = userServ.Login(un, pwd);
		switch (res.toEnum())
		{
		case wrongun:
			Response(false, "�û�������");
			return;
		case wrongstatus:
			Response(false, "�����δͨ��");
			return;
		case wrongpwd:
			Response(false, "�������");
			return;
		case success:
			user = res.getData();
			session.setAttribute("user", user);
			Response(true, "��ӭ����" + user.getName());
			return;
		case error:
		default:
			Response(false, "ϵͳ����");
			return;
		}
	}

	public void JLogin()
	{
		if (OnMethod("GET", "../login.jsp"))
			return;
		ServRes<UserBean> res = userServ.Login(un, pwd);
		switch (res.toEnum())
		{
		case wrongun:
			Response(false, "�û�������");
			return;
		case wrongstatus:
			Response(false, "�����δͨ��");
			return;
		case wrongpwd:
			Response(false, "�������");
			return;
		case success:
			user = res.getData();
			session.setAttribute("user", user);
			datmap.put("user", user);
			Response(true, "��ӭ����" + user.getName());
			return;
		case error:
		default:
			Response(false, "ϵͳ����");
			return;
		}
	}

	public String Logout()
	{
		session.setAttribute("user", null);
		return "success";
	}

	public void Register()
	{
		if (OnMethod("GET", "register.jsp"))
			return;
		if (stu == null)
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
		switch (res.toEnum())
		{
		case exist:
			Response(false, "�û����Ѵ���");
			return;
		case success:
			Response(true, "���μ������û�����" + user.getUn());
			return;
		case error:
		default:
			Response(false, "ϵͳ����");
			return;
		}
	}

	public void SearchUser()
	{
		if (!check())
			return;
		ServRes<ArrayList<StudentBean>> res = userServ.SearchStudents(name);
		switch (res.toEnum())
		{
		case success:
			ArrayList<HashMap<String, Object>> stus = new ArrayList<>();
			for (StudentBean u : res.getData())
			{
				HashMap<String, Object> stu = new HashMap<>();
				stu.put("uid", u.getUid());
				stu.put("name", u.getName());
				stu.put("school", u.getSchool());
				stus.add(stu);
			}
			datmap.put("students", stus);
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "ϵͳ����");
			return;
		}
	}

	public void GetGroups()
	{
		if (!check())
			return;
		ServRes<ArrayList<GroupBean>> res = userServ.GetGroups((StudentBean) user);
		switch (res.toEnum())
		{
		case success:
			datmap.put("groups", res.getData());
			Response(true, "");
			return;
		case error:
		default:
			Response(false, "ϵͳ����");
			return;
		}
	}

	public String getUn()
	{
		return un;
	}

	public void setUn(String un)
	{
		this.un = un.replace(" ", "");
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
