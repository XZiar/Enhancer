package pojo;

import java.util.HashMap;

public class GroupBean extends UserBean
{
	private HashMap<Integer,Integer> members = new HashMap<>();

	
	public HashMap<Integer, Integer> getMembers()
	{
		return members;
	}

	public void setMembers(HashMap<Integer, Integer> members)
	{
		this.members = members;
	}
}
