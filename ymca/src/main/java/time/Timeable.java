package time;

public interface Timeable {
	/**
	 * Gets the time to be used by the whole system.
	 * 
	 * @return the current Time.
	 */
	long currentTimeMillis();
}
