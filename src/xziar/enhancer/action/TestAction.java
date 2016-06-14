package xziar.enhancer.action;

import org.apache.struts2.ServletActionContext;

public class TestAction extends ActionUtil
{
	public TestAction()
	{

	}

	public void dir()
	{
		datmap.put("catalina.base", System.getProperty("catalina.base"));
		datmap.put("getRealPath", ServletActionContext.getServletContext().getRealPath("/"));
		datmap.put("getContextPath", ServletActionContext.getServletContext().getContextPath());
		datmap.put("context.tempdir", ServletActionContext.getServletContext()
				.getAttribute("javax.servlet.context.tempdir"));
		String msg = System.getProperty("catalina.base") + "/work/Catalina/localhost"
				+ ServletActionContext.getServletContext().getContextPath() + "/image";
		Response(true, msg);
		return;
	}
}
