package xziar.enhancer.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class DataInject
{
	static HashMap<String, HashMap<String, Method>> ObjectMap = new HashMap<>();

	public static void Load(Class<?> cls)
	{
		Pattern pm = Pattern.compile("set(\\w+)");
		HashMap<String, Method> MethodMap = new HashMap<>();
		Method[] ms = cls.getMethods();
		for (Method m : ms)
		{
			String mthName = m.getName();
			if (Pattern.matches("set(\\w+)", mthName))
			{
				MethodMap.put(
						pm.matcher(mthName).replaceAll("$1").toLowerCase(), m);
			}
		}
		String objName = cls.getName();
		System.out.println("DataInject Load Class:" + objName);
		ObjectMap.put(objName, MethodMap);
	}

	public static boolean RSToObj(ResultSet rs, Object obj) throws SQLException
	{
		Class<?> cls = obj.getClass();
		String cname = cls.getName();
		HashMap<String, Method> MethodMap = ObjectMap.get(cname);
		if (MethodMap == null)
		{
			Load(cls);
			MethodMap = ObjectMap.get(cname);
		}
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int a = 1; a <= rsmd.getColumnCount(); ++a)
		{
			String par = rsmd.getColumnName(a).toLowerCase();
			Method mth = MethodMap.get(par);
			if (mth == null)
				continue;
			Object val = rs.getObject(a);
			try
			{
				mth.invoke(obj, val);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
}
