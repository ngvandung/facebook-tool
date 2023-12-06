/**
 * 
 */
package tool.facebook.config;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * @author Ddt
 *
 */
public class HttpClientConfig {

	private HttpClient httpClient;
	private static final HttpClientConfig instance = new HttpClientConfig();

	private HttpClientConfig() {
		httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(10))
				.build();
	}

	public static HttpClient getInstance() {
		return instance.httpClient;
	}
}
