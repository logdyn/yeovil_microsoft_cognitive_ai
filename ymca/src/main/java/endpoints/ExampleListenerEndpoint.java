package endpoints;

import java.util.Map;
import java.util.logging.Level;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import servlets.ObservableServerClass;
import servlets.WebcamServlet;

public class ExampleListenerEndpoint extends Endpoint implements Observer
{
	public static Map<String, ExampleListenerEndpoint> endpointInstances;
	private Session session;
	private String sessionId;

	@Override
	public void onOpen(final Session session, final EndpointConfig ec)
	{
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(final String text)
			{
				sessionId = text;
				WebcamServlet.servletInstances.get(text).addObserver(ExampleListenerEndpoint.this);
			}
		});

		LoggingEndpoint.log(session.getId(), Level.FINE, "Added example endpoint as observer");
	}

	/**
	 * Event that is triggered when a session has closed.
	 *
	 * @param session
	 *            The session
	 * @param closeReason
	 *            Why the session was closed
	 */
	@Override
	public void onClose(final Session session, final CloseReason closeReason)
	{
		LoggingEndpoint.log(session.getId(), Level.FINE, "Closed endpoint");
		WebcamServlet.servletInstances.get(this.sessionId).deleteObserver(this);
	}

	@Override
	public void update(final ObservableServerClass servlet, final Object image)
	{
		// send image to ms
		if (this.session.isOpen())
		{
			LoggingEndpoint.log(session.getId(), Level.FINE, "Example endpoint to MS");
			LoggingEndpoint.log(session.getId(), Level.FINE, "Successful update()");
		}
		else
		{
			LoggingEndpoint.log(Level.WARNING, "Attempted to use closed session");
		}
	}
}
