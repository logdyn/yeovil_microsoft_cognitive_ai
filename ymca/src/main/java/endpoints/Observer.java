package endpoints;

import servlets.ObservableServerClass;

public interface Observer
{
	public void update(final ObservableServerClass servlet, final Object image);
}
