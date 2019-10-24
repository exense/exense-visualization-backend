package ch.exense.viz.proxy;

import java.io.IOException;

import org.junit.AfterClass;
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
	public void basicUrlTest() throws IOException {
		ProxiedRequest request = new ProxiedRequest();
		request.setMethod("GET");
		request.setUrl("https://wikimedia.org/api/rest_v1/metrics/pageviews/top/en.wikisource/all-access/2015/10/all-days");
		ProxiedResponse response = new ProxyService().executeProxiedQuery(request);
		System.out.println(response.getCode() + ": "+ response.getData());
	}

}
