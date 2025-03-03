package com.nj2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Service {

	private static final String QUEUE_NAME = "DOCCONVERTER";
	public static void main(String[] args) {
		ConnectionFactory factory = null;
		Connection connection = null;
		Channel channel = null;
		try {
			factory = new ConnectionFactory();
			factory.setHost("hrabbitmq");
			
			int con_count = 1;
			while( con_count < 50 ) {
				try {
					Thread.sleep(2000);
					connection = factory.newConnection();
					channel = connection.createChannel();
				} catch( Exception e ) {
					con_count += 1;
					continue;
				}
				break;
			}

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				try {
					Thread.sleep(10000);
				} catch( Exception e ) {
				}
			};
			channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

}


