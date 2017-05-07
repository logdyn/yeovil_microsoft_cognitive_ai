package servlets;

import java.io.IOException;
import java.util.Deque;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
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
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final Deque<JSONObject> messageQueue = LoggingEndpoint.getMessages(request.getRequestedSessionId());
		
		if (null != messageQueue && !messageQueue.isEmpty())
		{
			final JSONObject messageObject = new JSONObject();
			final JSONArray messageArray = new JSONArray();
			
			messageObject.put("sessionId", request.getRequestedSessionId());
			
			while (!messageQueue.isEmpty())
			{
				messageArray.put(messageQueue.pop());		
			}
			
			messageObject.put("messageArray", messageArray);
			
			LoggingEndpoint.sendQueuedMessages(messageObject);
		}
	}

}
