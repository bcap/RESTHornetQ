
package com.github.bcap.resthornetq;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;

public class HttpResult {
	private byte[] data;
	private Map<String, String> headers;
	private Integer resultCode;

	private String url;
	private String action;
	
	public HttpResult(){}
	
	public HttpResult(HttpMethod method) {
		try {
			this.setResultCode(method.getStatusCode());
			this.setHeaders(method.getResponseHeaders());
			this.setAction(method.getName());
			this.setData(method.getResponseBody());
			this.setUrl(method.getURI().toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setData(InputStream stream) throws IOException {
		DataInputStream dataInputStream = new DataInputStream(stream);
		this.data = new byte[stream.available()];
		dataInputStream.readFully(this.data);
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setHeaders(Header[] headers) {
		this.headers = new HashMap<String, String>();
		for (int i = 0; i < headers.length; i++)
			this.headers.put(headers[i].getName(), headers[i].getValue());
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public Integer getResultCode() {
		return resultCode;
	}
}
