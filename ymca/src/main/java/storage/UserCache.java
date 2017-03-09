package storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import models.Address;
import models.user.Digest;
import models.user.User;
import models.user.UserRole;

public class UserCache
{
	private static UserCache INSTANCE;
	
	private final Database database;
	
	private final Set<User> users = new HashSet<>();
	
	public static UserCache getInstance()
	{
		if (UserCache.INSTANCE == null)
		{
			UserCache.INSTANCE = new UserCache(Database.getInstance());
		}
		return UserCache.INSTANCE;
	}
	
	private UserCache(final Database database)
	{
		this.database = database;
	}
	
	public User getUser (final UUID id)
	{
		return this.getUsers(Column.USER_ID, id.toString()).get(0);
	}
	
	public User getUser (final String username)
	{
		return this.getUsers(Column.USER_NAME, username).get(0);
	}
	
	public User getUser (final HttpServletRequest request)
	{
		return this.getUser(request.getUserPrincipal().getName());
	}
	
	private List<User> getUsers(final Column column, final Object columnValue)
	{
		final QueryRunner run = this.database.getQueryRunner();
		List<User> foundUsers = null;
		try
		{
			foundUsers = run.query("SELECT * FROM users WHERE " + column + "=?;",
					new UserResultsSetHandler(), columnValue);
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		if (foundUsers == null || foundUsers.isEmpty())
		{
			return null;
		}
		this.users.addAll(foundUsers);
		return foundUsers;
	}
	
	/**
	 * Insert the provided user into the database or updates them if user already exists.
	 * 
	 * @param user the user to put in database.
	 * @throws SQLException
	 */
	public void putUser(final User user) throws SQLException
	{
		final AsyncQueryRunner run = this.database.getAsyncQueryRunner();
		run.update("INSERT INTO users (user_id, user_name, user_pass, forename, surname, address_id) VALUES (?,?,?,?,?,?)"
		+ "ON CONFLICT (user_id) DO UPDATE SET user_name = EXCLUDED.user_name, user_pass=EXCLUDED.user_pass, forename=EXCLUDED.forename, surname=EXCLUDED.surname, address_id=EXCLUDED.address_id;",
				user.getUuid(), user.getUsername(), user.getDigest().toString(), user.getForename(), user.getSurname(), user.getAddress().getId());
		for (final String rolename : user.getRole().toDatabaseValues())
		{
			run.update("INSERT INTO user_roles (user_name, role_name) VALUES (?,?) ON CONFLICT (user_name, role_name) DO NOTHING", user.getUsername(), rolename);
		}
	}
	
	public boolean isUsernameTaken(final String username)
	{
		return this.getUser(username) == null;
	}
	
	/**
	 * Results set handler to turn each row of the users database into a user Object
	 * 
	 * @see AbstractListHandler
	 * @author Matt Rayner
	 */
	private class UserResultsSetHandler extends AbstractListHandler<User>
	{
		private ResultSet rs;
		
		private String getString(final Column column) throws SQLException
		{
			return this.rs.getString(column.toString());
		}
		
		@Override
		protected User handleRow(final ResultSet rs) throws SQLException
		{
			//Get the roles for this user form the roles table.
			final QueryRunner roleRunner = UserCache.this.database.getQueryRunner();
			final List<String> roles = roleRunner.query(
					"SELECT role_name FROM user_roles WHERE user_name=?;",
					new ColumnListHandler<String>(), this.getString(Column.USER_NAME));
			
			return new User(
					UUID.fromString(this.getString(Column.USER_ID)),
					this.getString(Column.FORENAME), 
					this.getString(Column.SURNAME),
					Address.NULL,
					UserRole.fromDatabaseValues(roles),
					this.getString(Column.USER_NAME),
					Digest.fromEncrypted(this.getString(Column.USER_PASS)));
		}
	}
	
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
