package filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.filters.FilterBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * Filter to allow logging in from any page.
 */
public class LoginFilter extends FilterBase
{
	private static final Log LOGGER = LogFactory.getLog(LoginFilter.class);
	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(final FilterConfig fConfig) throws ServletException
	{
		// NOOP
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException
	{
		ServletRequest newRequest = request;
		if (Boolean.parseBoolean(request.getParameter("auth")) && request instanceof HttpServletRequest)
		{
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			if (httpRequest.getUserPrincipal() == null && "POST".equals(httpRequest.getMethod()))
			{
				try
				{
					httpRequest.login(httpRequest.getParameter("j_username"), httpRequest.getParameter("j_password"));
					newRequest = new ForceGetWrapper(httpRequest);
				}
				catch (ServletException e)
				{
					LoginFilter.LOGGER.info(e.getMessage() + " with username: " + httpRequest.getParameter("j_username"), e);
					if (response instanceof HttpServletResponse)
					{
						HttpServletResponse httpResponse = (HttpServletResponse) response;
						httpResponse.sendRedirect(httpResponse.encodeRedirectURL("/Account"));
						return;
					}
					
				}
			}
		}
		chain.doFilter(newRequest, response);
	}


	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy()
	{
		// NOOP
	}

	@Override
	protected Log getLogger()
	{
		return LoginFilter.LOGGER;
	}
	
	/**
	 * Changes the method of the request to be GET.
	 * 
	 * @author Matt Rayner
	 */
	private class ForceGetWrapper extends HttpServletRequestWrapper
	{
		public ForceGetWrapper(final HttpServletRequest request)
		{
			super(request);
		}

		@Override
		public String getMethod()
		{
			return "GET";
		}
	}
}
