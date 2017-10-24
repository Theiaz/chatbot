package com.hs_karlsruhe.iwi.wbi.chatbot;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;

public class Sender {

	private static Logger logger = LoggerFactory.getLogger(Sender.class);

	private Channel channel;

	public Sender(Channel channel) {
		this.channel = channel;
	}

	public void sendMessage(String message) {
		try {
			channel.basicPublish("", Main.getQueueName(), null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("[x] Sent '" + message + "'");
//		test123
	}

}
