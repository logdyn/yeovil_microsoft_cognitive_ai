package models.user;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.json.JSONObject;

import models.Address;

public class User
{
	private final UUID id;
	private final String forename;
	private final String surname;	
	private final Address address;	
	private final UserRole role;	
	private final String username;	
	private final Digest digest;
	
	protected User(final Builder builder)
	{
		this.id = builder.id;
		this.forename = builder.forename;
		this.surname = builder.surname;
		this.address = builder.address;
		this.role = builder.role;
		this.username = builder.username;
		this.digest = builder.digest;
	}
	
	
	public User(final UUID id, final String forename, final String surname, final Address address, final UserRole role, final String username,
	        final Digest digest)
	{
		super();
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.address = address;
		this.role = role;
		this.username = username;
		this.digest = digest;
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
	
	public Address getAddress()
	{
		return this.address;
	}
	
	public UserRole getRole()
	{
		return this.role;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public Digest getDigest()
	{
		return this.digest;
	}
	
	public static class Builder
	{
		private UUID id = UUID.randomUUID();
		private String forename;
		private String surname;
		private Address address = Address.NULL;
		private UserRole role = UserRole.getDefault();
		private String username;
		private Digest digest;
		
		public Builder(final String forename, final String surname)
		{
			this.forename = forename;
			this.surname = surname;
		}
		
		public Builder fromJSON(final JSONObject user) throws NoSuchAlgorithmException
		{
			this.forename = user.getString("forename");
			this.surname = user.getString("surname");			
			final String idString = user.optString("userid", null);
			
			if (null != idString)
			{
				this.id = UUID.fromString(idString);
			}
			
			this.username = user.optString("username", this.username);
			if (user.has("password"))
			{
				this.digest = Digest.fromPlainText(user.getString("password"));
			}
			
			//this.address = TODO
			
			return this;
		}
		
		public Builder(final User value)
		{
			this.id = value.id;
			this.forename = value.forename;
			this.surname = value.surname;
			this.address = value.address;
			this.digest = value.digest;
			this.username = value.username;
			this.role = value.role;
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
		
		public Builder address(final Address value)
		{
			this.address = value;
			return this;
		}
		
		public Builder role(final UserRole value)
		{
			this.role = value;
			return this;
		}
		
		public Builder username(final String value)
		{
			this.username = value;
			return this;
		}
		
		public Builder digest(final Digest value)
		{
			this.digest = value;
			return this;
		}
		
		public User build()
		{
			return new User(this);
		}
		
		public User toUser()
		{
			return this.build();
		}
	}
}
