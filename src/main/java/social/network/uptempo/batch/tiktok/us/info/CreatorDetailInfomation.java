package social.network.uptempo.batch.tiktok.us.info;


import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.MediaType;

import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.HTTPSClientConnection;

public class CreatorDetailInfomation {

	
	String start_text = "<script id=\"__UNIVERSAL_DATA_FOR_REHYDRATION__\" type=\"application/json\">";
	String end_text = "</script>";
	
	
	public EData creatorInfomation(EData data) throws Exception {
		
		HTTPSClientConnection hcc = new HTTPSClientConnection();
		
//		(String fullNotiURL, String contentType, String cookie)
		hcc.connectGet(data.getString("creator_link"), MediaType.TEXT_HTML_VALUE, null);
		String readResult = hcc.readResult();
		hcc.close();
		
		if(readResult == null || "".equals(readResult)) {
			throw new Exception();
		}
		
		int startIndex = readResult.indexOf(start_text);
		int endIndex = readResult.indexOf(end_text, startIndex);
		
		String extractData = readResult.substring(startIndex + start_text.length(), endIndex);
		
		JSONObject jo = new JSONObject(extractData);
		JSONObject defaultScope = jo.getJSONObject("__DEFAULT_SCOPE__");
		JSONObject webappUserDetail = defaultScope.getJSONObject("webapp.user-detail");

		JSONObject userInfo = webappUserDetail.getJSONObject("userInfo");
		JSONObject user = userInfo.getJSONObject("user");
		
		
		EData reData = new EData();
		reData.put("uniqueId", user.getString("uniqueId"));
		reData.put("nickname", user.getString("nickname"));
		reData.put("signature", user.getString("signature"));
		reData.put("secUid", user.getString("secUid"));
		reData.put("region", user.getString("region"));
		reData.put("language", user.getString("language"));
		reData.put("followerCount", user.getString("followerCount"));
		reData.put("followingCount", user.getString("followingCount"));
		reData.put("videoCount", user.getString("videoCount"));
		
			
		// 하위 정보를 가지고 오는 로직 만들기
		EData reData2 = subCreatorInfo(reData);
		reData.put("createTime", reData2.getString("createTime"));
		reData.put("avgPlayCount", reData2.getString("avgPlayCount"));
		reData.put("avgDiggCount", reData2.getString("avgDiggCount"));
		
		
		
		return reData;
	}
	
	
	String userUrl = "https://m.tiktok.com/api/post/item_list/?aid=1988&app_name=tiktok_web&device_platform=web&referer=https://www.google.com/&root_referer=https://www.google.com/&user_agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36&cookie_enabled=true&screen_width=1920&screen_height=1080&browser_language=ko-KR&browser_platform=MacIntel&browser_name=Mozilla&browser_version=5.0%20%28Windows%20NT%2010.0%3B%20Win64%3B%20x64%29%20AppleWebKit%2F537.36%20%28KHTML%2C%20like%20Gecko%29%20Chrome%2F131.0.0.0%20Safari%2F537.36&browser_online=true&ac=4g&timezone_name=Asia/seoul&page_referer=https://www.tiktok.com/search/q=beauty%26lang=ko-KR&priority_region=&verifyFp=verify_kkqg77dw_hai6t3Ps_dkIT_41yF_ApMY_G7euFo5nq2xH&appId=1180&region=KR&appType=t&isAndroid=false&isMobile=false&isIOS=false&OS=windows&did=6925283638187689473&count=30&cursor=0&language=ko-KR&secUid=";

	private EData subCreatorInfo(EData data) {
		
		
		
		
		EData reData = new EData();
		return reData;
	}
	
}
