/**
 * 
 */
package tool.facebook.constant;

/**
 * @author Ddt
 *
 */
public class EndpointConstants {
	public static final String API_GET_LONG_LIVED_USER_ACCESS_TOKEN = "https://graph.facebook.com/v18.0/oauth/access_token?grant_type=fb_exchange_token&client_id={app-id}&client_secret={app-secret}&fb_exchange_token={your-access-token}";
	public static final String API_GET_LONG_LIVED_PAGE_ACCESS_TOKEN = "https://graph.facebook.com/v18.0/me/accounts?access_token={long-lived-user-access-token}";
	public static final String API_INIT_UPLOAD_SESSION = "https://graph.facebook.com/v18.0/{your_page_id}/video_reels?upload_phase=start&access_token={long-lived-page-access-token}";
	public static final String API_UPLOAD_VIDEO = "https://rupload.facebook.com/video-upload/v18.0/{video-id}";
	public static final String API_PUSHLISH_VIDEO_REELS = "https://graph.facebook.com/v18.0/{page-id}/video_reels?access_token={page-access-token}&video_id={video-id}&upload_phase=finish&video_state=PUBLISHED&description={description}";
}
