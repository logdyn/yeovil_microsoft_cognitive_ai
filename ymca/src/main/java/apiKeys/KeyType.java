package apiKeys;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import time.TimeFactory;

/**
 * Enum used for the Type of API key.
 */
public enum KeyType
{
	/** Type used for vision keys. */
	Vision(5000, 1, 20, 1),
	/** Type used for Test to Speech. */
	TextToSpeech(5000, 1, 20, 1);
	
	/** number of times keys of this type can be used within numberOfMonths. */
	private int monthsUseLimit = Integer.MAX_VALUE;
	
	/** number of months the monthly use limit extends for. */
	private int numberOfMonths;
	
	/** number of times key can be used within numberOfMinutes. */
	private int minutesUseLimit = Integer.MAX_VALUE;
	
	/** number of minutes the minute limit is for. */
	private int numberOfMinutes;
	
	/**
	 * Class Constructor.
	 *
	 * @param monthlyUseLimit
	 * @param monthlyLimitLength
	 */
	private KeyType(final int monthlyUseLimit, final int monthlyLimitLength)
	{
		this(monthlyUseLimit, monthlyLimitLength, Integer.MAX_VALUE, 0);
	}
	
	/**
	 * Class Constructor.
	 *
	 * @param monthsLimit
	 * @param noOfMonths
	 * @param minutesLimit
	 * @param noOfMinutes
	 */
	private KeyType(final int monthsLimit, final int noOfMonths, final int minutesLimit, final int noOfMinutes)
	{
		this.monthsUseLimit = monthsLimit;
		this.numberOfMonths = noOfMonths;
		this.minutesUseLimit = minutesLimit;
		this.numberOfMinutes = noOfMinutes;
	}
	
	private boolean isOverMonthUseLimit(final ApiKey key)
	{
		return this.numberOfMonths != 0 && key.getTimesUsed().tailSet(Long.valueOf(TimeFactory.timeMonthsAgo(this.numberOfMonths))).size() >= this.monthsUseLimit;
	}
	
	private boolean isOverMinuteUseLimit(final ApiKey key)
	{
		return this.numberOfMinutes != 0 && key.getTimesUsed().tailSet(Long.valueOf(TimeFactory.timeMinutesAgo(this.numberOfMinutes))).size() >= this.minutesUseLimit;
	}
	
	/**
	 * Tests if the provided Key is valid.
	 * 
	 * @param key - the key to test.
	 * @return <code>true</code> if the key is valid.
	 */
	public boolean isKeyValid(final ApiKey key)
	{
		return !this.isOverMonthUseLimit(key) && !this.isOverMinuteUseLimit(key);
	}
	
	public static String[] names()
	{
		final String[] names = new String[KeyType.values().length];
		for (int i = 0; i < names.length; i++)
		{
			names[i] = KeyType.values()[i].name();
		}
		return names;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Type name", this.name())
				.append("Monthly use limit", this.monthsUseLimit)
				.append("Monthly limit length", this.numberOfMonths)
				.append("Minute use limit", this.minutesUseLimit)
				.append("Minute limit length", this.numberOfMinutes)
				.build();
	}
}
