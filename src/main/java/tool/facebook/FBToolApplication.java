/**
 * 
 */
package tool.facebook;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;

import tool.facebook.constant.EndpointConstants;
import tool.facebook.utils.Utils;

/**
 * @author Ddt
 *
 */
public class FBToolApplication {
	private static final String ACCESS_TOKEN = "EAAOmOZAIjbGkBO0I4zIrfsLlyOYI95PlrabFvZBnENzSMULPmZAPOasKSsQwEoovNQ3H3XuDCzfsQaLxKao5zwbY6tbnbwdjnMO4ZCAQqNX3ZCryOr1lsbjRvvbl2Kh4mjr9mZBTtZCxx8yyBGcflmmvyLECGszYxmr21UpfIfV3BqOTKthaQYcaeNU24QNsYyt5extVGZB8p24QZAeFfzxCuZBZAxepOSTohLD6PjLj3LOUqcZD";
	private static final String APP_ID = "1027191125273705";
	private static final String APP_SECRET = "9e8819416b4cb6d9d7350403e4616258";

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		// 1. Get Long Lived User Access Token
		String userToken = getLongLivedUserAccessToken(APP_ID, APP_SECRET, ACCESS_TOKEN).get("access_token")
				.getAsString();
		// 2. Get Long Lived Page Access Token
		JsonObject pages = getOwnPages(userToken);
		String pageId = pages.get("data").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
		String pageToken = pages.get("data").getAsJsonArray().get(0).getAsJsonObject().get("access_token")
				.getAsString();
		// 3. Init Upload Video Session
		JsonObject sessionUpload = initUploadSession(pageId, pageToken);
		String videoId = sessionUpload.get("video_id").getAsString();
		String fileUrl = "https://img.ayrshare.com/012/reel.mp4";
		String filePath = "C:\\Users\\dung.nguyenvan1\\Downloads\\video-1646710811.mp4";
		// 4. Upload Video
		JsonObject uploadVideoRes = uploadVideoFromLocal(videoId, filePath, pageToken);
		if (uploadVideoRes.has("success") && uploadVideoRes.get("success").getAsBoolean()) {
			// 5. Publish Video To Reels
			JsonObject publishReelRes = publishTheReel(pageToken, pageId, videoId, "Test_Facebook_Reels_2");
			if (publishReelRes.has("success") && publishReelRes.get("success").getAsBoolean()) {
				System.out.println("==== PUBLISH REELS DONE ====");
			}
		}
	}

	public static JsonObject getLongLivedUserAccessToken(String appId, String appSecret, String accessToken)
			throws URISyntaxException, IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(EndpointConstants.API_GET_LONG_LIVED_USER_ACCESS_TOKEN.replace("{app-id}", appId)
						.replace("{app-secret}", appSecret).replace("{your-access-token}", accessToken)))
				.GET().build();
		return Utils.callAPI(request);
	}

	public static JsonObject getOwnPages(String longLivedUserAccessToken)
			throws URISyntaxException, IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(EndpointConstants.API_GET_LONG_LIVED_PAGE_ACCESS_TOKEN
						.replace("{long-lived-user-access-token}", longLivedUserAccessToken)))
				.GET().build();
		return Utils.callAPI(request);
	}

	public static JsonObject initUploadSession(String pageId, String pageAccessToken)
			throws URISyntaxException, IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(EndpointConstants.API_INIT_UPLOAD_SESSION.replace("{your_page_id}", pageId)
						.replace("{long-lived-page-access-token}", pageAccessToken)))
				.POST(HttpRequest.BodyPublishers.noBody()).build();
		return Utils.callAPI(request);
	}

	public static JsonObject uploadVideoByURL(String videoId, String fileUrl, String pageAccessToken)
			throws URISyntaxException, IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder().header("Authorization", "OAuth " + pageAccessToken)
				.header("file_url", fileUrl)
				.uri(new URI(EndpointConstants.API_UPLOAD_VIDEO.replace("{video-id}", videoId)))
				.POST(HttpRequest.BodyPublishers.noBody()).build();
		return Utils.callAPI(request);
	}

	public static JsonObject uploadVideoFromLocal(String videoId, String filePath, String pageAccessToken)
			throws URISyntaxException, IOException, InterruptedException {
		File file = new File(filePath);
		long fileSize = Files.size(file.toPath());
		HttpRequest request = HttpRequest.newBuilder().header("Authorization", "OAuth " + pageAccessToken)
				.header("offset", "0").header("file_size", String.valueOf(fileSize))
				.header("Content-Type", "application/octet-stream")
				.uri(new URI(EndpointConstants.API_UPLOAD_VIDEO.replace("{video-id}", videoId)))
				.POST(HttpRequest.BodyPublishers.ofByteArray(Files.readAllBytes(file.toPath()))).build();
		return Utils.callAPI(request);
	}

	public static JsonObject publishTheReel(String pageAccessToken, String pageId, String videoId, String description)
			throws URISyntaxException, IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(EndpointConstants.API_PUSHLISH_VIDEO_REELS.replace("{page-id}", pageId)
						.replace("{page-access-token}", pageAccessToken).replace("{video-id}", videoId)
						.replace("{description}", description)))
				.POST(HttpRequest.BodyPublishers.noBody()).build();
		return Utils.callAPI(request);
	}
}
