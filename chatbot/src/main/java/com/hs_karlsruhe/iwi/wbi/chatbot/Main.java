package com.hs_karlsruhe.iwi.wbi.chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);
	private final static String QUEUE_NAME = "hello";

	private Connection connection;
	private Channel channel;
	private Sender sender;
	private Receiver rec;

	public static void main(String[] args) throws Exception {
		Main main = new Main();

		try {
			main.openConnection();
			main.init();
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

	private void init() {
		sender = new Sender(channel);
		// change for other Agent on Api.ai
		String accessToken = "53949f90a9e44175bc3dbf78da2ae085";
		rec = new Receiver(channel, accessToken);
	}

	private void listenInput() throws IOException {
		System.out.print("Me \t> ");
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
