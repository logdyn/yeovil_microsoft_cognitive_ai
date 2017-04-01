package endpoints;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import servlets.ObservableServerClass;
import servlets.WebcamServlet;

public class ExampleListenerEndpoint extends Endpoint implements Observer, ObservableServerClass
{
	public static Map<String, ExampleListenerEndpoint> endpointInstances;
	private Session session;
	private String sessionId;
	private final Set<Observer> observers = new HashSet<>();
	
	@Override
	public void onOpen(final Session session, final EndpointConfig ec)
	{
		this.session = session;
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(final String text)
			{
				sessionId = text;
				WebcamServlet.servletInstances.get(text).addObserver(ExampleListenerEndpoint.this);
			}
		});
		
		this.notifyObservers("Added example endpoint as observer");
	}

	/**
     * Event that is triggered when a session has closed.
     *
     * @param session       The session
     * @param closeReason   Why the session was closed
     */	
	@Override
	public void onClose(final Session session, final CloseReason closeReason)
	{
		this.notifyObservers("Closed endpoint");
		WebcamServlet.servletInstances.get(this.sessionId).deleteObserver(this);
	}

	@Override
	public void update(final ObservableServerClass servlet, final Object image)
	{
		//send image to ms
		if (this.session.isOpen())
		{
			this.notifyObservers("Example endpoint to MS");
			this.notifyObservers("Successful update()");
		}
		else
		{
			this.notifyObservers("Attempted to use closed session");
		}
	}

	@Override
	public void addObserver(final Observer observer)
	{
		this.observers.add(observer);
	}

	@Override
	public void deleteObserver(final Observer observer)
	{
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers(final Object object)
	{
		for (final Observer observer : this.observers)
		{
			observer.update(this, object);
		}
	}
}
