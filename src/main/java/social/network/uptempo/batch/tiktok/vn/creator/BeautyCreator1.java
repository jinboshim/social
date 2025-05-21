package social.network.uptempo.batch.tiktok.vn.creator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import social.network.uptempo.batch.context.ApplicationContextProvider;
import social.network.uptempo.batch.mapper.CodeMapper;
import social.network.uptempo.batch.mapper.CreatorMapper;
import social.network.uptempo.batch.mapper.ScheduleInfoMapper;
import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.HTTPSClientConnection;
import social.network.uptempo.set.util.StringUtil;

//@Component
public class BeautyCreator1 {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext;
	private ScheduleInfoMapper scheduleInfoMapper;
	private CreatorMapper creatorMapper;
	private CodeMapper codeMapper;
	
	private static int creatorSleepTime = 3 * 1000; //milliseconds
	private static int categorySleepTime = 5 * 1000; //milliseconds
	
	private static int paginationSize = 20;
	
//	private static int boundPlusCount = 10;
	
	public BeautyCreator1() {
		applicationContext = ApplicationContextProvider.getApplicationContext();
		
		scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
		creatorMapper = (CreatorMapper) applicationContext.getBean("creatorMapper");
		codeMapper = (CodeMapper) applicationContext.getBean("codeMapper");
	}
	
	
	@Scheduled(initialDelay = 1000, fixedDelay=60000) // 1min delay
	public void doTask() {
		System.out.println("@@@@@@@@@@@@@ BeautyCreator1 doTask start @@@@@@@@@@@@@");
		EData scheduleInfo = new EData();
		scheduleInfo.put("id", "creatorCategoryBatchVN1");
		try {
			EData eData = scheduleInfoMapper.getInfo(scheduleInfo);
			System.out.println(eData.toString());

			// start yn = Y 일경우에만 동작한다.
			if( eData.compareString("START_YN", "Y") ) {
				
				String sessionId = eData.getString("SESSIONID");
				int startCount = eData.getInt("START_COUNT");
				int left_bound = eData.getInt("NEXT_COUNT");
				int maxCount = eData.getInt("MAX_COUNT");
				String url = eData.getString("URL");
				int right_bound = left_bound + eData.getInt("NEXT_STEP");
				
				
				JSONObject follower_filter = new JSONObject();
				follower_filter.put("left_bound", left_bound);
				follower_filter.put("right_bound", right_bound);
				
				// 카테고리를 조회 한다.
				// 조회된 카테고리를 기준으로 creator를 수집한다.
				List<EData> categoryList = codeMapper.tiktokCategoryBeautyList(eData);
				for(EData categoryInfo : categoryList) {
					List<String> stringList = new ArrayList<>();
					stringList.add(categoryInfo.getString("PARENT_ID"));
					stringList.add(categoryInfo.getString("ID"));
					
					JSONObject string_list = new JSONObject();
					string_list.put("string_list", stringList);
					
					JSONArray category_list = new JSONArray();
					category_list.put(string_list);
					
					creatorJob(sessionId, startCount, maxCount, url, follower_filter, category_list);
					
					Thread.sleep(categorySleepTime);
				}
				
				// 정해진 범위안에 creator 조회 및 저장이 완료되면 이후 범위로 변경 할수 있도록 DB를 업데이트 해야 한다.
				// 해당 업테이트의 경우 tb_schedule_info 테이블에 left_bound / right_bound의 값을 조정한다.
				if( right_bound >= maxCount ) {
					eData.put("next_count", startCount);
				}
				else {
					eData.put("next_count", right_bound);
				}
				
				scheduleInfoMapper.modifyNextCount(eData);
				
//				creatorJob(eData);
				
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		}
		 
	}

	private void creatorJob(String sessionId, int startCount, int maxCount, String url, JSONObject follower_filter, JSONArray category_list) {
		String cookie = "sessionid=" + sessionId;
		
		int page = 0;
		boolean hasMore = true;
		String searchKey = "";
		int next_item_cursor = 0;
		
		logger.info("tiktok creator start : " + StringUtil.nowTime("yyyy/MM/dd E HH:mm"));
		try {
			while(hasMore) {
				JSONObject pagination = new JSONObject();
				pagination.put("size", paginationSize);
				pagination.put("page", page);
				if(!StringUtil.bNvl(searchKey)) {
					pagination.put("search_key", searchKey);
				}
				if(next_item_cursor>0) {
					pagination.put("next_item_cursor", next_item_cursor);
				}
				
				JSONObject filter_params = new JSONObject();
				
				if(startCount >= 0 && maxCount > 0) {
					filter_params.put("follower_filter", follower_filter);
				}
				
				filter_params.put("category_list", category_list);
				
				
				JSONObject bodyData = new JSONObject();
				bodyData.put("pagination", pagination);
				bodyData.put("filter_params", filter_params);
				bodyData.put("algorithm", 1);
				logger.info("tiktok creator ing - 1 : " + page + " bodyData : " + bodyData.toString());
				
				HTTPSClientConnection hcc = new HTTPSClientConnection();
				hcc.connectPost(url, MediaType.APPLICATION_JSON_VALUE, cookie);
				hcc.sendMessage(bodyData.toString());
				String readResult = hcc.readResult();
				hcc.close();
				
				
				JSONObject resultData = new JSONObject(readResult);
				
				if(resultData == null || resultData.length() == 0) {
					logger.error("creator data is null or empty");
					throw new Exception();
				}
				if(resultData.getInt("code") > 0) {
					logger.error("creator data is error : " + resultData.toString());
					throw new Exception();
				}
				
				// creator infomation list
				JSONArray creatorArray = resultData.getJSONArray("creator_profile_list");
				for(int i=0, j=creatorArray.length(); i<j; i++) {
					// all data save is mongodb
					// 1row data = 1 insert
					JSONObject creatorInfo = creatorArray.getJSONObject(i);
					
					String id = creatorInfo.getJSONObject("creator_oecuid").getString("value");
					String name = creatorInfo.getJSONObject("handle").getString("value");
					String nick_name = creatorInfo.getJSONObject("nickname").getString("value");
					String region = creatorInfo.getJSONObject("selection_region").getString("value");
					int follower_cnt = creatorInfo.getJSONObject("follower_cnt").getInt("value");
					boolean contact = creatorInfo.getJSONObject("contact_info_available").getBoolean("value");
					
					EData creatorData = new EData();
					creatorData.put("id", id);
					creatorData.put("type_cd", 12);
					creatorData.put("name", name);
					creatorData.put("nick_name", nick_name);
					creatorData.put("creator_link", "https://www.tiktok.com/@"+name);
					creatorData.put("region_cd", 1001);
					creatorData.put("follower_cnt", follower_cnt);
					if(contact) {
						creatorData.put("contact_type", 111);
						
					}
					else {
						// creator의 연락처 정보가 존재하지 않는다.
						creatorData.put("contact_type", 999);
					}
					creatorData.put("insert_id", "schedule");
					creatorData.put("update_id", "schedule");
					
					creatorMapper.creatorAdd(creatorData);
					// Data 등록이후에 해당 인플루언서 정보를 기반으로 상세 정보를 업데이트 할수 있는 비동기 로직을 호출한다.
					// 비동기 로직에서 상세 정보를 업데이트 및 등록 할수 있어야 한다.
					// 단 이때 Transaction 처리에 대한 고민이 이루어 진다.
					// 현재는 각 동작에 대해서 Transaction 처리가 되어 있지 않는다.
					// 그런만큼 비동기 동작에서 영향이 가지 않는다고 판단된다.
//					searchCreator(creatorData);
					
					
					JSONArray categoryList = creatorInfo.getJSONObject("category").getJSONArray("value");
					if(categoryList.length() > 0) {
						creatorMapper.creatorCategoryRemove(creatorData);
					}
					for(int a=0, b=categoryList.length(); a<b; a++) {
						JSONObject categoryInfo = categoryList.getJSONObject(a);
						String[] starlingKey = categoryInfo.getString("starling_key").split("_");
						
						EData creatorCategoryData = new EData();
						creatorCategoryData.put("id", id);
						creatorCategoryData.put("category_cd", starlingKey[1]);
						creatorCategoryData.put("category_name", categoryInfo.getString("name"));
						creatorCategoryData.put("category_value", starlingKey[0]+starlingKey[1]);
						creatorCategoryData.put("insert_id", "schedule");
						
						creatorMapper.creatorCategoryAdd(creatorCategoryData);
						
					}
					
				}

				// 1Page 이후로는 호출시마다 search_key가 필수로 셋팅되어야 한다.
				// 해당값이 있어야 그걸 기준으로 페이지의 정보가 중복되지 않는다.
				// 맨처음 호출시 return 결과에 search_key 존재
//						// next page check logic
				JSONObject nextPagination = resultData.getJSONObject("next_pagination");
				hasMore = nextPagination.getBoolean("has_more");
				searchKey = nextPagination.getString("search_key");
				next_item_cursor = nextPagination.getInt("next_item_cursor");
				logger.info("tiktok creator ing - 2 : " + page + " nextPagination : " + nextPagination.toString());
				page++;
				
				Thread.sleep(creatorSleepTime);
			}
			
			logger.info("tiktok creator end : " + StringUtil.nowTime("yyyy/MM/dd E HH:mm"));
		}
		catch (Exception e) {
//			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	
	
class SearchCreator {
	private String PRE_URL = "https://www.tiktok.com/@";
	private String start_text = "<script id=\"__UNIVERSAL_DATA_FOR_REHYDRATION__\" type=\"application/json\">";
	private String end_text = "</script>";
	
	public void searchCreator(EData data) {
		try {
			HTTPSClientConnection hcc = new HTTPSClientConnection();
			hcc.connectGet(PRE_URL + data.getString("name"), MediaType.APPLICATION_JSON_VALUE , "");
			String readResult = hcc.readResult();
			hcc.close();
			
			int textData_start_index = readResult.indexOf(start_text);
			int textData_end_index = readResult.indexOf(end_text, textData_start_index);
			String extractData = readResult.substring(textData_start_index, textData_end_index);
			JSONObject extractData_json = new JSONObject(extractData);
			
			JSONObject defaultScope = extractData_json.getJSONObject("__DEFAULT_SCOPE__");
			JSONObject webappUserDetail = defaultScope.getJSONObject("webapp.user-detail");
			
			JSONObject userInfo = webappUserDetail.getJSONObject("userInfo");
			JSONObject user = userInfo.getJSONObject("user");
			JSONObject stats = userInfo.getJSONObject("stats");
			
			
			EData addData = new EData();
			addData.put("uniqueId", user.getString("uniqueId"));
			addData.put("nickname", user.getString("nickname"));
			addData.put("signature", user.getString("signature"));
			addData.put("secUid", user.getString("secUid"));
			addData.put("region", user.getString("region"));
			addData.put("language", user.getString("language"));
			addData.put("followerCount", user.getString("followerCount"));
			addData.put("followingCount", user.getString("followingCount"));
			addData.put("videoCount", user.getString("videoCount"));
			addData.put("email", extractEmail(user.get("signature")));
			EData subData = searchCreatorPageItemList(user.getString("secUid"));
			addData.put("last_video_time", subData.getString("last_video_time"));
//			addData.put("avg_video_play_count", subData.getString("avg_video_play_count"));
//			addData.put("avg_video_digg_count", subData.getString("avg_video_digg_count"));
			
			creatorMapper.modify(addData);
			
			
//	        dataAdd(re_data, id)
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	
	private EData searchCreatorPageItemList(String secUid) {
		try {
			String WebIdLastTime = System.currentTimeMillis() + "";
			paramSet.replace("#WebIdLastTime#", WebIdLastTime).replace("#secUid#", secUid);
			
			String parameters = paramSet.replace("#WebIdLastTime#", WebIdLastTime).replace("#secUid#", secUid);
			
			String xBogus = xBogusMake(parameters);
			String targetUrl = detailInfoUrl+ parameters + "&X-Bogus=" + xBogus;
			
			
			EData data = new EData();
			data.put("fullNotiURL", targetUrl);
			data.put("contentType", "application/json");
			data.put("UserAgent", user_agent);
			data.put("cookie", cookie_param);
			data.put("reqMethod", "GET");
			
			HTTPSClientConnection hcc = new HTTPSClientConnection();
			boolean connectResult = hcc.connect( data );
			String readResult = hcc.readResult();
			hcc.close();
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		EData reData = new EData();
		return reData;
	}
	
	String detailInfoUrl = "https://www.tiktok.com/api/post/item_list/?";
	
	String mstokens = "WjNSbOHBV6p9-SHvVJJZWNktZG-oyvKldDmusC9SUQwtpkJH6JlyVBULFn8j86NQQthJqL1u2cjgPAqV0a9K9_HCuAFb0HzPDslkRd1a-K-ukXnDZz-3Kcz5zCuWt-kXtCbGQUqB-6PFIg==";
	
	String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36";
	String cookie_param = "msToken=jUzSLO0qr7IbH8TunQflzBov8JAWdJdurTVITpb0Er9o7sUGNs6q0GgfxGoPWmJ46ii8jgRjRu1njGcjYx7BXAX4E6rFqoMAG4MMlq0tNxhWSZYozfUh9RGCNtISFJ4sFMOct3zs-55pgg==; msToken=fgV4Q0kQpLCIZ1yvZTPsHaRk8NMD-OyXhm_hXtxi86KFV-QReu_pSuoV0tIRyXzO6OeUULaK2DXrq68wpIsl8AbY6e1GhPI7ecfN5tSJ7GWlEDodpooHagCHUfaH4ALqErjqhWv3AUOPx63eifvLeFxu";
	String paramSet = "WebIdLastTime=#WebIdLastTime#&aid=1988&app_language=en&app_name=tiktok_web&browser_language=ko-KR&browser_name=Mozilla&browser_online=true&browser_platform=Win32&browser_version=5.0%20%28Windows%20NT%2010.0%3B%20Win64%3B%20x64%29%20AppleWebKit%2F537.36%20%28KHTML%2C%20like%20Gecko%29%20Chrome%2F135.0.0.0%20Safari%2F537.36&channel=tiktok_web&cookie_enabled=true&count=16&coverFormat=2&cursor=1744324561238&data_collection_enabled=true&device_id=7485631078528255496&device_platform=web_pc&focus_state=true&from_page=user&history_len=4&is_fullscreen=false&is_page_visible=true&language=en&needPinnedItemIds=true&odinId=7489365659659863061&os=windows&post_item_list_request_type=0&priority_region=&referer=&region=KR&screen_height=1600&screen_width=2560&secUid=#secUid#&tz_name=Asia%2FSeoul&user_is_login=false&webcast_language=en&msToken=p1a7VOyTJiZRVgI98E923OtqPQ_w1PNVIqR-XJD3TSO2A9AoEaMSqqApi9ZlxFZH5IHCOUaNNP4UGdo1CNicjr7qBHtCbGU7Y0nkrtzaTTVsLCSeP3EsjbQs62HIjAUQuoAAjNkRhYPGcg==";
	
	private String xBogusMake(String parameters) {
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		
		URL resourceUrl = this.getClass().getClassLoader().getResource("js/aes.js");
		String fileLocation = resourceUrl.getFile();
		// evel 메소드를 사용하여 컴파일
		try {
			se.eval(new FileReader(fileLocation));
		} catch (FileNotFoundException | ScriptException e) {
			e.printStackTrace();
		}
		
		String aesKey = "test";
		String ob = null;
		
		// 자바스크립트의 함수를 실행하게 해주는 Invocable
		Invocable inv = (Invocable) se;
		
		// invokeFunction을 통한 특정 함수 호출
		try {
			ob = (String) inv.invokeFunction("sign", parameters, user_agent);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
		
		return ob;
	}
	
	
	private String extractEmail(Object obj) {
		String reStr = "";
		if( obj != null && !obj.equals("")) {
			Pattern p = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
			Matcher m = p.matcher(obj.toString());
			while(m.find()) {
				reStr = m.group(1);
			}
		}
		return reStr;
	}
	
}
	
	
	
	

	
}
