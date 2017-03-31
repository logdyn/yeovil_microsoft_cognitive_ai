package endpoints;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import servlets.ObservableServlet;
import servlets.WebcamServlet;

public class ExampleListenerEndpoint extends Endpoint implements Observer
{
	private Session session;
	private String sessionId;
	
	private static Logger LOGGER = Logger.getLogger(ExampleListenerEndpoint.class.getName());
	@Override
	public void onOpen(final Session session, final EndpointConfig ec)
	{
		this.session = session;
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(final String text)
			{
				LOGGER.log(Level.FINE, "Added example endpoint as observer");
				sessionId = text;
				WebcamServlet.servletInstances.get(text).addObserver(ExampleListenerEndpoint.this);
			}
		});
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
		LOGGER.log(Level.FINE, "Closed endpoint");
		WebcamServlet.servletInstances.get(this.sessionId).deleteObserver(this);
	}

	@Override
	public void update(final ObservableServlet servlet, final Object image)
	{
		//send image to ms
		if (this.session.isOpen())
		{
			this.session.getAsyncRemote().sendText("Example endpoint to MS");
			LOGGER.log(Level.FINE, "Successful update()");
		}
		else
		{
			LOGGER.log(Level.WARNING, "Attempted to use closed session");
		}
	}
}
