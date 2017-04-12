package storage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

/**
 * Class used to connect to the database.
 * 
 * @author Matt Rayner
 */
public class Database
{
	private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
	
	private static Database INSTANCE;
	
	private final DataSource dataSource;
	
	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	/**
	 * Gets the singleton instance.
	 * 
	 * @return the Database object.
	 */
	public static Database getInstance()
	{
		if (Database.INSTANCE == null)
		{
			try
			{
				Database.INSTANCE = new Database();
			}
			catch (final NamingException e)
			{
				Database.LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return Database.INSTANCE;
	}
	
	public void destroy()
	{
		this.executor.shutdown();
	}
	
	/**
	 * Class Constructor.
	 * 
	 * @throws NamingException if a naming exception is encountered
	 */
	private Database() throws NamingException
	{
		super();
		final Context init = new InitialContext();
		this.dataSource = (DataSource) init.lookup("java:/comp/env/jdbc/YMCA");
	}
	
	/**
	 * Gets a new queryRunner that connects to this database.
	 * 
	 * @return a new QueryRunner.
	 */
	public QueryRunner getQueryRunner()
	{
		return new QueryRunner(this.dataSource);
	}
	
	/**
	 * Gets a new AsyncQueryRunner that connects to this database.
	 * 
	 * @return a new AsyncQueryRunner.
	 */
	public AsyncQueryRunner getAsyncQueryRunner()
	{
		return new AsyncQueryRunner(this.executor, this.getQueryRunner());
	}
}
