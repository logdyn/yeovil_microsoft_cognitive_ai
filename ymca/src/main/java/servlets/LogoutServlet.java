package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Class Constructor.
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public LogoutServlet()
	{
		super();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
	{
		request.getSession().invalidate();
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));
	}
}
