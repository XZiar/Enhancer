package xziar.enhancer.action;

import java.util.ArrayList;

import xziar.enhancer.pojo.PostBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.service.ForumService;
import xziar.enhancer.service.UserService;
import xziar.enhancer.util.ServRes;

public class ForumAction extends ActionUtil
{
	int pid = -1;
	Integer uid = null;
	int from = 0;
	String des = "����";
	PostBean post;
	ArrayList<PostBean> posts;

	ForumService forumServ = new ForumService();
	UserService userServ = new UserService();

	public String View()
	{
		if (OnMethod("POST", null))
			return "error";
		ServRes<PostBean> res = forumServ.GetPost(pid);
		switch (res.toEnum())
		{
		case nonexist:
			return "404";
		case success:
			post = res.getData();
			return "success";
		case error:
		default:
			return "error";
		}
	}

	public String List()
	{
		if (OnMethod("POST", null))
			return "error";
		ServRes<ArrayList<PostBean>> res = forumServ.GetTasks(from);
		switch (res.toEnum())
		{
		case success:
			posts = res.getData();
			return "success";
		case error:
		default:
			return "error";
		}
	}

	public void PostPost()
	{
		if (OnMethod("get", null))
		{
			Response(false, "error");
			return;
		}
		UserBean user = (UserBean) session.getAttribute("user");
		if (user == null)
		{
			Response(false, "unlogin");
			return;
		}
		ServRes<Integer> res = forumServ.PostPost(post, user);
		switch (res.toEnum())
		{
		case unsatisfy:
			Response(false, "nopermission");
			return;
		case success:
			Response(true, res.getData().toString());
			return;
		case error:
		default:
			Response(false, "error");
			return;
		}

	}

	public PostBean getPost()
	{
		return post;
	}

	public void setPost(PostBean post)
	{
		this.post = post;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
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

	public ArrayList<PostBean> getPosts()
	{
		return posts;
	}
}
