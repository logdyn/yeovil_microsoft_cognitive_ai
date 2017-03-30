package models.apiKeys;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import models.time.TimeFactory;

/**
 * This Class represents a Microsoft Cognitive Services Api Key.
 * 
 * @author Matt Rayner
 */
public final class ApiKey extends Observable implements Comparable<ApiKey>
{

	/** Value of API key. */
	private final String key;
	
	/** Storage of all the times the key has been used. */
	private final NavigableSet<Long> timesUsed;
	
	/** Type of Key */
	private final KeyType keyType;
	
	/**
	 * Class Constructor.
	 *
	 * @param key - the Key to use.
	 * @param keyType
	 * @param timesUsed - the number of times the key has already been used.
	 */
	public ApiKey(final String key, final KeyType keyType, final Set<Long> timesUsed)
	{
		super();
		this.key = key;
		this.keyType = keyType;
		this.timesUsed = new TreeSet<>(timesUsed);
	}
	
	/**
	 * Class Constructor.
	 * 
	 * Sets the times used to <code>0</code> (zero).
	 * 
	 * @param key - the Key to use.
	 * @param keyType
	 */
	public ApiKey(final String key, final KeyType keyType)
	{
		this(key, keyType , Collections.emptySet());
	}
	
	/**
	 * Gets the Key and increments the number of times key has been used.
	 * 
	 * @return the String value of the Key.
	 */
	public String getKeyValue()
	{
		if(this.isValid())
		{
			this.timesUsed.add(Long.valueOf(TimeFactory.currentTimeMillis()));
			this.setChanged();
			this.notifyObservers(this.key);
			return this.key;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Gets the key value but does not add to use counter.
	 * This should only be used when storing keys. Instead use {@link #getKeyValue()}
	 * 
	 * @return the String value of the Key.
	 */
	public String getKeyValueWithoutIncrementing()
	{
		return this.key;
	}
	
	/**
	 * Get number of times key has been used.
	 * 
	 * @return Number of times key has been used.
	 */
	public NavigableSet<Long> getTimesUsed()
	{
		return Collections.unmodifiableNavigableSet(this.timesUsed);
	}
	
	/**
	 * Gets the type of this key.
	 * 
	 * @return a KeyType.
	 */
	public KeyType getKeyType()
	{
		return this.keyType;
	}
	
	/**
	 * is this key valid
	 * 
	 * @return true if this key is still valid.
	 */
	public boolean isValid()
	{
		return this.keyType.isKeyValid(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final ApiKey otherKey)
	{
		return new CompareToBuilder()
				.append(this.keyType, otherKey.keyType)
				.append(this.isValid(), otherKey.isValid())
				.append(this.timesUsed.size(), otherKey.timesUsed.size())
				.append(this.key, otherKey.key)
				.toComparison();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(this.key)
				.append(this.timesUsed)
				.append(this.keyType)
				.toHashCode();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
		{
			return true;
		}
		else if (!(obj instanceof ApiKey))
		{
			return false;
		}
		else
		{
			final ApiKey otherKey = (ApiKey) obj;
			return new EqualsBuilder()
					.appendSuper(super.equals(obj))
					.append(this.key, otherKey.key)
					.append(this.timesUsed, otherKey.timesUsed)
					.append(this.keyType, otherKey.keyType)
					.isEquals();
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("Key value", this.key)
				.append("Key Type", this.keyType)
				.append("Times used", this.timesUsed)
				.build();
	}
}
