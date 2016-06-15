package xziar.enhancer.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImgBean
{
	private byte[] data;
	private String imgContentType;
	private String imgFileName;

	public ImgBean()
	{
	}

	public ImgBean(byte[] data)
	{
		setData(data);
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public void setImg(File img)
	{
		data = new byte[(int) img.length()];
		try (InputStream ins = new FileInputStream(img))
		{
			ins.read(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setSimg(InputStream ins) throws IOException
	{
		data = new byte[ins.available()];
		ins.read(data);
	}

	public String getImgContentType()
	{
		return imgContentType;
	}

	public void setImgContentType(String imgContentType)
	{
		this.imgContentType = imgContentType;
	}

	public String getImgFileName()
	{
		return imgFileName;
	}

	public void setImgFileName(String imgFileName)
	{
		this.imgFileName = imgFileName;
	}

}
