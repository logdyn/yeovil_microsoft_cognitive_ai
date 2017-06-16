package endpoints;

import java.util.Collections;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import com.logdyn.api.endpoints.LoggingEndpointConfig;

public class WebsocketConfig implements ServerApplicationConfig
{

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(final Set<Class<? extends Endpoint>> set)
	{
		return Collections.singleton(new LoggingEndpointConfig("/LoggingEndpoint"));
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(final Set<Class<?>> set)
	{
		return Collections.emptySet();
	}
}