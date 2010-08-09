package com.github.bcap.resthornetq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.log4j.Logger;

public abstract class RESTObject {
	
	private static final Logger logger = Logger.getLogger(RESTObject.class);
	
	private HttpClient httpClient;
	
	private Map<String, String> headers;
	
	private String name;
	private String baseURL;
	
	protected RESTObject(String name, String baseURL) {
		this.setName(name);
		this.setBaseURL(baseURL);
		this.headers = new HashMap<String, String>();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	protected void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getBaseURL() {
		return baseURL;
	}
	
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	protected HttpResult execute(String action, String resource) {
		return this.execute(action, resource, null);
	}
	
	protected HttpResult execute(String action, String resource, String data) {

		HttpMethod method = null;
		
		String resourceURL = this.getResourceURL(resource);
		
		if(action.equalsIgnoreCase("post"))
			method = new PostMethod(resourceURL);
		else if (action.equalsIgnoreCase("put"))
			method = new PutMethod(resourceURL);
		else if (action.equalsIgnoreCase("get"))
			method = new GetMethod(resourceURL);
		else if (action.equalsIgnoreCase("delete"))
			method = new DeleteMethod(resourceURL);
		else if (action.equalsIgnoreCase("head"))
			method = new HeadMethod(resourceURL);
		
		if(data != null && method instanceof EntityEnclosingMethod) {
			EntityEnclosingMethod writeMethod = (EntityEnclosingMethod) method;
			writeMethod.setRequestEntity(new ByteArrayRequestEntity(data.getBytes()));
			writeMethod.setRequestHeader("Content-Type", "text/plain");
		}
		
		return this.execute(method);
	}

	protected String getResourceURL(String resource) {
		if(!headers.containsKey(resource))
			this.execute(new HeadMethod(getBaseURL()));
		return headers.get(resource);
	}
	
	protected HttpResult execute(HttpMethod method) {
		if(httpClient == null)
			httpClient = new HttpClient();
		
		try {
			
			logger.debug("executing " + method.getName() + " " + method.getURI().toString());
			
			int resultCode = httpClient.executeMethod(method);
			
			logger.debug(method.getName() + " on " + method.getURI().toString() + " returned status code " + resultCode);
			
			HttpResult result = new HttpResult(method);
			
			this.headers.putAll(result.getHeaders());
			
			return result;
		
		} catch (IOException e) {
			try {
				throw new RuntimeException("IOException occured while trying to invoke a " + method.getClass().getSimpleName() + " on URI \"" + method.getURI() + "\"", e);
			} catch (URIException e2) {
				throw new RuntimeException("IOException occured while trying to invoke a " + method.getClass().getSimpleName() + " (could not get URI as an URIException occured)", e);
			}
		} finally {
			method.releaseConnection();
		}
	}
}
