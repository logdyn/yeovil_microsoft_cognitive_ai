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

/**
 * Filter to allow logging in from any page.
 */
public class LoginFilter extends FilterBase
{

    /**
     * Default constructor.
     */
    public LoginFilter()
    {
        super();
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(final FilterConfig fConfig) throws ServletException
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException
	{
		ServletRequest newRequest = request;
		if (request instanceof HttpServletRequest && Boolean.parseBoolean(request.getParameter("auth")))
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
					System.err.println(e.getMessage() + " with username: " + httpRequest.getParameter("j_username"));
					if (response instanceof HttpServletResponse)
					{
						httpRequest.authenticate((HttpServletResponse) response);
					}
					return;
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
		// TODO Auto-generated method stub
	}

	@Override
	protected Log getLogger()
	{
		return null;
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
