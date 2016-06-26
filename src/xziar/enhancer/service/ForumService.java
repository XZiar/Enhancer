package xziar.enhancer.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import xziar.enhancer.dao.DaoBase;
import xziar.enhancer.dao.PostDao;
import xziar.enhancer.pojo.PostBean;
import xziar.enhancer.pojo.ReplyBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.util.ServRes;
import xziar.enhancer.util.ServRes.Result;

public class ForumService
{
	PostDao postdao;
	public ServRes<PostBean> GetPost(int pid)
	{
		if (pid < 0)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		postdao = new PostDao(conn);
		try
		{
			PostBean post = postdao.queryPost(pid);
			if (post == null)
				return new ServRes<>(Result.nonexist);
			else
			{
				return new ServRes<>(post);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<ArrayList<PostBean>> GetPosts(int from, int perpage)
	{
		if (from < 0)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		postdao = new PostDao(conn);
		try
		{
			ArrayList<PostBean> posts = postdao.queryPosts(from, perpage, "");
			return new ServRes<>(posts);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
	
	public ServRes<ArrayList<PostBean>> GetPosts(UserBean user)
	{
		Connection conn = DaoBase.getConnection(true);
		postdao = new PostDao(conn);
		try
		{
			ArrayList<PostBean> posts = postdao.queryPosts(user);
			return new ServRes<>(posts);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<ArrayList<ReplyBean>> GetReplys(int pid, int from)
	{
		if (from < 0)
			return new ServRes<>(Result.error);
		Connection conn = DaoBase.getConnection(true);
		postdao = new PostDao(conn);
		try
		{
			ArrayList<ReplyBean> replys = postdao.queryReplys(from, 10, pid, "");
			return new ServRes<>(replys);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<ArrayList<PostBean>> GetTasksByUser(UserBean user)
	{
		Connection conn = DaoBase.getConnection(true);
		postdao = new PostDao(conn);
		try
		{
			ArrayList<PostBean> posts = postdao.queryPosts(user);
			return new ServRes<>(posts);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}

	public ServRes<Integer> PostPost(PostBean post, UserBean user)
	{
		Connection conn = DaoBase.getConnection(false);
		postdao = new PostDao(conn);
		try
		{
			int pid = postdao.addPost(post, user).getPid();
			conn.commit();
			return new ServRes<>(pid);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
	
	public ServRes<Integer> PostReply(ReplyBean reply, UserBean user)
	{
		Connection conn = DaoBase.getConnection(false);
		postdao = new PostDao(conn);
		try
		{
			int rid = postdao.addReply(reply, user).getRid();
			conn.commit();
			return new ServRes<>(rid);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			return new ServRes<>(Result.error);
		}
		finally
		{
			DaoBase.close(conn, null, null);
		}
	}
}
