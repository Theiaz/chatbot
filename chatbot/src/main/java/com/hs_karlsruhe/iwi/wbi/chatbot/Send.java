package com.hs_karlsruhe.iwi.wbi.chatbot;

import java.io.IOException;

import com.rabbitmq.client.Channel;

public class Send {

	Channel channel;

	public Send(Channel channel) {
		this.channel = channel;
	}

	public void sendMessage(String message) {
		try {
			channel.basicPublish("", Main.getQueueName(), null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[x] Sent '" + message + "'");
	}

}
