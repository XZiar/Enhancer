package xziar.enhancer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import xziar.enhancer.pojo.PostBean;
import xziar.enhancer.pojo.ReplyBean;
import xziar.enhancer.pojo.UserBean;
import xziar.enhancer.util.DataInject;

public class PostDao
{
	Connection conn = null;

	public PostDao(Connection c)
	{
		conn = c;
	}

	public PostBean queryPost(int pid) throws SQLException
	{
		final String sql_queryTask = "select * from PostData where pid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTask))
		{
			ps.setInt(1, pid);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())// no match
				return null;
			else
			{
				PostBean post = new PostBean();
				DataInject.RSToObj(rs, post);
				return post;
			}
		}
	}

	public ArrayList<PostBean> queryPosts(UserBean user) throws SQLException
	{
		final String sql_queryTasks = "select pid,type,uid,title,time_post,poster from PostData where uid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryTasks))
		{
			ps.setInt(1, user.getUid());
			ResultSet rs = ps.executeQuery();
			ArrayList<PostBean> posts = new ArrayList<>();
			while (rs.next())
			{
				PostBean post = new PostBean();
				DataInject.RSToObj(rs, post);
				posts.add(post);
			}
			return posts;
		}
	}

	public ArrayList<PostBean> queryPosts(int from, int size, String order) throws SQLException
	{
		final String sql_queryRangePosts = "select top " + size
				+ " pid,type,uid,title,time_post,poster,replycount from PostData where pid not in (select top "
				+ from + " pid from PostData order by time_post desc) order by time_post desc";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryRangePosts))
		{
			ResultSet rs = ps.executeQuery();
			ArrayList<PostBean> posts = new ArrayList<>();
			while (rs.next())
			{
				PostBean post = new PostBean();
				DataInject.RSToObj(rs, post);
				posts.add(post);
			}
			return posts;
		}
	}

	public ArrayList<ReplyBean> queryReplys(int from, int size, int pid, String order)
			throws SQLException
	{
		final String sql_queryRangeReplys = "select top " + size
				+ " * from ReplyData where pid=? AND rid not in (select top " + from
				+ " pid from ReplyData order by time_reply desc) order by time_reply";
		try (PreparedStatement ps = conn.prepareStatement(sql_queryRangeReplys))
		{
			ps.setInt(1, pid);
			ResultSet rs = ps.executeQuery();
			ArrayList<ReplyBean> replys = new ArrayList<>();
			while (rs.next())
			{
				ReplyBean reply = new ReplyBean();
				DataInject.RSToObj(rs, reply);
				replys.add(reply);
			}
			return replys;
		}
	}

	public PostBean addPost(PostBean post, UserBean user) throws SQLException
	{
		final String sql_insert = "insert into PostInfo (uid,title,time_post,describe,type) values(?,?,?,?,?)";
		try (PreparedStatement ps = conn.prepareStatement(sql_insert,
				Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1, user.getUid());
			ps.setString(2, post.getTitle());
			Long time = System.currentTimeMillis();
			ps.setLong(3, time);
			ps.setString(4, post.getDescribe());
			ps.setInt(5, post.getType());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			post.setPid(rs.getInt(1));
			post.setUid(user.getUid());
		}
		return post;
	}

	public ReplyBean addReply(ReplyBean reply, UserBean user) throws SQLException
	{
		final String sql_insert = "insert into PostReply (uid,pid,time_reply,describe) values(?,?,?,?)";
		try (PreparedStatement ps = conn.prepareStatement(sql_insert,
				Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1, user.getUid());
			ps.setInt(2, reply.getPid());
			Long time = System.currentTimeMillis();
			ps.setLong(3, time);
			ps.setString(4, reply.getDescribe());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			reply.setRid(rs.getInt(1));
			reply.setUid(user.getUid());
		}
		return reply;
	}
	
	public int deletePost(int pid) throws SQLException
	{
		final String sql_delReply = "delete from PostReply where pid=?";
		final String sql_delPost = "delete from PostInfo where pid=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql_delReply);
				PreparedStatement ps2 = conn.prepareStatement(sql_delPost);)
		{
			ps1.setInt(1, pid);
			ps2.setInt(1, pid);
			int i = ps1.executeUpdate(), j = ps2.executeUpdate();
			return i + j;
		}
	}
	
	public int deleteReply(int rid) throws SQLException
	{
		final String sql_delReply = "delete from PostReply where rid=?";
		try (PreparedStatement ps = conn.prepareStatement(sql_delReply))
		{
			ps.setInt(1, rid);
			return ps.executeUpdate();
		}
	}
}
