package servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import endpoints.LoggingEndpoint;
import models.user.User;
import storage.UserCache;

public class SignUpServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * Class Constructor.
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public SignUpServlet()
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
		request.getRequestDispatcher("/WEB-INF/pages/account.jsp?mode=edit").forward(request, response);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
	{
		final String newUserString = req.getParameter("newUser");
		final JSONObject jsonUser;
		if (newUserString != null)
		{
			jsonUser = new JSONObject(newUserString);
		}
		else
		{
			jsonUser = new JSONObject(req.getParameterMap());
		}
		final JSONObject response = new JSONObject();
		try
		{
			if (!UserCache.getInstance().isUsernameTaken(jsonUser.getString("username")))
			{
				final User newUser = new User.Builder(jsonUser.getString("forename"), jsonUser.getString("surname"))
				        .fromJSON(jsonUser).build();
				UserCache.getInstance().putUser(newUser);
				response.put("userid", newUser.getUuid());
				LoggingEndpoint.log(req, Level.FINE, "Successfully added user '"
				        + jsonUser.getString("username") + "' to the database.");
			}
			else
			{
				response.put("error", "The username '" + jsonUser.getString("username")
				        + "' is already taken, please try another one.");
				LoggingEndpoint.log(req, Level.WARNING, "The username '"
				        + jsonUser.getString("username") + "' is already taken, please try another one.");
			}

		}
		catch (NoSuchAlgorithmException | SQLException e)
		{
			e.printStackTrace();
			response.put("error", "An error happend, please contact your administrator: " + e.getMessage());
			LoggingEndpoint.log(req, Level.SEVERE, e.getMessage());
		}
		resp.getWriter().write(response.toString());
	}
}
