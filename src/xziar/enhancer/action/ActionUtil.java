package xziar.enhancer.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;

public class ActionUtil
{
	protected Map<String,Object> datmap;
	protected HttpServletRequest request = ServletActionContext.getRequest();
	protected HttpServletResponse response = ServletActionContext.getResponse();
	protected HttpSession session = request.getSession();
	
	protected ActionUtil()
	{
		datmap = new HashMap<String,Object>();
	}
	
	protected boolean OnMethod(String method, String url)
	{
		boolean isM = method.equalsIgnoreCase(request.getMethod());
		if(isM && url != null)
		{
			try
			{
				response.sendRedirect(url);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return isM;
	}
	
	protected void Response()
	{
		String ret = JSON.toJSONString(datmap);
		response.setContentType("text/html;charset=utf-8");
		try
		{
			PrintWriter out = response.getWriter();
			out.write(ret);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	protected void Response(boolean suc, String msg)
	{
		datmap.put("success", suc);
		if(msg != null)
			datmap.put("msg", msg);
		Response();
	}
}
