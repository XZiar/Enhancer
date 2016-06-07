package action;

import java.io.InputStream;

import pojo.CompanyBean;


public class ImgAction extends ActionUtil
{
	String obj;
	InputStream istream = null;



	public String Basic()
	{
		CompanyBean cpn = (CompanyBean)session.getAttribute("user");
		if(obj == null || cpn == null)
		{
			return "error";
		}
		
		switch(obj)
		{
		case "id":
			istream = cpn.getPic_id();break;
		case "coltd":
			istream = cpn.getPic_coltd();break;
		}
		return "success";
	}
	
	
	
	public String getObj()
	{
		return obj;
	}

	public void setObj(String obj)
	{
		this.obj = obj;
	}
	
	public InputStream getIstream()
	{
		return istream;
	}

	public void setIstream(InputStream istream)
	{
		this.istream = istream;
	}
}
