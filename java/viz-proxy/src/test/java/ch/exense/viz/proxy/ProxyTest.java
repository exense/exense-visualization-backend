package ch.exense.viz.proxy;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class ProxyTest {
	
	@BeforeClass
	public static void beforeClass() {
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
	}
	
	@Test
	public void basicGetTest() throws IOException {
		ProxiedRequest request = new ProxiedRequest();
		request.setMethod("GET");
		request.setUrl("https://wikimedia.org/api/rest_v1/metrics/pageviews/top/en.wikisource/all-access/2015/10/all-days");
		ProxiedResponse response = new ProxyService().executeProxiedQuery(request);
		Assert.assertEquals(200, response.getCode());
		Assert.assertNotNull(response.getData());
		Assert.assertEquals(true, response.getData().contains("items"));
	}
	
	@Test
	public void basicPostTest() throws IOException {
		ProxiedRequest request = new ProxiedRequest();
		request.setMethod("Post");
		request.setUrl("https://postman-echo.com/post");
		request.setData("{ \"json\" : \"stuff\"}");
		ProxiedResponse response = new ProxyService().executeProxiedQuery(request);
		Assert.assertEquals(200, response.getCode());
		Assert.assertNotNull(response.getData());
		Assert.assertEquals(true, response.getData().contains("stuff"));
	}

}
