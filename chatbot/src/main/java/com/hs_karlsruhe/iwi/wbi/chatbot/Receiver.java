package com.hs_karlsruhe.iwi.wbi.chatbot;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class Receiver {

	private static Logger logger = LoggerFactory.getLogger(Receiver.class);

	private Channel channel;
	private String message;
	private String accessToken;
	// API.ai
	private AIConfiguration configuration;
	private AIDataService dataService;

	public Receiver(Channel channel, String accessToken) {
		this.channel = channel;
		this.accessToken = accessToken;
		init();
		initApiAI();
	}

	private void init() {
		System.out.println("[*] Waiting for messages. To exit type 'quit'");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				message = new String(body, "UTF-8");
				logger.debug(LocalDateTime.now().toString() + ": [x] Received '" + message + "'");
				sendRequestToApiAI();
			}
		};

		try {
			channel.basicConsume(Main.getQueueName(), true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initApiAI() {
		configuration = new AIConfiguration(accessToken);
		dataService = new AIDataService(configuration);
	}

	private void sendRequestToApiAI() {
		try {
			AIRequest request = new AIRequest(message);
			AIResponse response = dataService.request(request);

			if (response.getStatus().getCode() == 200) {
				System.out.println("Bot \t> " + response.getResult().getFulfillment().getSpeech());
				System.out.print("Me \t> ");
			} else {
				System.err.println("Bot:" + response.getStatus().getErrorDetails());
				logger.error(response.getStatus().getErrorDetails());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}