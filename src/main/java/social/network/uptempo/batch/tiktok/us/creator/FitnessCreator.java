package social.network.uptempo.batch.tiktok.us.creator;

import java.util.ArrayList;
import java.util.List;

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
public class FitnessCreator {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext;
	private ScheduleInfoMapper scheduleInfoMapper;
	private CreatorMapper creatorMapper;
	private CodeMapper codeMapper;
	
	private static int creatorSleepTime = 10 * 1000; //milliseconds
	private static int categorySleepTime = 240 * 1000; //milliseconds
	
	private static int paginationSize = 20;
	
	public FitnessCreator() {
		applicationContext = ApplicationContextProvider.getApplicationContext();
		scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
		creatorMapper = (CreatorMapper) applicationContext.getBean("creatorMapper");
		codeMapper = (CodeMapper) applicationContext.getBean("codeMapper");
	}
	
	
	// 크리에이터 모집을 위한 로직을 개발한다.(Partner 계정을 사용할시)
	// DB에서 Creator 정보를 위한 조회 기준 값을 가지고 와야 한다.
	// 이전에 사용했던 방법은 효율적이지 못해서 별도의 테이블 및 별도의 방식이 필요해 보입니다.
	// 1. header로 설정할 Cookie에 대해서 가장 중요한 2개의 값이 변경될 소지가 너무 많다.
	//  - 해당 값을 2개의 컬럼으로 진행할지 아니면 json 구조를 가진 형태로 만들어 놓고 바로 할지 고민중이다.
	//  - 일단 컬럼으로 분리해보고자 한다.
	//  - table 명 : tb_scraption_info
	//  - column 정보 : id / s_v_web_id / sessionid_tiktokseller
	
	
	@Scheduled(initialDelay = 10000, fixedDelay=60000) // 1min delay
	public void doTask2() {
		EData scheduleInfo = new EData();
		scheduleInfo.put("id", "creatorCategoryBatchUS2");
		try {
			Thread.sleep(1000);
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
				// exhealthcare
//				List<EData> categoryList = codeMapper.tiktokCategoryBeautyListUS(eData);
				List<EData> categoryList = new ArrayList();
				EData cateData = new EData();
				cateData.put("PARENT_ID","603014");
				cateData.put("ID","835336");
				categoryList.add(cateData);
				cateData = new EData();
				cateData.put("PARENT_ID","700645");
				cateData.put("ID","700646");
				categoryList.add(cateData);
				cateData = new EData();
				cateData.put("PARENT_ID","700645");
				cateData.put("ID","950792");
				categoryList.add(cateData);
				
				System.out.println(categoryList.toString());
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
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		}
		 
	}

	private void creatorJob(String sessionId, int startCount, int maxCount, String url, JSONObject follower_filter, JSONArray category_list) {
		String cookie = sessionId;
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@creatorJob@@@@@@@@@@");
		int page = 0;
		boolean hasMore = true;
		String searchKey = "";
		int next_item_cursor = 0;
		
		System.out.println("tiktok creator start : " + StringUtil.nowTime("yyyy/MM/dd E HH:mm"));
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
				
				if(readResult == null || "".equals(readResult)) {
					logger.error("readResualt data is null or empty");
					throw new Exception();
				}
				
				JSONObject resultData = new JSONObject(readResult);
				System.out.println("@@@@@@@@@@@ = "+readResult);
				System.out.println("@@@@@@@@@@@ = "+resultData.get("code"));
				
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
					
//					boolean contact = creatorInfo.getJSONObject("contact_info_available").getBoolean("value");
					boolean contact = false;
					if(!creatorInfo.isNull("contact_info_available")) {
						contact = creatorInfo.getJSONObject("contact_info_available").getBoolean("value");
					}
					
					
					EData creatorData = new EData();
					creatorData.put("id", id);
					creatorData.put("type_cd", 12);
					creatorData.put("name", name);
					creatorData.put("nick_name", nick_name);
					creatorData.put("creator_link", "https://www.tiktok.com/@"+name);
					creatorData.put("region_cd", 1002);
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
			// TODO: handle exception
//			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
