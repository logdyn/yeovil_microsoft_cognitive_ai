package endpoints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			public void onMessage(final String jsonString)
			{
				JSONObject jsonObject = new JSONObject(jsonString);
				
				if (jsonObject.has("httpSessionId"))
				{
					LoggingEndpoint.this.sessionId = jsonObject.getString("httpSessionId");
					Set<LoggingEndpoint> set = LoggingEndpoint.endpoints.get(LoggingEndpoint.this.sessionId);

					if (null == set)
					{
						set = new HashSet<>();
						LoggingEndpoint.endpoints.put(LoggingEndpoint.this.sessionId, set);
					}

					set.add(LoggingEndpoint.this);

					final SortedSet<LogMessage> messageQueue = LoggingEndpoint.messages.get(LoggingEndpoint.this.sessionId);
					
					if (null != messageQueue && !messageQueue.isEmpty())
					{
						LoggingEndpoint.logToClient(LoggingEndpoint.this, new JSONArray(messageQueue).toString());
					}
				}
				else
				{					
					try
					{
						jsonObject.put("sessionId", LoggingEndpoint.this.sessionId);
						LogMessage logMessage = new LogMessage(jsonObject);
						queueMessage(logMessage);
						
						for (LoggingEndpoint endpoint : LoggingEndpoint.endpoints.get(LoggingEndpoint.this.sessionId))
						{
							if (!LoggingEndpoint.this.equals(endpoint))
							{
								logToClient(endpoint, logMessage.toJSONString());
							}
						}
					}
					catch (JSONException ex)
					{
						log(new LogMessage(Level.WARNING, ex.getMessage()));
					}
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

	/**
	 * Logs a message to the client, see {@link LoggingEndpoint#log(LogMessage, boolean)}
	 * @param logMessage The {@link LogMessage} to log
	 */
	public static void log(final LogMessage logMessage)
	{
		LoggingEndpoint.log(logMessage, true);
	}

	/**
	 * Logs a message to the endpoint for that message, if no message is specified it will log to all endpoints
	 * @param logMessage The {@link LogMessage} to log
	 * @param queue Boolean value, if true queue the message
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
				System.err.println(String.format("No LoggingEndpoints registered for session '%s'", sessionId));
				System.err.println(logMessage);
				LoggingEndpoint.log(new LogMessage(Level.WARNING, 
						String.format("No LoggingEndpoints registered for session '%s'", sessionId)));
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
	 * @param sessionId The HTTP session ID
	 * @param logMessage The {@link LogMessage} to queue
	 */
	private static void queueMessage(final LogMessage logMessage)
	{
		SortedSet<LogMessage> messageQueue = LoggingEndpoint.messages.get(logMessage.getSessionId());

		if (null == messageQueue)
		{
			messageQueue = new TreeSet<>();
			LoggingEndpoint.messages.put(logMessage.getSessionId(), messageQueue);
		}

		messageQueue.add(logMessage);
	}

	/**
	 * Logs the JSON message to every provided endpoint
	 * 
	 * @param endpoints The endpoints to send the message to
	 * @param message The message
	 */
	private static void logToClient(final Set<LoggingEndpoint> endpoints, final LogMessage message)
	{
		if (null != endpoints)
		{
			for (final LoggingEndpoint endpoint : endpoints)
			{
				logToClient(endpoint, message.toJSONString());
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
	private static void logToClient(final LoggingEndpoint endpoint, final String message)
	{
		endpoint.session.getAsyncRemote().sendText(message);
	}

	public static void clearSession(final String id)
	{
		LoggingEndpoint.messages.remove(id);
	}
}