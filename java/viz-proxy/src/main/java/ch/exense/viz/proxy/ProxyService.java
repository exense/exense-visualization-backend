package ch.exense.viz.proxy;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProxyService {

	private static CloseableHttpClient httpclient = HttpClients.createDefault();

	public ProxiedResponse executeProxiedQuery(ProxiedRequest proxiedRequest) {
		HttpRequestBase httpRequest = null;

		CloseableHttpResponse executed = null;
		HttpEntity entity = null;

		try {
			httpRequest = buildRequest(proxiedRequest);	
			executed = httpclient.execute(httpRequest);
			return toProxiedResponse(executed.getEntity(), executed.getStatusLine().getStatusCode());
		} catch (Exception e) {
			ProxiedResponse errorResponse = new ProxiedResponse();
			errorResponse.setCode(500);
			errorResponse.setData(e.getClass() + ":" + e.getMessage());
			e.printStackTrace();
			return errorResponse;
		} finally {
			if(executed != null) {
				try {
					EntityUtils.consume(entity);
					executed.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private HttpRequestBase buildRequest(ProxiedRequest proxiedRequest) throws Exception {

		final HttpRequestBase httpRequest = initializeRequest(proxiedRequest);

		if(proxiedRequest.getHeaders() != null){
			proxiedRequest.getHeaders().entrySet().stream().forEach(e -> {
				httpRequest.addHeader(e.getKey(), e.getValue());
			});
		}
		return httpRequest;
	}

	private HttpRequestBase initializeRequest(ProxiedRequest proxiedRequest) throws Exception {
		String url = proxiedRequest.getUrl();
		String method = proxiedRequest.getMethod();

		if(url == null || method == null) {
			throw new Exception("Incomplete proxied query: url=" + url + "; method="+ method);
		}

		HttpRequestBase httpRequest = null;

		switch(method.trim().toLowerCase()) {
		case "get":
			httpRequest = new HttpGet(url);
			break;
		case "post":
			HttpPost post = new HttpPost(url);
			post.setEntity(new StringEntity(proxiedRequest.getData()));
			httpRequest = post;
			break;
		case "delete":
			httpRequest = new HttpDelete(url);
			break;
		case "put":
			httpRequest = new HttpPut(url);
			break;
		case "patch":
			httpRequest = new HttpPatch(url);
			break;
		default:
			throw new Exception("Unknown method type: " + method);
		}
		return httpRequest;
	}

	private ProxiedResponse toProxiedResponse(HttpEntity entity, int code) throws ParseException, IOException {
		ProxiedResponse response = new ProxiedResponse();
		response.setCode(code);
		if(entity != null) {
			response.setData(EntityUtils.toString(entity));
		}
		return response;
	}
}
