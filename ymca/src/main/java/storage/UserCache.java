package storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import models.Address;
import models.user.Digest;
import models.user.User;
import models.user.UserRole;

/**
 * UserCache for reading user data out of the database.
 * 
 * @author Matt Rayner
 */
public class UserCache
{
	private static Logger LOGGER = Logger.getLogger(UserCache.class.getName());
	
	private static UserCache INSTANCE;
	
	private final Database database;
	
	private final Map<UUID, User> users = new HashMap<>();
	
	/**
	 * Gets a single instance of the UserCache class.
	 * 
	 * @return the UserCache
	 */
	public static UserCache getInstance()
	{
		if (UserCache.INSTANCE == null)
		{
			UserCache.INSTANCE = new UserCache(Database.getInstance());
		}
		return UserCache.INSTANCE;
	}
	
	/**
	 * Class Constructor.
	 *
	 * @param database the database to read from.
	 */
	private UserCache(final Database database)
	{
		this.database = database;
	}
	
	/**
	 * Gets a User by user ID.
	 * 
	 * @param id the id to look for.
	 * @return a user with the specified id or <code>null</code> if none found.
	 */
	public User getUser (final UUID id)
	{
		final User mappedUser = this.users.get(id);
		if (null != mappedUser)
		{
			return mappedUser;
		}
		final List<User> results = this.getUsers(Column.USER_ID, id.toString());
		return !results.isEmpty() ? results.get(0) : null;
	}
	
	/**
	 * Gets a User by their username.
	 * 
	 * @param username the username to look for.
	 * @return the user with the provided username or <code>null</code> if none found.
	 */
	public User getUser (final String username)
	{
		if (null == username) {return null;}
		for (final User user : this.users.values())
		{
			if (username.equals(user.getUsername()))
			{
				return user;
			}
		}
		final List<User> results = this.getUsers(Column.USER_NAME, username);
		return !results.isEmpty() ? results.get(0) : null;
	}
	
	/** Gets the user object for the currently logged in user.
	 * 
	 * @param request the http request to identify the current user from.
	 * @return a user object representing the current user.
	 */
	public User getUser (final HttpServletRequest request)
	{
		if (null == request.getUserPrincipal())
		{
			return null;
		}
		return this.getUser(request.getUserPrincipal().getName());
	}
	
	/**
	 * Gets a list of users where the spesified column matches a given value
	 * 
	 * @param column the column to select by.
	 * @param columnValue the value to select from the value.
	 * @return list of users that match that have the given value.
	 */
	private List<User> getUsers(final Column column, final Object columnValue)
	{
		final QueryRunner run = this.database.getQueryRunner();
		List<User> foundUsers = null;
		try
		{
			foundUsers = run.query("SELECT * FROM users WHERE " + column + "=?;",
					new UserResultsSetHandler(run), columnValue);
		}
		catch (final SQLException e)
		{
			UserCache.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		if (foundUsers == null || foundUsers.isEmpty())
		{
			return Collections.emptyList();
		}
		for (final User foundUser : foundUsers)
		{
			this.users.put(foundUser.getUuid(), foundUser);
		}
		return foundUsers;
	}
	
	/**
	 * Insert the provided user into the database or updates them if user already exists.
	 * 
	 * @param user the user to put in database.
	 * @throws SQLException if a database access error occurs
	 */
	public void putUser(final User user) throws SQLException
	{
		final AsyncQueryRunner run = this.database.getAsyncQueryRunner();
		run.update(
		        "INSERT INTO users (user_id, user_name, user_pass, forename, surname, address_id) VALUES (?,?,?,?,?,?)"
		                + "ON CONFLICT (user_id) DO UPDATE SET user_name = EXCLUDED.user_name, user_pass=EXCLUDED.user_pass, forename=EXCLUDED.forename, surname=EXCLUDED.surname, address_id=EXCLUDED.address_id;",
		        user.getUuid(), user.getUsername(), user.getDigest().toString(), user.getForename(), user.getSurname(),
		        user.getAddress().getId());
		run.update("DELETE FROM user_roles WHERE user_name=?;", user.getUsername());
		for (final String rolename : user.getRole().toDatabaseValues())
		{
			run.update(
			        "INSERT INTO user_roles (user_name, role_name) VALUES (?,?) ON CONFLICT (user_name, role_name) DO NOTHING",
			        user.getUsername(), rolename);
		}
		this.users.put(user.getUuid(), user);
	}
	
	/**
	 * Determines if a given username has already been used.
	 * 
	 * @param username the username to check.
	 * @return <tt>true</tt> if the username has already been used, otherwise <tt>false</tt>
	 */
	public boolean isUsernameTaken(final String username)
	{
		return this.getUser(username) != null;
	}
	
	/**
	 * Results set handler to turn each row of the users database into a user Object
	 * 
	 * @see AbstractListHandler
	 * @author Matt Rayner
	 */
	private class UserResultsSetHandler extends AbstractListHandler<User>
	{
		private final QueryRunner queryRunner;
		private ResultSet rs;
		
		/**
		 * Class Constructor.
		 * 
		 * @param queryRunner the queryRunner to use for additional database lookups.
		 */
		public UserResultsSetHandler(final QueryRunner queryRunner)
		{
			this.queryRunner = queryRunner;
		}

		/**
		 * Gets a String value from the current resultsSet.
		 * 
		 * @param column the column to read from.
		 * @return the String value contained in that column.
		 * @throws SQLException if a database access error occurs or this method is called on a closed result set.
		 */
		private String getString(final Column column) throws SQLException
		{
			return this.rs.getString(column.toString());
		}
		
		@Override
		protected User handleRow(final ResultSet rs) throws SQLException
		{
			this.rs = rs;
			//Get the roles for this user form the roles table.
			final List<String> roles = this.queryRunner.query(
					String.format("SELECT %s FROM user_roles WHERE %s=?;",Column.ROLE_NAME, Column.USER_NAME),
					new ColumnListHandler<String>(), this.getString(Column.USER_NAME));
			
			return new User(UUID.fromString(
					this.getString(Column.USER_ID)),
					this.getString(Column.FORENAME),
					this.getString(Column.SURNAME),
					Address.NULL,
					UserRole.fromDatabaseValues(roles),
					this.getString(Column.USER_NAME),
					Digest.fromEncrypted(this.getString(Column.USER_PASS)));
		}
	}
	
	/**
	 * Database table columns.
	 * 
	 * @author Matt Rayner
	 */
	private enum Column
	{
		USER_ID, USER_NAME, USER_PASS, FORENAME, SURNAME, ADDRESS_ID, ROLE_NAME;

		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
}
