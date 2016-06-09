package xziar.enhancer.util;

public class ServRes<T>
{
	public enum Result
	{
		success,error,exist,nonexist,unsatisfy,wrongstatus,wrongun,wrongpwd,unimplement;
	}
	
	private T data = null;
	private String msg = null;
	private Result res;
	
	public ServRes(T data)
	{
		this.data = data;
		res = Result.success;
	}
	public ServRes(Result res)
	{
		this.res = res;
	}
	public ServRes(Result res, String msg)
	{
		this.res = res;
		this.msg = msg;
	}
	
	public Result toEnum()
	{
		return res;
	}
	public T getData()
	{
		return data;
	}
	public String getMsg()
	{
		return msg;
	}
}
