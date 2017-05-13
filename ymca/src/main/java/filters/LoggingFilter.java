package filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import endpoints.LoggingEndpoint;
import models.LogMessage;

/**
 * Servlet Filter implementation class LoggingFilter
 */
public class LoggingFilter implements Filter
{
	private static final Pattern ERROR_DESC = Pattern.compile("<b>description<\\/b>\\s*(?:<.*?>)*(.*?)(?:<\\/.*?>)*\\s*<\\/p>");
	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy()
	{
		// NOOP
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException
	{
		try
		{
			chain.doFilter(request, response);
		}
		catch (final Throwable e)
		{
			final String error;
			Matcher descMatcher = LoggingFilter.ERROR_DESC.matcher(e.getMessage());
			if(descMatcher.matches())
			{
				error = descMatcher.group(1);
			}
			else
			{
				error = e.getMessage();
			}
			if (request instanceof HttpServletRequest)
			{
				LoggingEndpoint.log(new LogMessage((HttpServletRequest) request, Level.SEVERE, error));
			}
			else
			{
				LoggingEndpoint.log(new LogMessage(Level.SEVERE, error));
			}
			throw e;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(final FilterConfig fConfig) throws ServletException
	{
		// NOOP
	}
}
