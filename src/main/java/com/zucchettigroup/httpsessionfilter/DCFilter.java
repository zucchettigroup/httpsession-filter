package com.zucchettigroup.httpsessionfilter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

@WebFilter(dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR}, 
		   urlPatterns = { "/*" })
public class DCFilter implements Filter 
{
	private static final String HTTP_HEADER_SETCOOKIE = "Set-Cookie";
	private static final String HTTP_HEADER_XDCAJAX = "X-DCAJAX";
	private static final String HTTP_HEADER_XREQUESTEDWITH = "X-Requested-With";
	private static final String PATTERN_DD_MM_YYYY_HH_MM_SS_SS = "dd/MM/yyyy HH:mm:ss,SS";
	private static final String INSTANCENAME = System.getProperty("jvm.instance.name", "noname");
	private static final AtomicBoolean ENABLED = new AtomicBoolean(false);
	private static final AtomicLong COUNTER = new AtomicLong();
	
	private BufferedWriter bwLogger;
	private Properties confProp;

	public static boolean enable()
	{
		return ENABLED.compareAndSet(false, true);
	}

	public static boolean disable()
	{
		return ENABLED.compareAndSet(true, false);
	}

	public void init(FilterConfig fConfig) throws ServletException 
	{
		this.confProp = loadProperties();
		
		String validInstanceNames = (String) this.confProp.get("validInstanceNames");
		ENABLED.compareAndSet(false, validInstanceNames.contains(INSTANCENAME));
		if(ENABLED.get())
		{
			try 
			{
				String logAppenderDir = (String) this.confProp.get("logAppenderDir");
				String versionLabel = (String) this.confProp.get("version");
				this.bwLogger = createLogger(fConfig, logAppenderDir, versionLabel);
				writeLogSTART();
				for(SessionStatus status : SessionStatus.values())
				{
					if(status != SessionStatus.VALID)
					{
						writeLogLegendSessionStatus(status);
					}
				}
				writeLogLegendID();
			} 
			catch (IOException e) 
			{
				throw new ServletException(e);
			}
		}
	}

	private Properties loadProperties() throws ServletException 
	{
		Properties props = new Properties();
		try(InputStream in = DCFilter.class.getResourceAsStream("httpsession-filter.properties");)
		{
			props.load(in);
		} 
		catch (IOException e1) 
		{
			throw new ServletException(e1);
		}
		return props;
	}

	private BufferedWriter createLogger(FilterConfig fConfig, String logAppenderDir, String versionLabel) throws IOException
	{
		String timestamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		Path logPath = Paths.get(logAppenderDir).resolve(INSTANCENAME + "_" + versionLabel + "_" + timestamp + ".log");
		return Files.newBufferedWriter(logPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

	public void destroy() 
	{
		try 
		{
			if(bwLogger != null)
			{
				writeLogSTOP();
				bwLogger.close();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		if(ENABLED.get())
		{
			if(request instanceof HttpServletRequest)
			{
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				HttpSession httpSession = httpRequest.getSession(false);
				Cookie jsessionIdOrNull = jsessionId(httpRequest);
				long filterCounter = COUNTER.incrementAndGet();
				SessionStatus sessionStatus = validate(jsessionIdOrNull, httpSession, bwLogger, filterCounter);
				if(sessionStatus != SessionStatus.VALID)
				{
					boolean isAjaxCall = httpRequest.getHeader(HTTP_HEADER_XREQUESTEDWITH) != null 
							|| httpRequest.getHeader(HTTP_HEADER_XDCAJAX) != null;

					writeLogURL(filterCounter, httpRequest.getRequestURL().toString(), httpRequest.getRemoteAddr());
					writeLogErrorCode(filterCounter, sessionStatus, isAjaxCall);

					if(response instanceof HttpServletResponse)
					{
						HttpServletResponse httpResponse = (HttpServletResponse) response;
						chain.doFilter(request, new FakeResponseWrapper(httpResponse));

						String setCookieValue = httpResponse.getHeader(HTTP_HEADER_SETCOOKIE);
						if(setCookieValue != null && !setCookieValue.isEmpty())
						{
							writeLogSetCookie(filterCounter, setCookieValue);
						}
						if(isAjaxCall)
						{
							byte[] jsonBytes = fromJsonToBytes(sessionStatus);
							httpResponse.setContentLength(jsonBytes.length);
							httpResponse.setContentType("application/json;charset=UTF-8");
							httpResponse.getOutputStream().write(jsonBytes);
							httpResponse.sendError(sessionStatus.httpcode, sessionStatus.errorMsg);
						}
						else
						{
							byte[] htmlBytes = fromHtmlToBytes(sessionStatus);
							httpResponse.setContentLength(htmlBytes.length);
							httpResponse.setContentType("text/html;charset=UTF-8");
							httpResponse.getOutputStream().write(htmlBytes);
						}
					}
					else
					{
						if(bwLogger != null) bwLogger.append("Invalid response class -> ").append(response.getClass().getName()).append("\n").flush();
					}
				}
				else
				{
					chain.doFilter(request, response);
				}
			}
			else
			{
				chain.doFilter(request, response);

				if(bwLogger != null) bwLogger.append("Invalid request class -> ").append(request.getClass().getName()).append("\n").flush();
			}
		}
		else
		{
			chain.doFilter(request, response);
		}
	}

	private byte[] fromHtmlToBytes(SessionStatus sessionStatus) 
	{
		byte[] htmlBytes = new StringBuilder("<!DOCTYPE html>")
				.append("<html>")
				.append("<head><meta charset=\"UTF-8\">")
				.append("<title>").append(sessionStatus.errorMsg).append("</title>")
				.append("</head>")
				.append("<body>")
				.append("<div style=\"text-align:center\">")
				.append(Utils.logoImageEncodedTag())
				.append("<h2>").append(sessionStatus.httpcode).append(" - ")
				.append(sessionStatus.errorMsg).append("</h2>")
				.append("</div>")
				.append("</body>")
				.append("</html>").toString().getBytes();
		return htmlBytes;
	}

	private byte[] fromJsonToBytes(SessionStatus sessionStatus) 
	{
		byte[] jsonBytes = 
				new JSONObject().put("ERROR", sessionStatus.errorMsg).toString().getBytes();
		return jsonBytes;
	}


	private static SessionStatus validate(Cookie requestCookie, HttpSession currentSession, BufferedWriter bw, long filterCounter)
	{
		String currentServerSessionID = null;
		if(currentSession != null)
		{
			currentServerSessionID = currentSession.getId();
			if(currentServerSessionID == null || currentServerSessionID.isEmpty())
			{
				return SessionStatus.SESSION_WITHOUT_ID;
			}
		}	
		String currentClientSessionID = null;
		if(requestCookie != null)
		{
			currentClientSessionID = requestCookie.getValue();
			if(currentClientSessionID == null || currentClientSessionID.isEmpty())
			{
				return SessionStatus.JSESSIONID_WITHOUT_ID;
			}
			// Casistica in cui il cookie client esiste
			// 1) Se l'istanza della JVM è diversa da quella del server corrente
			if(!currentClientSessionID.endsWith(INSTANCENAME))
			{
				writeLogValidationError(bw, filterCounter, currentClientSessionID, currentServerSessionID);
				return SessionStatus.INVALID_JVM_COOKIE;
			}
			// Se non è presente una sessione lato server valida
			if(currentServerSessionID == null)
			{
				writeLogValidationError(bw, filterCounter, currentClientSessionID, currentServerSessionID);
				return SessionStatus.VALID_COOKIE_INVALID_SESSION;
			}
		}
		return SessionStatus.VALID;
	}


	enum SessionStatus 
	{
		SESSION_WITHOUT_ID(586, "Sessione server senza ID"),
		JSESSIONID_WITHOUT_ID(587, "Cookie client senza ID"),
		INVALID_JVM_COOKIE(588, "Cookie client non valido per il server corrente"),
		VALID_COOKIE_INVALID_SESSION(589, "Sessione server non trovata per il cookie client specificato"),
		VALID(200, "tutto ok");

		public final int httpcode;
		public final String errorMsg;

		private SessionStatus(int httpcode, String errorMsg) 
		{
			this.httpcode = httpcode;
			this.errorMsg = errorMsg;
		}
	}

	private static Cookie jsessionId(HttpServletRequest request)
	{
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
		{
			for(Cookie c : cookies)
			{
				if(c.getName().equals("JSESSIONID"))
				{
					return c;
				}
			}
		}
		return null;
	}
	public class FakeResponseWrapper extends HttpServletResponseWrapper 
	{
		private final CharArrayWriter output;
		private final ByteArrayOutputStream baos;

		public FakeResponseWrapper(HttpServletResponse response)
		{
			super(response);
			this.baos = new ByteArrayOutputStream();
			this.output = new CharArrayWriter();
		}
		public PrintWriter getWriter()
		{
			return new PrintWriter(output);
		}
		@Override
		public ServletOutputStream getOutputStream() throws IOException 
		{
			return new ServletOutputStream() 
			{
				@Override
				public void write(int b) throws IOException 
				{
					baos.write(b);
				}

				@Override
				public boolean isReady() 
				{
					return true;
				}

				@Override
				public void setWriteListener(WriteListener listener) {}
			};
		}
		public String toString() 
		{
			return output.toString();
		}
	}
	
	private void writeLogLegendSessionStatus(SessionStatus status) throws IOException 
	{
		bwLogger.append("# [").append(String.valueOf(status.httpcode)).append("]='")
		.append(status.errorMsg).append("'").append("\n");
		bwLogger.flush();
	}
	
	private void writeLogLegendID() throws IOException 
	{
		bwLogger.append("# [").append("CID").append("]='")
		.append("Valore del Cookie JSESSIONID del client").append("'").append("\n");
		bwLogger.flush();
		bwLogger.append("# [").append("SID").append("]='")
		.append("ID della sessione sul server, recuperata con il metodo HttpServletRequest#getSession(false)").append("'").append("\n");
		bwLogger.flush();
	}
	
	private void writeLogSTART() throws IOException 
	{
		bwLogger.append("Zucchetti datacenter filter START -> ")
		.append(new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS_SS).format(new Date())).append("\n");
		bwLogger.flush();
	}

	private void writeLogSTOP() throws IOException 
	{
		bwLogger.append("Zucchetti datacenter filter STOP -> ")
		.append(new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS_SS).format(new Date())).append("\n");
	}
	
	private void writeLogSetCookie(long filterCounter, String setCookieValue) throws IOException 
	{
		if(bwLogger != null) bwLogger.append("- (" + filterCounter + ") ")
		.append(new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS_SS).format(new Date()))
		.append(" Set-Cookie -> '").append(setCookieValue).append("'")
		.append("\n").flush();
	}
	
	private void writeLogErrorCode(long filterCounter, SessionStatus sessionStatus, boolean isAjaxCall) throws IOException 
	{
		if(bwLogger != null) bwLogger.append("- (" + filterCounter + ") ")
		.append(new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS_SS).format(new Date()))
		.append(" ERROR->[").append(String.valueOf(sessionStatus.httpcode))
		.append("]").append(" AJAX->").append(String.valueOf(isAjaxCall))
		.append("\n").flush();
	}
	
	private void writeLogURL(long filterCounter, String urlComplete, String remoteIP) throws IOException 
	{
		if(bwLogger != null) bwLogger.append("- (" + filterCounter + ") ")
		.append(new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS_SS).format(new Date()))
		.append(" [" + remoteIP + "]")
		.append(" [" + urlComplete + "]")
		.append("\n").flush();
	}
	
	private static void writeLogValidationError(BufferedWriter bw, long filterCounter, String currentClientSessionID, String currentServerSessionID)
	{
		try 
		{
			if(bw != null) 
			{
				bw.append("- (").append(String.valueOf(filterCounter)).append(") ")
				.append(new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS_SS).format(new Date()))
				.append(" JVM->'").append(INSTANCENAME).append("' ")
				.append("CID->'").append(currentClientSessionID).append("' ")
				.append("SID->'").append(currentServerSessionID).append("'")
				.append("\n").flush();
			}
		} 
		catch (IOException e) 
		{
			// No ex
		}
	}
}
