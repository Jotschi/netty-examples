package de.jotschi.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseWriter;

public class JerseyContainerWriter implements ContainerResponseWriter {
	private Channel channel;
	private DefaultFullHttpResponse response;

	public JerseyContainerWriter(Channel channel) {
		this.channel = channel;
	}

	public OutputStream writeStatusAndHeaders(long contentLength, ContainerResponse cResponse) throws IOException {

		ByteBuf buffer = Unpooled.buffer();
		response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(cResponse.getStatus()), buffer);

		for (Map.Entry<String, List<Object>> e : cResponse.getHttpHeaders().entrySet()) {
			List<String> values = new ArrayList<String>();
			for (Object v : e.getValue())
				values.add(ContainerResponse.getHeaderValue(v));
			response.headers().set(e.getKey(), values);
		}
		return new ByteBufOutputStream(buffer);
	}

	public void finish() throws IOException {
		channel.write(response).addListener(ChannelFutureListener.CLOSE);
	}
}
