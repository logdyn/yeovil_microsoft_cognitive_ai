package endpoints;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.json.JSONArray;

import models.LogMessage;

/**
 * Endpoint Class used to log messages and send them to the client
 * 
 * @author Jake Lewis
 */
public class LoggingEndpoint extends Endpoint
{

	private static Map<String, Set<LoggingEndpoint>> endpoints = new HashMap<>();
	private static Map<String, SortedSet<LogMessage>> messages = new HashMap<>();
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

				final SortedSet<LogMessage> messageQueue = LoggingEndpoint.getMessages(text);
				if (null != messageQueue && !messageQueue.isEmpty())
				{
					final JSONArray messageArray = new JSONArray();

					for (final LogMessage message : messageQueue)
					{
						messageArray.put(message);
					}

					LoggingEndpoint.logToClient(LoggingEndpoint.this, messageArray);
				}
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

	public static void log(final LogMessage logMessage)
	{
		LoggingEndpoint.log(logMessage, true);
	}

	/**
	 * Logs to all JavaScript Logging Endpoints for a specific session
	 * 
	 * @param sessionId
	 *            The session to send the message to
	 * @param level
	 *            The log level e.g. INFO or SEVERE
	 * @param message
	 *            The message to display
	 */
	public static void log(final LogMessage logMessage, final boolean queue)
	{
		final String sessionId = logMessage.getSessionId();
		
		// If sessionID is not specified, notify all endpoints
		if (null == sessionId)
		{
			for (final Set<LoggingEndpoint> endpoints : LoggingEndpoint.endpoints.values())
			{
				logToClient(endpoints, logMessage);
			}

			// Queue message for all sessions
			if (queue)
			{
				queueMessageToAll(logMessage);
			}
		}
		else
		{
			final Set<LoggingEndpoint> sessionEndpoints = LoggingEndpoint.endpoints.get(sessionId);
			if (null != sessionEndpoints)
			{
				logToClient(sessionEndpoints, logMessage);

				if (queue)
				{
					queueMessage(logMessage);
				}
			}
			else
			{
				System.err.println(String.format("No LoggingEndpoints regestered for session '%s'", sessionId));
				System.err.println(logMessage);
			}
		}
	}

	private static void queueMessageToAll(final LogMessage logMessage)
	{
		for (final SortedSet<LogMessage> messageSet : LoggingEndpoint.messages.values())
		{
			messageSet.add(logMessage);
		}
	}
	

	/**
	 * Adds a message to the queue of messages for that session ID
	 * 
	 * @param sessionId
	 *            The HTTP session ID
	 * @param logMessage
	 *            The message to queue
	 */
	private static void queueMessage(final LogMessage logMessage)
	{
		SortedSet<LogMessage> messageQueue = LoggingEndpoint.messages.get(logMessage.getSessionId());

		if (null == messageQueue)
		{
			messageQueue = new TreeSet<>();
		}

		messageQueue.add(logMessage);
		LoggingEndpoint.messages.put(logMessage.getSessionId(), messageQueue);
	}

	/**
	 * Returns the queue of existing messages for a given session
	 * 
	 * @param sessionId
	 *            The session ID for the session messages to be retrieved
	 * @return The queue of messages
	 */
	public static SortedSet<LogMessage> getMessages(final String sessionId)
	{
		return LoggingEndpoint.messages.get(sessionId);
	}

	/**
	 * Logs the JSON message to every provided endpoint
	 * 
	 * @param endpoints
	 *            The endpoints to send the message to
	 * @param message
	 *            The message
	 */
	private static void logToClient(final Set<LoggingEndpoint> endpoints, final LogMessage message)
	{
		if (null != endpoints)
		{
			for (final LoggingEndpoint endpoint : endpoints)
			{
				try
				{
					endpoint.session.getBasicRemote().sendText(message.toJSONString());
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Send an array of messages to an endpoint
	 * 
	 * @param endpoint
	 *            The endpoint to send the messages to
	 * @param jsonArray
	 *            A JSON array of log messages
	 */
	private static void logToClient(final LoggingEndpoint endpoint, final JSONArray jsonArray)
	{
		endpoint.session.getAsyncRemote().sendText(jsonArray.toString());
	}

	public static void clearSession(final String id)
	{
		LoggingEndpoint.messages.remove(id);
	}
}