package xziar.enhancer.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.struts2.ServletActionContext;

import xziar.enhancer.pojo.ImgBean;

public class ImageDao
{
	private static final Path imgDir;
	public static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	static
	{
		imgDir = Paths.get(ServletActionContext.getServletContext()
				.getAttribute("javax.servlet.context.tempdir").toString(), "image");
		try
		{
			Files.createDirectory(imgDir);
		}
		catch (FileAlreadyExistsException e)
		{
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String bytesToHex(byte[] bytes)
	{
		char[] hexChars = new char[bytes.length * 2];
		for (int i = 0, j = 0; i < bytes.length;)
		{
			int v = bytes[i++] & 0xFF;
			hexChars[j++] = hexArray[v >>> 4];
			hexChars[j++] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String md5(byte[] data)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			return bytesToHex(md.digest(data));
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String addImage(ImgBean img)
	{
		byte[] data = img.getData();
		if (data == null)
		{
			System.err.println("No data in ImgBean!");
			return null;
		}
		String fname = md5(data);
		if (fname == null)
		{
			System.err.println("Fail to calculate MD5!");
			return null;
		}
		System.out.println("MD5 for " + img.toString() + " : " + fname);
		try
		{
			File f = Files.createFile(imgDir.resolve(fname + ".png")).toFile();
			//System.out.println("suppose: " + f.getName());
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(data);
			fos.close();
		}
		catch (FileAlreadyExistsException e)
		{
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return fname;
	}

	public static ImgBean readImage(String fname)
	{
		//System.out.println("read suppose: " + imgDir.resolve(fname + ".png"));
		File f = imgDir.resolve(fname + ".png").toFile();
		ImgBean img = new ImgBean();
		if(f.exists())
			img.setImg(f);
		img.setImgFileName(f.getName());
		return img;
	}

}
