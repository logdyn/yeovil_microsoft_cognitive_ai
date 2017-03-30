package endpoints;

import servlets.ObservableServlet;

public interface Observer
{
	public void update(final ObservableServlet servlet, final Object image);
}
