package storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import models.apiKeys.ApiKey;
import models.apiKeys.KeyMap;
import models.apiKeys.KeyType;

/**
 * UserCache for reading user data out of the database.
 * 
 * @author Matt Rayner
 */
public class KeyCache
{
	private static Logger LOGGER = Logger.getLogger(KeyCache.class.getName());
	
	private static KeyCache INSTANCE;
	
	private final Database database;
	
	private final Map<KeyType, ApiKey> keys = new KeyMap();
	
	/**
	 * Gets a single instance of the UserCache class.
	 * 
	 * @return the UserCache
	 */
	public static KeyCache getInstance()
	{
		if (KeyCache.INSTANCE == null)
		{
			KeyCache.INSTANCE = new KeyCache(Database.getInstance());
		}
		return KeyCache.INSTANCE;
	}
	
	/**
	 * Class Constructor.
	 *
	 * @param database the database to read from.
	 */
	private KeyCache(final Database database)
	{
		this.database = database;
	}
	
	/**
	 * Gets a User by user ID.
	 * 
	 * @param id the id to look for.
	 * @return a user with the specified id or <code>null</code> if none found.
	 */
	public ApiKey getKey (final KeyType keyType)
	{
		final ApiKey key = this.keys.get(keyType);
		if (null != key)
		{
			return key;
		}
		final List<ApiKey> results = this.getKeys(Column.KEY_TYPE, keyType.name());
		return !results.isEmpty() ? results.get(0) : null;
	}
	
	/**
	 * Gets a list of users where the spesified column matches a given value
	 * 
	 * @param column the column to select by.
	 * @param columnValue the value to select from the value.
	 * @return list of users that match that have the given value.
	 */
	private List<ApiKey> getKeys(final Column column, final Object columnValue)
	{
		final QueryRunner run = this.database.getQueryRunner();
		List<ApiKey> foundKeys = null;
		try
		{
			foundKeys = run.query("SELECT * FROM keys WHERE " + column + "=?;",
					new ApiKeyResultsSetHandler(run), columnValue);
		}
		catch (final SQLException e)
		{
			KeyCache.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		if (foundKeys == null || foundKeys.isEmpty())
		{
			return Collections.emptyList();
		}
		for (final ApiKey foundKey : foundKeys)
		{
			this.keys.put(foundKey.getKeyType(), foundKey);
		}
		return foundKeys;
	}
	
	/**
	 * Insert the provided user into the database or updates them if user already exists.
	 * 
	 * @param key the user to put in database.
	 * @throws SQLException if a database access error occurs
	 */
	public void putKey(final ApiKey key) throws SQLException
	{
		final AsyncQueryRunner run = this.database.getAsyncQueryRunner();
		try
		{
			run.update(String.format("INSERT INTO keys (%s, %s) VALUES (?,?)"
					+ "ON CONFLICT (%s) DO UPDATE SET %s = EXCLUDED.%s",
					Column.KEY_VALUE, Column.KEY_TYPE, Column.KEY_VALUE, Column.KEY_TYPE, Column.KEY_TYPE),
							key.getKeyValueWithoutIncrementing(), key.getKeyType());
			run.update(String.format("DELETE FROM key_uses WHERE %s=?;", Column.KEY_VALUE),key.getKeyValueWithoutIncrementing());
			for (final Long timeUsed : key.getTimesUsed())
			{
				run.update(String.format("INSERT INTO key_uses (%s, %s) VALUES (?,?) ON CONFLICT (%s, %s) DO NOTHING",
						Column.KEY_VALUE, Column.DATE_USED, Column.KEY_VALUE, Column.DATE_USED),
						key.getKeyValueWithoutIncrementing(), timeUsed);
			}
			this.keys.put(key.getKeyType(), key);
		}
		catch (SQLException e)
		{
			KeyCache.LOGGER.log(Level.WARNING, e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Results set handler to turn each row of the keys database table into a ApiKey Object
	 * 
	 * @see AbstractListHandler
	 * @author Matt Rayner
	 */
	private class ApiKeyResultsSetHandler extends AbstractListHandler<ApiKey>
	{
		private final QueryRunner queryRunner;
		private ResultSet rs;
		
		/**
		 * Class Constructor.
		 * 
		 * @param queryRunner the queryRunner to use for additional database lookups.
		 */
		public ApiKeyResultsSetHandler(final QueryRunner queryRunner)
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
		protected ApiKey handleRow(final ResultSet rs) throws SQLException
		{
			this.rs = rs;
			//Get the roles for this user form the roles table.
			final List<Long> roles = this.queryRunner.query(
					String.format("SELECT %s FROM key_uses WHERE %s=?;",Column.DATE_USED, Column.KEY_VALUE),
					new ColumnListHandler<Long>(), this.getString(Column.KEY_VALUE));
			
			return new ApiKey(
					this.getString(Column.KEY_VALUE),
					KeyType.valueOf(this.getString(Column.KEY_TYPE)),
					new TreeSet<>(roles));
		}
	}
	
	/**
	 * Database table columns.
	 * 
	 * @author Matt Rayner
	 */
	private enum Column
	{
		KEY_TYPE, KEY_VALUE, DATE_USED;

		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
}
