package businesses;

/**
 * Business interface that allows each feature to register itself with the controller.
 * 
 * @author Matt Rayner
 */
public interface Business
{
	/**
	 * Getter.
	 * 
	 * @return the business title.
	 */
	String getTitle();
	
	/**
	 *  onCreate event. This method is called <b>after</b> the controller has been created.
	 */
	void onCreate();
	
	/**
	 *  onDispose event. This method is called when the main ui window has been closed.
	 */
	void onDispose();
}
