package models.user;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Matt Rayner
 */
public enum UserRole
{
	/** Role assigned to a user with no permissions. */
	NULL()
	{
		@Override
		public Set<String> toDatabaseValues()
		{
			// returns an empty set as NULL role should not be entered into database.
			return Collections.emptySet();
		}

		@Override
		UserRole searchParents(final Collection<String> name)
		{
			//EVERY user has the NULL role.
			return this;
		}
	},
	/** Default role for new users. */
	USER(NULL),
	/** role for admin users.  */
	ADMIN(USER);
	
	private UserRole parentRole;
	private Set<String> dbValues;
	private int rank;
	
	/**
	 * Class Constructor.
	 */
	private UserRole()
	{
		this(null);
	}
	
	/**
	 * Class Constructor.
	 *
	 * @param parentRole the role that this role should extend.
	 */
	private UserRole(final UserRole parentRole)
	{
		if (parentRole != null)
		{
			this.parentRole = parentRole;
			this.rank = this.parentRole.rank + 1;
		}
	}
	
	/**
	 * Gets all parent roles that this role contains.
	 * 
	 * @return Set of role names.
	 */
	public Set<String> toDatabaseValues()
	{
		//dvValues is cached so this calculation only happens once if needed.
		if (this.dbValues == null)
		{
			//gets the database values from the parent.
			final Collection<String> parentValues = this.parentRole.toDatabaseValues();
			final Set<String> result = new LinkedHashSet<>(parentValues.size() + 1);
			result.add(this.name().toLowerCase()); // adds it's own database role.
			result.addAll(parentValues); // add parent database roles.
			this.dbValues = Collections.unmodifiableSet(result);
		}
		return this.dbValues;
	}
	
	/**
	 * Recursively searches for the highest value role that is a parent of this role.
	 * 
	 * @param names the names of roles to search for.
	 * @return UserRole
	 */
	UserRole searchParents(final Collection<String> names)
	{
		if(names.contains(this.name().toLowerCase()))
		{
			return this;
		}
		return this.parentRole.searchParents(names);
	}
	
	public boolean isUserinRole(final HttpServletRequest request)
	{
		return request.isUserInRole(this.name().toLowerCase());
	}
	
	/**
	 * Gets the default User Role
	 * 
	 * @return User role
	 * @see UserRole#USER
	 */
	public static UserRole getDefault()
	{
		return UserRole.USER;
	}
	
	/**
	 * Recursively searches for the highest value role that is named in input list.
	 * 
	 * @param names the names of roles to search for.
	 * @return UserRole
	 */
	public static UserRole fromDatabaseValues(final Collection<String> databaseValues)
	{
		//Start searching in Admin role as it is the highest up role.
		return UserRole.ADMIN.searchParents(databaseValues);
	}
}
