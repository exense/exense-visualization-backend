package ch.exense.viz.proxy;

import java.util.Map;

public class ProxiedRequest {
	private String url;
	private String method;
	private String data;
	private Map<String,String> headers;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Map<String,String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String,String> headers) {
		this.headers = headers;
	}
}
