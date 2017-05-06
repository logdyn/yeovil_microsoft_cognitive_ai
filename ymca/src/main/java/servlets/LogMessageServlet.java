package servlets;

import java.io.IOException;
import java.util.Deque;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import endpoints.LoggingEndpoint;

/**
 * Servlet implementation class LogMessageServlet
 */
public class LogMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Deque<JSONObject> messageQueue = LoggingEndpoint.getMessages(request.getRequestedSessionId());
		
		if (null != messageQueue)
		{
			while (!messageQueue.isEmpty())
			{
				JSONObject message = messageQueue.pop();
				LoggingEndpoint.log(request, Level.parse((String) message.getString("level")), (String) message.get("message"), false);
			}
		}
	}

}
