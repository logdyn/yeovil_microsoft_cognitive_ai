package endpoints;

import java.util.logging.Level;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.json.JSONObject;

import servlets.ObservableServerClass;
import servlets.WebcamServlet;

public class LoggingEndpoint extends Endpoint implements Observer
{
	private Session session;
	private String sessionId;
	
	@Override
	public void onOpen(Session session, EndpointConfig config) 
	{
		this.session = session;
		session.addMessageHandler(new MessageHandler.Whole<String>() 
		{
			@Override
			public void onMessage(String text)
			{
				sessionId = text;
				WebcamServlet.servletInstances.get(sessionId).addObserver(LoggingEndpoint.this);
				//ExampleListenerEndpoint.endpointInstances.get(sessionId).addObserver(LoggingEndpoint.this);
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
		WebcamServlet.servletInstances.get(this.sessionId).deleteObserver(this);
	}
	
	public void log(Level level, String message)
	{
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("level", level.getName());
		jsonMessage.put("message", message);
		this.session.getAsyncRemote().sendText(jsonMessage.toString());
	}

	@Override
	public void update(ObservableServerClass serverClass, Object object) 
	{
		if(serverClass instanceof WebcamServlet && object instanceof byte[])
		{
			this.log(Level.INFO, "Sending image data to Vision Service");
		}
		else if (serverClass instanceof ExampleListenerEndpoint && object instanceof String)
		{
			this.log(Level.INFO, (String) object);
		}
	}
}