package models;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.json.JSONObject;

public abstract class Person
{
	private final UUID id;
	private final String forename;
	private final String surname;

	public Person(final String forename, final String surname)
	{
		this(UUID.randomUUID(), forename, surname);
	}

	public Person(final UUID uuid, final String forename, final String surname)
	{
		this.id = uuid;
		this.forename = forename;
		this.surname = surname;
	}
	
	protected Person(final Builder builder)
	{
		this(builder.id, builder.forename, builder.surname);
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid()
	{
		return this.id;
	}

	/**
	 * @return the forename
	 */
	public String getForename()
	{
		return this.forename;
	}

	/**
	 * @return the surname
	 */
	public String getSurname()
	{
		return this.surname;
	}
	
	public abstract static class Builder
	{
		private UUID id = UUID.randomUUID();
		private String forename;
		private String surname;
		
		public Builder(final String forename, final String surname)
		{
			this.forename = forename;
			this.surname = surname;
		}
		
		public Builder fromJSON(final JSONObject person) throws NoSuchAlgorithmException
		{
			this.forename = person.getString("forename");
			this.surname = person.getString("surname");
			final String idString = person.optString("userid", null);
			if (null != idString)
			{
				this.id = UUID.fromString(idString);
			}
			return this;
		}
		
		public Builder(final Person value)
		{
			this.id = value.id;
			this.forename = value.forename;
			this.surname = value.surname;
		}
		
		public Builder id(final UUID value)
		{
			this.id = value;
			return this;
		}
		
		public Builder forename(final String value)
		{
			this.forename = value;
			return this;
		}
		
		public Builder surname(final String value)
		{
			this.surname = value;
			return this;
		}
	}
}
