package models;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.json.JSONObject;
import org.json.JSONString;

import models.time.TimeFactory;

public class LogMessage implements Comparable<LogMessage>, JSONString
{
	private final String sessionId;
	private final Level level;
	private final String message;
	private final long timestamp;
	
	public LogMessage(final String message)
	{
		this(Level.FINE, message);
	}
	
	public LogMessage(final Level level, final String message)
	{
		this((String) null, level, message);
	}
	
	public LogMessage(final HttpServletRequest request, final Level level, final String message)
	{
		this(request.getRequestedSessionId(), level, message, TimeFactory.currentTimeMillis());
	}
	
	public LogMessage(final String sessionId, final Level level, final String message)
	{
		this(sessionId, level, message, TimeFactory.currentTimeMillis());
	}
	
	public LogMessage(final String sessionId, final Level level, final String message, final long time)
	{
		this.sessionId = sessionId;
		this.level = level;
		this.message = message;
		this.timestamp = time;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId()
	{
		return sessionId;
	}

	/**
	 * @return the level
	 */
	public Level getLevel()
	{
		return level;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	@Override
	public int compareTo(final LogMessage other)
	{
		return new CompareToBuilder()
			.append(this.timestamp, other.timestamp)
			.append(this.level.intValue(), other.level.intValue())
			.append(this.message, other.message)
			.build();
	}

	@Override
	public String toJSONString()
	{
		return new JSONObject()
				.put("sessionId", this.sessionId)
				.put("level", this.level.getName())
				.put("message", this.message)
				.put("timestamp", this.timestamp)
				.toString();
	}
	
	@Override
	public String toString()
	{
		return this.toJSONString();
	}
}
