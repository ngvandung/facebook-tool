/**
 * 
 */
package tool.facebook.utils;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tool.facebook.config.HttpClientConfig;

/**
 * @author Ddt
 *
 */
public class Utils {

	public static JsonObject callAPI(HttpRequest request) throws IOException, InterruptedException {
		HttpResponse<String> response = HttpClientConfig.getInstance().send(request,
				HttpResponse.BodyHandlers.ofString());
		JsonObject result = JsonParser.parseString(response.body()).getAsJsonObject();
		return result;
	}
}
