package com.hs_karlsruhe.iwi.wbi.chatbot;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

public class Send {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws java.io.IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost"); //change to IP
		Connection connection = null;
		try {
			connection = factory.newConnection();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		
		try {
			channel.close();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection.close();
	}

}
