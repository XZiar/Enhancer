package xziar.enhancer.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import xziar.enhancer.dao.ImageDao;
import xziar.enhancer.pojo.ImgBean;

public class ImgAction extends ActionUtil
{
	String obj;
	InputStream istream = null;

	public String Basic()
	{
		ImgBean img = ImageDao.readImage(obj);
		if(img.getData() == null)
			return "error";
		istream = new ByteArrayInputStream(img.getData());
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
