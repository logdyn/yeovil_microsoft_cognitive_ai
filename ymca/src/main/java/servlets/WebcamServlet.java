package servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import apiRequests.VisionServiceRequest;
import endpoints.Observer;

/**
 * Servlet implementation class WebcamServlet
 */
public class WebcamServlet extends HttpServlet implements ObservableServlet
{
	public static Map<String, WebcamServlet> servletInstances;

	private static final long serialVersionUID = 1L;

	private String uuid;
	private final Set<Observer> observers = new HashSet<>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WebcamServlet()
	{
		super();
		WebcamServlet.servletInstances = new HashMap<>();
	}

	@Override
	public void destroy()
	{
		WebcamServlet.servletInstances.remove(this.uuid);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
	        throws ServletException, IOException
	{
		super.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
	        throws ServletException, IOException
	{
		final String id = request.getParameter("uuid");
		final String data = request.getParameter("webcamImage");
		
		if (null != id)
		{
			this.uuid = id;
			WebcamServlet.servletInstances.put(this.uuid, this);
			System.out.println(this.uuid);
			response.setContentType("text/plain");
			response.setContentLength(0);
		}
		else if (null != data)
		{
			// Decode image, for some reason decode() doesn't seem to handle '+'
			String decodedData = java.net.URLDecoder.decode(data, "UTF-8");
			decodedData = decodedData.replace(' ', '+');
			final byte[] imagedata = DatatypeConverter
			        .parseBase64Binary(decodedData.substring(decodedData.indexOf(",") + 1));

			// send image to listeners
			this.notifyObservers(imagedata);

			// Vision Request
			final String imageResponse = new VisionServiceRequest(imagedata, VisionServiceRequest.toGet.DESCRIPTION).call();
			
			response.setContentLength(imageResponse.length());
			response.setContentType("application/json");
			response.getOutputStream().print(imageResponse);
		}
		else
		{
			final String errorText = "{error: One or more expected POST parameters missing}";
			response.setContentLength(errorText.length());
			response.setContentType("application/json");
			response.getWriter().print(errorText);
		}
	}
	
	@Override
	public void addObserver(final Observer observer)
	{
		this.observers.add(observer);
	}

	@Override
	public void deleteObserver(final Observer observer)
	{
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers(final Object object)
	{
		for (final Observer observer : this.observers)
		{
			observer.update(this, object);
		}
	}
}
