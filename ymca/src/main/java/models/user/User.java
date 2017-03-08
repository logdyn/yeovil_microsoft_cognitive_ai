package models.user;

import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import models.Address;
import models.Person;

public class User extends Person
{
	
	private final Address address;
	
	private final UserRole role;
	
	private final String username;
	
	private final Digest digest;
	
	protected User(final Builder builder)
	{
		super(builder);
		this.address = builder.address;
		this.role = builder.role;
		this.username = builder.username;
		this.digest = builder.digest;
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
	
	public static class Builder extends Person.Builder
	{
		private Address address = Address.NULL;
		private UserRole role = UserRole.getDefault();
		private String username;
		private Digest digest;
		
		public Builder(final String forename, final String surname)
		{
			super(forename, surname);
		}
		
		@Override
		public Builder fromJSON(final JSONObject user) throws NoSuchAlgorithmException
		{
			super.fromJSON(user);
			this.username = user.getString("username");
			if (user.has("password"))
			{
				this.digest = Digest.fromPlainText(user.getString("password"));
			}
			//this.address = TODO
			return this;
		}
		
		public Builder(final User value)
		{
			super(value);
			this.address = value.address;
			this.digest = value.digest;
			this.username = value.username;
			this.role = value.role;
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
