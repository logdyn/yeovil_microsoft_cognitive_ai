package time;

public class TimeNow implements Timeable {

	public static final TimeNow INSTANCE = new TimeNow();
	
	private TimeNow() {
		
	}
	
	public static TimeNow getInstance()
	{
		return TimeNow.INSTANCE;
	}
	/**
	 * {@inheritDoc}
	 *
	 * @see ai_project.common.time.Timeable#currentTimeMillis()
	 */
	@Override
	public long currentTimeMillis()
	{
		return System.currentTimeMillis();
	}
}
