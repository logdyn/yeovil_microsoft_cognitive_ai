package endpoints;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import servlets.ObservableServlet;
import servlets.WebcamServlet;

public class ExampleListenerEndpoint extends Endpoint implements Observer
{
	private Session session;
	@Override
	public void onOpen(final Session session, final EndpointConfig ec)
	{
		this.session = session;
		session.addMessageHandler(new MessageHandler.Whole<String>()
		{
			@Override
			public void onMessage(final String text)
			{
				System.out.println("Added example endpoint as observer");
				WebcamServlet.servletInstances.get(text).addObserver(ExampleListenerEndpoint.this);
			}
		});
	}

	@Override
	public void update(final ObservableServlet servlet, final Object image)
	{
		//send image to ms
		session.getAsyncRemote().sendText("Example endpoint to MS");
	}

}
