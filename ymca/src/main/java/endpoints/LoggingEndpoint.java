package endpoints;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.json.JSONObject;

/**
 * Endpoint Class used to log messages and send them to the client
 * @author Jake Lewis
 *
 */
public class LoggingEndpoint extends Endpoint
{

	private static Map<String, Set<LoggingEndpoint>> endpoints = new HashMap<>();
	private static Map<String, Deque<JSONObject>> messages = new HashMap<>();
	private Session session;
	private String sessionId;

	@Override
	public void onOpen(final Session session, final EndpointConfig config)
	{
		this.session = session;
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(final String text)
			{
				LoggingEndpoint.this.sessionId = text;
				Set<LoggingEndpoint> set = LoggingEndpoint.endpoints.get(LoggingEndpoint.this.sessionId);
				if (null == set)
				{
					set = new HashSet<>();
					LoggingEndpoint.endpoints.put(LoggingEndpoint.this.sessionId, set);
				}
				set.add(LoggingEndpoint.this);
			}
		});
	}

	/**
	 * Event that is triggered when a session has closed.
	 *
	 * @param session
	 *            The session
	 * @param closeReason
	 *            Why the session was closed
	 */
	@Override
	public void onClose(final Session session, final CloseReason closeReason)
	{
		final Set<LoggingEndpoint> set = LoggingEndpoint.endpoints.get(this.sessionId);
		set.remove(this);
		if (set.isEmpty())
		{
			LoggingEndpoint.endpoints.remove(this.sessionId);
		}
	}

	/**
	 * Logs to all JavaScript Logging Endpoints for all sessions
	 * @param level The log level e.g. INFO or SEVERE
	 * @param message The message to display
	 */
	public static void log(final Level level, final String message)
	{
		LoggingEndpoint.log((String) null, level, message);
	}
	
	/**
	 * Logs to all JavaScript Logging Endpoints for a specific session, logs to all sessions if ID is <code>null</code>
	 * @param request The request to get the session to send the message to
	 * @param level The log level e.g. INFO or SEVERE
	 * @param message The message to display
	 */
	public static void log (final HttpServletRequest request, final Level level, final String message)
	{
		LoggingEndpoint.log(request.getRequestedSessionId(), level, message);
	}

	/**
	 * Logs to all JavaScript Logging Endpoints for a specific session
	 * @param sessionId The session to send the message to
	 * @param level The log level e.g. INFO or SEVERE
	 * @param message The message to display
	 */
	public static void log(final String sessionId, final Level level, final String message)
	{
		final JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("level", level.getName());
		jsonMessage.put("message", message);

		// If sessionID is not specified, notify all endpoints
		if (null == sessionId)
		{
			for (final Set<LoggingEndpoint> set : LoggingEndpoint.endpoints.values())
			{
				for (final LoggingEndpoint endpoint : set)
				{
					endpoint.session.getAsyncRemote().sendText(jsonMessage.toString());
				}
			}
			
			//Queue message for all sessions
			for (String tempSessionId : LoggingEndpoint.endpoints.keySet())
			{
				Deque<JSONObject> messageQueue = LoggingEndpoint.messages.get(tempSessionId);
				
				if (null == messageQueue)
				{
					messageQueue = new LinkedList<>();
				}

				messageQueue.add(jsonMessage);
				LoggingEndpoint.messages.put(tempSessionId, messageQueue);
			}
		}
		else
		{
			final Set<LoggingEndpoint> sessionEndpoints = LoggingEndpoint.endpoints.get(sessionId);
			if (null != sessionEndpoints)
			{
				for (final LoggingEndpoint endpoint : sessionEndpoints)
				{
					endpoint.session.getAsyncRemote().sendText(jsonMessage.toString());
				}
				
				//Add message to queue for the sessionId
				Deque<JSONObject> messageQueue = LoggingEndpoint.messages.get(sessionId);
				
				if (null == messageQueue)
				{
					messageQueue = new LinkedList<>();
				}

				messageQueue.add(jsonMessage);
				LoggingEndpoint.messages.put(sessionId, messageQueue);
			}
			else
			{
				System.err.println(String.format("No LoggingEndpoints regestered for session '%s'", sessionId));
				System.err.println(level.getName() + ": " + message);
			}
		}
	}
	
	/**
	 * Returns the queue of existing messages for a given session
	 * @param sessionId The session ID for the session messages to be retrieved
	 * @return The queue of messages
	 */
	public static Deque<JSONObject> getMessages(String sessionId)
	{
		return LoggingEndpoint.messages.get(sessionId);
	}

	public static void log(HttpServletRequest request, JSONObject message)
	{
		LoggingEndpoint.log(request, Level.parse((String) message.getString("level")), (String) message.get("message"));
	}
}