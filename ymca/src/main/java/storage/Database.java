package storage;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

import com.logdyn.api.endpoints.LoggingEndpoint;
import com.logdyn.api.model.LogMessage;

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
	
	private final ExecutorService executor = LoggingThreadPool.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
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
	
	public static String toSQLArray(final Collection<?> collection)
	{
		final StringBuilder result = new StringBuilder(collection.size() * 8); //guess at number of chars
		for (final Object object : collection)
		{
			if (object instanceof Number)
			{
				result.append(object);
			}
			else
			{
				result.append('\'');
				result.append(object);
				result.append('\'');
			}
			result.append(',');
		}
		result.deleteCharAt(result.length() - 1); // remove last comma
		return result.toString();
	}
	
	public static class LoggingThreadPool extends ThreadPoolExecutor
	{
		public static LoggingThreadPool newFixedThreadPool(final int nThreads)
		{
			return new LoggingThreadPool(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
		}
		
		
		public LoggingThreadPool(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit,
				final BlockingQueue<Runnable> workQueue)
		{
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		protected void afterExecute(final Runnable r, Throwable t)
		{
			if (null == t && r instanceof Future)
			{
				try
				{
					((Future<?>) r).get();
				}
				catch (CancellationException ce)
				{
					t = ce;
				}
				catch (ExecutionException ee)
				{
					t = ee.getCause();
				}
				catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
			}
			if (null != t)
			{
				LoggingEndpoint.log(new LogMessage(Level.SEVERE, t.getLocalizedMessage()));
			}
		}
	}
}
