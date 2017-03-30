/**
 * 
 */
package models.apiKeys;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Matt
 *
 */
public class KeyMap implements Map<KeyType, ApiKey>
{
	
	private final Map<KeyType, SortedSet<ApiKey>> keySetMap;

	public KeyMap()
	{
		this.keySetMap = new EnumMap<>(KeyType.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#size()
	 */
	@Override
	public int size()
	{
		int size = 0;
		for (Collection<ApiKey> keySet : this.keySetMap.values())
		{
			size += keySet.size();
		}
		return size;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		if (!this.keySetMap.isEmpty())
		{
			for (SortedSet<ApiKey> keySet : this.keySetMap.values())
			{
				if (!keySet.isEmpty())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key)
	{
		return this.keySetMap.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value)
	{
		for (Collection<ApiKey> keySet : this.keySetMap.values())
		{
			if (keySet.contains(value))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public ApiKey get(final Object keyType)
	{
		final Collection<ApiKey> keySet = this.keySetMap.get(keyType);
		if (keySet != null)
		{
			for (final ApiKey key : keySet)
			{
				if (key.isValid())
				{
					return key;
				}
			}
		}
		return null;
	}
	
	public ApiKey add(final ApiKey key)
	{
		return this.put(key.getKeyType(), key);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ApiKey put(final KeyType keyType, final ApiKey key)
	{
		SortedSet<ApiKey> keySet = this.keySetMap.get(key);
		if (keySet == null)
		{
			keySet = new TreeSet<>();
			this.keySetMap.put(keyType, keySet);
		}
		return keySet.add(key) ? null : key;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public ApiKey remove(final Object key)
	{
		if (key instanceof ApiKey)
		{
			final ApiKey apiKey = (ApiKey) key;
			return this.keySetMap.get(apiKey.getKeyType()).remove(apiKey) ? apiKey : null;
		}
		return null;
	}
	
	public void addAll(final Collection<ApiKey> keys)
	{
		for (final ApiKey key : keys)
		{
			this.add(key);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends KeyType, ? extends ApiKey> map)
	{
		for (Map.Entry<? extends KeyType, ? extends ApiKey> entry : map.entrySet())
		{
			this.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear()
	{
		for (Collection<ApiKey> keySet : this.keySetMap.values())
		{
			keySet.clear();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<KeyType> keySet()
	{
		return this.keySetMap.keySet();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<ApiKey> values()
	{
		final Collection<ApiKey> values = new LinkedList<>();
		for (Collection<ApiKey> keySet : this.keySetMap.values())
		{
			values.addAll(keySet);
		}
		return values;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<Map.Entry<KeyType, ApiKey>> entrySet()
	{
		final Set<java.util.Map.Entry<KeyType, ApiKey>> entrySet = new LinkedHashSet<>(this.size());
		for (Map.Entry<KeyType, SortedSet<ApiKey>> keySet : this.keySetMap.entrySet())
		{
			for (ApiKey key : keySet.getValue())
			{
				entrySet.add(new SimpleEntry<KeyType, ApiKey>(keySet.getKey(), key));
			}
		}
		return entrySet;
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
				.append(this.keySetMap.hashCode())
				.toHashCode();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		else if (!(obj instanceof KeyMap))
		{
			return false;
		}
		else
		{
			return ((KeyMap) obj).keySetMap.equals(this.keySetMap);
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
		return this.keySetMap.toString();
	}
}
