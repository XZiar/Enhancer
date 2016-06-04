package pojo;

import java.util.regex.Pattern;

public class StudentBean extends UserBean
{
	private String ID;
	private String school;
	private String studentID;
	private int initTime;
	
	
	public String getID()
	{
		return ID;
	}
	public void setID(String iD)
	{
		ID = iD;
	}
	public String getSchool()
	{
		return school;
	}
	public void setSchool(String school)
	{
		this.school = school;
	}
	public String getStudentID()
	{
		return studentID;
	}
	public void setStudentID(String studentID)
	{
		this.studentID = studentID;
	}
	public int getInitTime()
	{
		return initTime;
	}
	public void setInitTime(int initTime)
	{
		this.initTime = initTime;
	}
	public void setSinitTime(String time)
	{
		if(time == null)
		{
			initTime = 0;
			System.out.println("null");
			return;
		}
		if(Pattern.matches("^[1,2][0,9][0-9]{2}-(([0][1-9])|([1][0-2]))$", time) == false)
		{
			System.out.println("false");
			initTime = 0;
			return;
		}
		Integer dy = Integer.parseInt(time.substring(0, 4));
		Integer dm = Integer.parseInt(time.substring(5, 7));
		initTime = dy * 100 + dm;
		System.out.println(initTime);
	}
}
