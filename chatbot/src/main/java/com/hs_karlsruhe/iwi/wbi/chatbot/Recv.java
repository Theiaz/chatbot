package com.hs_karlsruhe.iwi.wbi.chatbot;

import java.io.IOException;
import java.time.LocalDateTime;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv {

	Channel channel;
	
	public Recv(Channel channel) {
		this.channel = channel;
	}

	
	public void init()
	{
		System.out.println("[*] Waiting for messages. To exit type 'quit'");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(LocalDateTime.now().toString() + "[x] Received '" + message + "'");
			}
		};

		try {
			channel.basicConsume(Main.getQueueName(), true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}