package com.hs_karlsruhe.iwi.wbi.chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

	private final static String QUEUE_NAME = "hello";
	private Connection connection;
	private static Channel channel;
	private static Send sender;
	private static Recv rec;

	public static void main(String[] args) throws Exception {
		Main main = new Main();

		try {
			main.openConnection();

			sender = new Send(channel);
			rec = new Recv(channel);
			rec.init();

			main.listenInput();
		} finally {
			main.closeConnection();
		}
	}

	private void openConnection() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	}

	private void closeConnection() throws Exception {
		channel.close();
		connection.close();
	}

	private void listenInput() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line = "";

		while (true) {
			line = in.readLine();
			if (line.equalsIgnoreCase("quit")) {
				break; // quit console
			}
			sender.sendMessage(line);
		}

		in.close();
	}

	public static String getQueueName() {
		return QUEUE_NAME;
	}
}
