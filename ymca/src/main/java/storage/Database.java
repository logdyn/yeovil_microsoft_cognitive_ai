package storage;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

/**
 * @author Matt Rayner
 */
public class Database
{
	private static Database INSTANCE;
	
	private final DataSource ds;
	
	private QueryRunner qr;
	
	private AsyncQueryRunner asqr;
	
	private final ExecutorService xs = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	public static Database getInstance()
	{
		if (Database.INSTANCE == null)
		{
			try
			{
				Database.INSTANCE = new Database();
			}
			catch (NamingException e)
			{
				e.printStackTrace();
			}
		}
		return Database.INSTANCE;
	}
	
	/**
	 * Class Constructor.
	 * @throws NamingException
	 * @throws SQLException
	 */
	private Database() throws NamingException
	{
		super();
		Context init = new InitialContext();
		this.ds = (DataSource) init.lookup("java:/comp/env/jdbc/YHP");
	}
	
	public QueryRunner getQueryRunner()
	{
		if (this.qr == null)
		{
			this.qr = new QueryRunner(this.ds);
		}
		return this.qr;
	}
	
	public AsyncQueryRunner getAsyncQueryRunner()
	{
		if (this.asqr == null)
		{
			this.asqr = new AsyncQueryRunner(this.xs, this.getQueryRunner());
		}
		return this.asqr;
	}
}
