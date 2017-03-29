package time;

import java.util.Calendar;

public class TimeFactory {
	
	private static Timeable timeSource = TimeNow.getInstance();
	
	private TimeFactory()
	{
		
	}
	
	public static long currentTimeMillis()
	{
		return TimeFactory.timeSource.currentTimeMillis();
	}
	
	public static long timeMonthsAgo(final int numberOfMonths)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(TimeFactory.timeSource.currentTimeMillis());
		cal.add(Calendar.MONTH, -numberOfMonths);
		return cal.getTimeInMillis();
	}
	
	public static long timeMinutesAgo(final int numberOfMinutes)
	{
		return TimeFactory.timeSource.currentTimeMillis() - (60000 * numberOfMinutes);
	}
}
