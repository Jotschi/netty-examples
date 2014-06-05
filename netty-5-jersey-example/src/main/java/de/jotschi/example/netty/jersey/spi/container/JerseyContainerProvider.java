package de.jotschi.example.netty.jersey.spi.container;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.ContainerProvider;
import com.sun.jersey.spi.container.WebApplication;

import de.jotschi.example.netty.handler.NettyHandlerContainer;

public class JerseyContainerProvider implements ContainerProvider<NettyHandlerContainer> {
	public NettyHandlerContainer createContainer(Class<NettyHandlerContainer> type, ResourceConfig config, WebApplication application)
			throws ContainerException {
		if (type != NettyHandlerContainer.class) {
			return null;
		}
		return new NettyHandlerContainer(application, config);
	}
}