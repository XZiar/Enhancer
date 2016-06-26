package xziar.enhancer.pojo;

import java.util.HashMap;

public class GroupBean extends UserBean
{
	public static enum Role
	{
		leader, member;
	}

	private HashMap<Integer, String> members = new HashMap<>();
	private int leaderID = -1;
	private String leaderName = "";

	public GroupBean()
	{
		this.role = AccountBean.Role.group;
	}

	public GroupBean(AccountBean account)
	{
		super(account);
	}

	public void addMember(int muid, String name, int role)
	{
		if (Role.leader.ordinal() == role)
		{
			setLeaderID(muid);
			setLeaderName(name);
		}
		else
		{
			members.put(muid, name);
		}
	}

	@Override
	public int getPeople()
	{
		return members.size() + 1;
	}

	public int getLeaderID()
	{
		return leaderID;
	}

	public void setLeaderID(int leaderID)
	{
		this.leaderID = leaderID;
	}

	public String getLeaderName()
	{
		return leaderName;
	}

	public void setLeaderName(String leaderName)
	{
		this.leaderName = leaderName;
	}

	public HashMap<Integer, String> getMembers()
	{
		return members;
	}

	public void setMembers(HashMap<Integer, String> members)
	{
		this.members = members;
	}

}
