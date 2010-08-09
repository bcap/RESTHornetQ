package com.github.bcap.resthornetq;

import com.github.bcap.resthornetq.util.TemplateString;

public class Queue extends RESTObject {

	private static final TemplateString URL_TEMPLATE = new TemplateString("http://${0}/queues/jms.queue.${1}"); 
	
	public Queue(String host, String name) {
		super(name, URL_TEMPLATE.apply(host, name));
	}
	
	public void sendMessage(String message) {
		this.sendMessage(message, false);
	}
	
	public void sendMessage(String message, boolean durable) {
		super.execute("POST", "msg-create", message);
	}
	
	public PullConsumer createPullConsumer() {
		super.execute("POST", "msg-pull-consumers");
		return new PullConsumer(this, super.getResourceURL("Location"));
	}
}
