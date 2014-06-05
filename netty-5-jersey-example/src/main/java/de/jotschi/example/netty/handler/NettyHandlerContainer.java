package de.jotschi.example.netty.handler;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import java.net.URI;
import java.util.Map.Entry;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.WebApplication;

@Sharable
public class NettyHandlerContainer extends ChannelHandlerAdapter {
	public static final String PROPERTY_BASE_URI = "com.sun.jersey.server.impl.container.netty.baseUri";

	private WebApplication application;
	private String baseUri;

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	public NettyHandlerContainer(WebApplication application, ResourceConfig resourceConfig) {
		this.application = application;
		this.baseUri = (String) resourceConfig.getProperty(PROPERTY_BASE_URI);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof HttpRequest) {

			HttpRequest req = (HttpRequest) msg;

			if (is100ContinueExpected(req)) {
				ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
			}
//			boolean keepAlive = isKeepAlive(req);
//			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
			String base = getBaseUri(req);
			final URI baseUri = new URI(base);
			final URI requestUri = new URI(base.substring(0, base.length() - 1) + req.getUri());

//			if (!keepAlive) {
				final ContainerRequest cRequest = new ContainerRequest(application, req.getMethod().name(), baseUri, requestUri, getHeaders(req), null);
				application.handleRequest(cRequest, new JerseyContainerWriter(ctx.channel()));
//			} else {
//				response.headers().set(CONNECTION, Values.KEEP_ALIVE);
//				ctx.write(response);
//			}

		}
	}

	private String getBaseUri(HttpRequest request) {
		if (baseUri != null) {
			return baseUri;
		}

		return "http://" + request.headers().get(HttpHeaders.Names.HOST) + "/";
	}

	private InBoundHeaders getHeaders(HttpRequest request) {
		InBoundHeaders headers = new InBoundHeaders();

		for (Entry<String, String> entry : request.headers().entries()) {
			headers.putSingle(entry.getKey(), entry.getValue());
		}

		return headers;
	}
}