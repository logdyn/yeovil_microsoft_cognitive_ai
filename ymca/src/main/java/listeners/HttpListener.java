package listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import endpoints.LoggingEndpoint;

/**
 * Application Lifecycle Listener implementation class HttpListener
 *
 */
public class HttpListener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public HttpListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    @Override
	public void sessionCreated(final HttpSessionEvent se)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    @Override
	public void sessionDestroyed(final HttpSessionEvent se)  { 
         LoggingEndpoint.clearSession(se.getSession().getId());
    }
	
}
