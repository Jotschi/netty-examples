package de.jotschi.example.netty.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/helloworld")
public class HelloWorldResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public DummyResponse getClichedMessage() {
		DummyResponse response = new DummyResponse();
		response.setMessage("Hello World");
		return response;
	}
}