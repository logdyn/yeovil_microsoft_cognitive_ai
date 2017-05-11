package models;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import models.time.TimeFactory;

/**
 * Java object containing message details: sessionId, level, message, timestamp
 * @author jsjlewis96
 */
public class LogMessage implements Comparable<LogMessage>, JSONString
{
	private final String sessionId;
	private final Level level;
	private final String message;
	private final long timestamp;
	private String jsonString;
	private int hashCode = -1;
	
	/**
	 * Constructor
	 * Defaults level to FINE ({@link java.util.logging.Level#FINE})
	 * @param message The message to log
	 */
	public LogMessage(final String message)
	{
		this(Level.FINE, message);
	}
	
	/**
	 * Constructor
	 * No session ID is specified, takes a level and message
	 * @param level The log level, see {@link java.util.logging.Level}
	 * @param message The message to be displayed
	 */
	public LogMessage(final Level level, final String message)
	{
		this((String) null, level, message);
	}
	
	/**
	 * Constructor
	 * Uses the {@link javax.servlet.http.HttpServletRequest#getRequestedSessionId()} to get the session ID of the request
	 * @param request The HttpServletRequest
	 * @param level The log level, see {@link java.util.logging.Level}
	 * @param message The message to be displayed
	 */
	public LogMessage(final HttpServletRequest request, final Level level, final String message)
	{
		this(request.getRequestedSessionId(), level, message, TimeFactory.currentTimeMillis());
	}
	
	/**
	 * Constructor
	 * Populates message with current system time in milliseconds
	 * @param sessionId The HttpSessionId
	 * @param level The log level, see {@link java.util.logging.Level}
	 * @param message The message to be displayed
	 */
	public LogMessage(final String sessionId, final Level level, final String message)
	{
		this(sessionId, level, message, TimeFactory.currentTimeMillis());
	}
	
	public LogMessage(final JSONObject jsonObject) throws JSONException
	{
		this.sessionId = jsonObject.optString("sessionId", null);
		this.level = Level.parse(jsonObject.optString("level", "FINE"));
		this.message = jsonObject.getString("message");
		this.timestamp = jsonObject.optLong("timestamp", TimeFactory.currentTimeMillis());
	}
	
	/**
	 * Constructor
	 * Ideally other constructors should be used
	 * @param sessionId The HttpSessionId
	 * @param level The log level, see {@link java.util.logging.Level}
	 * @param message The message to be displayed
	 * @param timestamp The timestamp the message is logged at (in milliseconds)
	 */
	public LogMessage(final String sessionId, final Level level, final String message, final long timestamp)
	{
		this.sessionId = sessionId;
		this.level = level;
		this.message = message;
		this.timestamp = timestamp;
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
		if (this.jsonString == null)
		{
			this.jsonString = new JSONObject()
				.put("sessionId", this.sessionId)
				.put("level", this.level.getName())
				.put("message", this.message)
				.put("timestamp", this.timestamp)
				.toString();
		}
		
		return this.jsonString;
	}
	
	@Override
	public String toString()
	{
		return this.toJSONString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		if (this.hashCode == -1)
		{
			this.hashCode = new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(this.timestamp)
				.append(this.message)
				.append(this.sessionId)
				.append(this.level)
				.build();
		}
		
		return this.hashCode;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object other)
	{
		if (other instanceof LogMessage)
		{
			return new EqualsBuilder()
				.appendSuper(super.equals(other))
				.append(this.timestamp, ((LogMessage) other).timestamp)
				.append(this.message, ((LogMessage) other).message)
				.append(this.sessionId, ((LogMessage) other).sessionId)
				.append(this.level, ((LogMessage) other).level)
				.build();
		}
		else
		{
			return false;
		}
		
	}
	
	
}
