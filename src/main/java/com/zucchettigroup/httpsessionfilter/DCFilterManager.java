package com.zucchettigroup.httpsessionfilter;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servlet/DCFilterManager")
public class DCFilterManager extends HttpServlet 
{
	private static final String INSTANCENAME = System.getProperty("jvm.instance.name", "noname");

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String value = request.getParameter("value");
		if(value != null && !value.isEmpty())
		{
			boolean valueBoolean = Boolean.parseBoolean(value);
			if(valueBoolean)
			{
				boolean enabled = DCFilter.enable();
				response.getWriter().println("InstanceName -> " + INSTANCENAME);
				response.getWriter().append("Enabled: ").append(String.valueOf(enabled));
			}
			else
			{
				boolean disabled = DCFilter.disable();
				response.getWriter().println("InstanceName -> " + INSTANCENAME);
				response.getWriter().append("Disabled: ").append(String.valueOf(disabled));
			}
		}
		else
		{
			response.getWriter().println("InstanceName -> " + INSTANCENAME);
			response.getWriter().println("Per abilitare il DCFilter -> /servlet/DCFilterManager?value=true");
			response.getWriter().println("Per disabilitare il DCFilter -> /servlet/DCFilterManager?value=false");
		}
	}
}
