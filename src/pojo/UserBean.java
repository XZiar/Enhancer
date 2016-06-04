package pojo;

public class UserBean
{
	protected int UID = -1;
	protected String un = "";
	protected String pwd = "";
	protected String name = "";
	protected Boolean gender = null;
	protected String describe = "";
	protected int people = 1;
	protected int score = 0;
	protected int task_finish = 0, task_progress = 0;
	
	public UserBean()
	{
		
	}
	public UserBean(String un,String pwd)
	{
		this.un = un;
		this.pwd = pwd;
	}
	public UserBean(int UID, int people)
	{
		this.UID = UID;
		this.people = people;
	}
	public UserBean(int UID, int people, String name, Boolean gender, String describe,
			int score, int task_finish, int task_progress)
	{
		this.UID = UID;
		this.people = people;
		setName(name);
		setGender(gender);
		setDescribe(describe);
		setScore(score);
		setTask_finish(task_finish);
		setTask_progress(task_progress);
	}
	public void copy(UserBean user)
	{
		this.UID = user.UID;
		this.people = user.people;
		setName(user.name);
		setGender(user.gender);
		setDescribe(user.describe);
		setScore(user.score);
		setTask_finish(user.task_finish);
		setTask_progress(user.task_progress);
	}
	
	public int getUID()
	{
		return UID;
	}

	public void setUID(int uID)
	{
		UID = uID;
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
	public int getPeople()
	{
		return people;
	}
	public void setPeople(int people)
	{
		this.people = people;
	}
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Boolean getGender()
	{
		return gender;
	}

	public void setGender(Boolean gender)
	{
		this.gender = gender;
	}

	public void setSgender(String gender)
	{
		if("true".equals(gender))
			this.gender = true;
		else if("false".equals(gender))
			this.gender = false;
	}
	
	public String getDescribe()
	{
		return describe;
	}

	public void setDescribe(String describe)
	{
		this.describe = describe;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getTask_finish()
	{
		return task_finish;
	}

	public void setTask_finish(int task_finish)
	{
		this.task_finish = task_finish;
	}

	public int getTask_progress()
	{
		return task_progress;
	}

	public void setTask_progress(int task_progress)
	{
		this.task_progress = task_progress;
	}
}

