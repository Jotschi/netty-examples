package de.jotschi.example.netty.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.core.ResourceConfig;

import de.jotschi.example.netty.handler.NettyHandlerContainer;
import de.jotschi.example.netty.rest.JerseyConfiguration;

public class HttpServerPipelineInitializer extends ChannelInitializer<SocketChannel> {

	private NettyHandlerContainer jerseyHandler;

	public HttpServerPipelineInitializer() {
		this.jerseyHandler = getJerseyHandler();
	}

	private NettyHandlerContainer getJerseyHandler() {
		ResourceConfig rcf = new JerseyConfiguration();
		return ContainerFactory.createContainer(NettyHandlerContainer.class, rcf);
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("codec", new HttpServerCodec());
		p.addLast("jerseyHandler", jerseyHandler);
	}
}
