package com.nj2;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType; 

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@Path("/message")
public class Service {

	private static final String QUEUE_NAME = "DOCCONVERTER";

    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response addMsgToQueue( String msg )
    {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("hrabbitmq");
		try ( Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			System.out.println(" [x] Sent '" + msg + "'");
		} catch ( Exception e ) {
			e.printStackTrace();
		}

        return Response.status(200).entity("{\"status\" : \"OK\"}").build();
    }
}

