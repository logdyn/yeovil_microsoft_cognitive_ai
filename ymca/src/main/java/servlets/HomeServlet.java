package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logdyn.api.endpoints.LoggingEndpoint;
import com.logdyn.api.model.LogMessage;

import models.modules.Module;
import models.modules.ModuleUtils;

/**
 * Servlet implementation class HomeServlet
 */
public class HomeServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet()
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
	{
		final String modsParam = request.getParameter("mods");
		final Collection<Module> modules;
		if (null == modsParam)
		{
			modules = ModuleUtils.getDefaultModules();
		}
		else
		{
			final String[] modulesNames = modsParam.split(",");
			modules = new ArrayList<>(modulesNames.length);
			for (final String moduleName : modulesNames)
			{
				final Module module = Module.fromName(moduleName);
				if (null != module)
				{
					modules.add(module);
				}
				else
				{
					LoggingEndpoint.log(new LogMessage(request, Level.WARNING, "No module found with name: " + moduleName));
				}
			}
		}
		request.setAttribute("modules", modules);
		request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
	{
		super.doPost(request, response);
	}
}
