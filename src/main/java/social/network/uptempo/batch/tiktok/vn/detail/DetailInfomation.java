package social.network.uptempo.batch.tiktok.vn.detail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;

import social.network.uptempo.batch.context.ApplicationContextProvider;
import social.network.uptempo.batch.mapper.CodeMapper;
import social.network.uptempo.batch.mapper.CreatorMapper;
import social.network.uptempo.batch.mapper.ScheduleInfoMapper;
import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.HTTPSClientConnection;

public class DetailInfomation {

	private ApplicationContext applicationContext;
	private ScheduleInfoMapper scheduleInfoMapper;
	private CreatorMapper creatorMapper;
	private CodeMapper codeMapper;
	
	private String PRE_URL = "https://www.tiktok.com/@";
	private String start_text = "<script id=\"__UNIVERSAL_DATA_FOR_REHYDRATION__\" type=\"application/json\">";
	private String end_text = "</script>";
	
	public DetailInfomation() {
		applicationContext = ApplicationContextProvider.getApplicationContext();
		
		scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
		creatorMapper = (CreatorMapper) applicationContext.getBean("creatorMapper");
		codeMapper = (CodeMapper) applicationContext.getBean("codeMapper");
	}
	
	
//	public void searchCreator() {
//		List list = creatorMapper.getCreator();
//		
//		for(EData data : list) {
//			HTTPSClientConnection hcc = new HTTPSClientConnection();
//			hcc.connectGet(PRE_URL + data.getString(""), HttpMethod.GET, "");
//			String readResult = hcc.readResult();
//			hcc.close();
//			
//			
//			int textData_start_index = readResult.indexOf(start_text);
//			int textData_end_index = readResult.indexOf(end_text, textData_start_index);
//			String extractData = readResult.substring(textData_start_index, textData_end_index);
//			JSONObject extractData_json = new JSONObject(extractData);
//			
//			JSONObject defaultScope = extractData_json.getJSONObject("__DEFAULT_SCOPE__");
//			JSONObject webappUserDetail = defaultScope.getJSONObject("webapp.user-detail");
//			        
//			JSONObject userInfo = webappUserDetail.getJSONObject("userInfo");
//			JSONObject user = userInfo.getJSONObject("user");
//			JSONObject stats = userInfo.getJSONObject("stats");
//			
//			
//			EData addData = new EData();
//			addData.put("uniqueId", user.getString("uniqueId"));
//			addData.put("nickname", user.getString("nickname"));
//			addData.put("signature", user.getString("signature"));
//			addData.put("secUid", user.getString("secUid"));
//			addData.put("region", user.getString("region"));
//			addData.put("language", user.getString("language"));
//			addData.put("followerCount", user.getString("followerCount"));
//			addData.put("followingCount", user.getString("followingCount"));
//			addData.put("videoCount", user.getString("videoCount"));
//			addData.put("email", extractEmail(user.get("signature")))
//			EData subData = searchCreatorPageItemList(user.getString("secUid"));
//			addData.put("createTime", subData.getString("subData"));
//			
////	        dataAdd(re_data, id)
//		}
//		
//	}
//	
//	private EData searchCreatorPageItemList(String secUid) {
//		
//		
//		
//		EData reData = new EData();
//		return reData;
//	}
//	
//	
//	private String extractEmail(Object obj) {
//		if( obj == null || obj.equals("")) {
//			return "";
//		}
//		
//		String reStr = "";
//		
//		Pattern p = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
//        Matcher m = p.matcher(obj.toString());
//        while(m.find()) {
//        	reStr = m.group(1);
//        }
//		
//        return reStr;
//	}
	
}

