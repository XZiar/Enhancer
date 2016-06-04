package pojo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CompanyBean extends UserBean
{
	private String legal_name;
	private String legal_ID;
	private ImgBean img_ID;
	private ImgBean img_CoLtd;
	private String cel;
	private String tel;
	private String addr;
	
	
	public CompanyBean()
	{
		this.gender = true;
	}
	
	public String getLegal_name()
	{
		return legal_name;
	}
	public void setLegal_name(String legal_name)
	{
		this.legal_name = legal_name;
	}
	public String getLegal_ID()
	{
		return legal_ID;
	}
	public void setLegal_ID(String legal_ID)
	{
		this.legal_ID = legal_ID;
	}
	
	public ImgBean getImg_ID()
	{
		return img_ID;
	}
	public void setImg_ID(ImgBean img_ID)
	{
		this.img_ID = img_ID;
	}
	public InputStream getPic_ID()
	{
		return new ByteArrayInputStream(img_ID.getData());
	}
	public void setPic_ID(byte[] data)
	{
		img_ID = new ImgBean(data);
	}
	
	public ImgBean getImg_CoLtd()
	{
		return img_CoLtd;
	}
	public void setImg_CoLtd(ImgBean img_CoLtd)
	{
		this.img_CoLtd = img_CoLtd;
	}
	public InputStream getPic_CoLtd()
	{
		return new ByteArrayInputStream(img_CoLtd.getData());
	}
	public void setPic_CoLtd(byte[] data)
	{
		img_CoLtd = new ImgBean(data);
	}
	
	public String getCel()
	{
		return cel;
	}
	public void setCel(String cel)
	{
		this.cel = cel;
	}
	public String getTel()
	{
		return tel;
	}
	public void setTel(String tel)
	{
		this.tel = tel;
	}
	public String getAddr()
	{
		return addr;
	}
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
}
