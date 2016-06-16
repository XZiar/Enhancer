package xziar.enhancer.action;

import java.util.ArrayList;

import xziar.enhancer.pojo.PostBean;
import xziar.enhancer.pojo.ReplyBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.service.ForumService;
import xziar.enhancer.service.UserService;
import xziar.enhancer.util.ServRes;

public class ForumAction extends ActionUtil
{
	int pid = -1;
	Integer uid = null;
	int from = 0;
	String des = "ÔÝÎÞ";
	PostBean post;
	ReplyBean reply;
	ArrayList<PostBean> posts;
	ArrayList<ReplyBean> replys;

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
			break;
		case error:
		default:
			return "error";
		}
		ServRes<ArrayList<ReplyBean>> res2 = forumServ.GetReplys(post, from);
		switch (res.toEnum())
		{
		case nonexist:
			return "404";
		case success:
			replys = res2.getData();
			break;
		case error:
		default:
			return "error";
		}
		return "success";
	}

	public void JView()
	{
		if (OnMethod("GET", "../postview"))
			return;
		ServRes<PostBean> res = forumServ.GetPost(pid);
		switch (res.toEnum())
		{
		case success:
			post = res.getData();
			break;
		default:
			Response(false, "error");
			return;
		}
		ServRes<ArrayList<ReplyBean>> res2 = forumServ.GetReplys(post, from);
		switch (res.toEnum())
		{
		case success:
			replys = res2.getData();
			break;
		default:
			Response(false, "error");
			return;
		}
		datmap.put("post", post);
		Response(true, "");
		return;
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

	public void JList()
	{
		if (OnMethod("GET", "../forum"))
			return;
		ServRes<ArrayList<PostBean>> res = forumServ.GetTasks(from);
		switch (res.toEnum())
		{
		case success:
			posts = res.getData();
			datmap.put("posts", posts);
			Response(true, "");
		case error:
		default:
			Response(false, "error");
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

	public void PostReply()
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
		ServRes<Integer> res = forumServ.PostReply(reply, user);
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

	public ReplyBean getReply()
	{
		return reply;
	}

	public void setReply(ReplyBean reply)
	{
		this.reply = reply;
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

	public ArrayList<ReplyBean> getReplys()
	{
		return replys;
	}
}
