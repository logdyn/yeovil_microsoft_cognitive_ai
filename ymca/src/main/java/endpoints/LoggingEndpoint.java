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
		LoggingEndpoint.log(null, level, message);
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
			}
			else
			{
				System.err.println(String.format("No LoggingEndpoints regestered for session '%s'", sessionId));
				System.err.println(level.getName() + ": " + message);
			}
		}
	}
}