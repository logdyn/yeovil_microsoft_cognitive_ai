package businesses;

/**
 * Base Business class to set default behaviour for creation and disposal.
 * 
 * @author Matt Rayner
 */
public abstract class BaseBusiness implements Business
{
	/** The Application Controller. */
	protected Controller controller;
	
	/**
	 * Class Constructor.
	 *
	 * @param controller - the application controller.
	 */
	public BaseBusiness(final Controller controller)
	{
		super();
		this.controller = controller;
		this.controller.addBusiness(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see ai_project.businesses.interfaces.Business#onCreate()
	 */
	@Override
	public void onCreate()
	{
		//Do nothing.
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see ai_project.businesses.interfaces.Business#onDispose()
	 */
	@Override
	public void onDispose()
	{
		// Do nothing.
	}
}
