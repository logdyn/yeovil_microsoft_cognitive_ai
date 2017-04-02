package endpoints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

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
	private Session session;
	private String sessionId;

	@Override
	public void onOpen(Session session, EndpointConfig config)
	{
		this.session = session;
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(String text)
			{
				sessionId = text;
			}
		});

		if (null == LoggingEndpoint.endpoints.get(sessionId))
		{
			LoggingEndpoint.endpoints.put(sessionId, new HashSet<LoggingEndpoint>());
			LoggingEndpoint.endpoints.get(sessionId).add(this);
		}
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
		LoggingEndpoint.endpoints.remove(session.getId());
	}

	/**
	 * Logs to all JavaScript Logging Endpoints for all sessions
	 * @param level The log level e.g. INFO or SEVERE
	 * @param message The message to display
	 */
	public static void log(Level level, String message)
	{
		LoggingEndpoint.log(null, level, message);
	}

	/**
	 * Logs to all JavaScript Logging Endpoints for a specific session
	 * @param sessionId The session to send the message to
	 * @param level The log level e.g. INFO or SEVERE
	 * @param message The message to display
	 */
	public static void log(String sessionId, Level level, String message)
	{
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("level", level.getName());
		jsonMessage.put("message", message);

		// If sessionID is not specified, notify all endpoints
		if (null == sessionId)
		{
			for (Set<LoggingEndpoint> set : LoggingEndpoint.endpoints.values())
			{
				for (LoggingEndpoint endpoint : set)
				{
					endpoint.session.getAsyncRemote().sendText(jsonMessage.toString());
				}
			}
		}
		else
		{
			for (LoggingEndpoint endpoint : LoggingEndpoint.endpoints.get(sessionId))
			{
				endpoint.session.getAsyncRemote().sendText(jsonMessage.toString());
			}
		}
	}
}