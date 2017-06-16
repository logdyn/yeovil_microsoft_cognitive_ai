package endpoints;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import com.logdyn.api.endpoints.LoggingEndpoint;
import com.logdyn.api.endpoints.LoggingEndpointConfigurator;

public class WebsocketConfig implements ServerApplicationConfig
{

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(final Set<Class<? extends Endpoint>> set)
	{
		return new HashSet<ServerEndpointConfig>()
		{
			private static final long serialVersionUID = 1L;

			{
				this.add(ServerEndpointConfig.Builder.create(LoggingEndpoint.class, "/LoggingEndpoint").configurator(new LoggingEndpointConfigurator()).build());
			}
		};
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(final Set<Class<?>> set)
	{
		return Collections.emptySet();
	}
}