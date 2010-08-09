package com.github.bcap.resthornetq;

public class PullConsumer extends RESTObject {

	private static int instance = 0;
	
	private Queue queue;
	
	protected PullConsumer(Queue queue, String baseURL) {
		super(queue.getName() + "-PullConsumer" + instance++, baseURL);
	}

	protected void setQueue(Queue queue) {
		this.queue = queue;
	}

	public Queue getQueue() {
		return queue;
	}
	
	public String consumeNextMessage() {
		HttpResult result = super.execute("POST", "msg-consume-next");
		return new String(result.getData());
	}
	
	public void shutdown() {
		super.execute("DELETE", "msg-consumer");
	}
	
}
