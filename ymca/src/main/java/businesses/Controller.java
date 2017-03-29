package businesses;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import apiKeys.KeyMap;
import apiKeys.storage.ReadableCSVFileStorage;
import apiKeys.storage.WriteableCSVFileStorage;
import apiKeys.storage.interfaces.AbstractReadableKeyStorage;
import apiKeys.storage.interfaces.AbstractWriteableKeyStorage;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * Application Controller.
 * This class links all the business and provides a central location to process and execute background tasks.
 * 
 * @author Matt Rayner
 */
public class Controller
{
	/** Executor service for background business tasks. */
	private final ExecutorService exServ;
	/** Set of registered businesses. */
	private final Set<Business> businesses;
	/** Key storage to populate keyMap with. */
	private final AbstractReadableKeyStorage keyReader;
	
	private final AbstractWriteableKeyStorage keyWriter;
	
	/**
	 * Class Constructor.
	 *
	 * @param mainUI The main Application UI.
	 */
	public Controller()
	{
		this.exServ = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.businesses = new HashSet<>();

		this.keyReader = new ReadableCSVFileStorage();
		this.keyWriter = new WriteableCSVFileStorage();
	}
	
	/**
	 * onCreate event. This method is called after the controller has been created.
	 */
	public void onCreation()
	{
		this.keyReader.setOnSucceeded(new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(final WorkerStateEvent event)
			{
				KeyMap.getInstance().addAll(Controller.this.keyReader.getValue());
			}
		});
		this.exServ.execute(this.keyReader);
		for (Business business : this.businesses)
		{
			business.onCreate();
		}
	}
	
	/**
	 * onDispose event. This method is called when the main ui window has been closed.
	 */
	public void onDispose()
	{
		for (Business business : this.businesses)
		{
			business.onDispose();
		}
		this.keyWriter.addAll(KeyMap.getInstance().values());
		this.exServ.execute(this.keyWriter);
		this.exServ.shutdown();
	}
	
	/**
	 * Adds a business to the Controller.
	 * 
	 * @param business the business to add.
     * @return <tt>true</tt> if this set did not already contain the specified element
	 */
	public boolean addBusiness(final Business business)
	{
		return this.businesses.add(business);
	}
	
	/**
     * Executes the given command at some time in the future.
     *
     * @param task the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
	public void execute(final Runnable task)
	{
		this.exServ.execute(task);
	}
	
	/**
     * Submits a Runnable task for execution and returns a Future
     * representing that task. The Future's {@code get} method will
     * return {@code null} upon <em>successful</em> completion.
     *
     * @param task the task to submit
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     * @throws NullPointerException if the task is null
     */
	public Future<?> submit(final Runnable task)
	{
		return this.exServ.submit(task);
	}
	
	/**
     * Submits a value-returning task for execution and returns a
     * Future representing the pending results of the task. The
     * Future's {@code get} method will return the task's result upon
     * successful completion.
     *
     * @param task the task to submit
     * @param <T> the type of the task's result
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     * @throws NullPointerException if the task is null
     */
	public <T> Future<T> submit(final Callable<T> task)
	{
		return this.exServ.submit(task);
	}
}
