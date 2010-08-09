package com.github.bcap.resthornetq;

import org.apache.log4j.Logger;

public class RESTHornetQTest implements Runnable {

	private static final Logger logger = Logger.getLogger(RESTHornetQTest.class);
	
	public void run() {
		try {
			
			Queue queue = new Queue("pig:8080/resthornetq", "test");

			String messages = "lol, banana, helicoptero";
			String[] messageArray = messages.split(", *");
			
			logger.debug("creating consumer");
			PullConsumer consumer = queue.createPullConsumer();
			logger.info("consumer " + consumer.getName() + " created");
			
			logger.debug("sending messages: " + messages);
			for (int i = 0; i < messageArray.length; i++)
				queue.sendMessage(messageArray[i]);
			logger.info("sent messages: " + messages);
			
			for (int i = 0; i < messageArray.length; i++) {
				logger.debug("consuming next message");
				String consumedMessage = consumer.consumeNextMessage();
				logger.info("consumed message: " + consumedMessage);
			}
			
			logger.debug("shutting down consumer");
			consumer.shutdown();
			logger.info("consumer shutted down");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new RESTHornetQTest().run();
	}
}
