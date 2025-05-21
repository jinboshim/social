/**
 * 화훼 유통정보 수집
 */

package social.network.uptempo.batch.flower;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;

import social.network.uptempo.batch.context.ApplicationContextProvider;
import social.network.uptempo.batch.mapper.CodeMapper;
import social.network.uptempo.batch.mapper.CreatorMapper;
import social.network.uptempo.batch.mapper.ScheduleInfoMapper;
import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.HTTPSClientConnection;
import social.network.uptempo.set.util.StringUtil;


/*
 * https://flower.at.or.kr/haa00_new/haa00_new.do
 * 유통정보를 수집할수 있도록 한다.
 * 매일 1시간단위로 수집을 진행하고자 한다.
 */

//@Component
public class FlowerAtOrKr {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext;
	private ScheduleInfoMapper scheduleInfoMapper;
	private CreatorMapper creatorMapper;
	private CodeMapper codeMapper;
	
	public FlowerAtOrKr() {
		applicationContext = ApplicationContextProvider.getApplicationContext();
		scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
		creatorMapper = (CreatorMapper) applicationContext.getBean("creatorMapper");
		codeMapper = (CodeMapper) applicationContext.getBean("codeMapper");
	}
	
 	
//	@Scheduled(initialDelay = 5000, fixedDelay=3600000) // 1 hour delay
//	@Scheduled(cron = "0 0 ?/1 * * *", zone = "Asia/Seoul")
	public void doTask() {
		EData scheduleInfo = new EData();
		scheduleInfo.put("id", "flower");
		try {
			Thread.sleep(1000);
			EData eData = scheduleInfoMapper.getInfo(scheduleInfo);
			System.out.println(eData.toString());
			

			// start yn = Y 일경우에만 동작한다.
			if( eData.compareString("START_YN", "Y") ) {
				
				String url_1 = eData.getString("url_1");
				String url_2 = eData.getString("url_2");
				String url_3 = eData.getString("url_3");
				String url_4 = eData.getString("url_4");
				
				
//				type: day
//				gubn[]: 2
//				searchStrDate: 2025-02-19
//				searchEndDate: 2025-02-26
//				flower[]: 1
//				market[]: 0000000001
//				_search: false
//				nd: 1740538078779
//				rows: 15
//				page: 1
//				sidx: 
//				sord: asc
				JSONObject payload = new JSONObject();
				payload.put("type", "day");
				String[] gubn = {};
				payload.put("gubn", gubn);
				payload.put("searchStrDate", StringUtil.nowTime("yyyy-MM-dd"));
				payload.put("searchEndDate", StringUtil.nowTime("yyyy-MM-dd"));
				String[] flower = {"1"};
				payload.put("flower", flower);
				String[] market = {"0000000001"};
				payload.put("market", market);
				payload.put("_search", false);
				// nd = unixtime
				payload.put("nd", System.currentTimeMillis());
				payload.put("rows", 15);
				payload.put("page", 1);
				payload.put("sidx", "");
				payload.put("sord", "asc");
				
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
				
//				System.out.println(categoryList.toString());
//				for(EData categoryInfo : categoryList) {
//					List<String> stringList = new ArrayList<>();
//					stringList.add(categoryInfo.getString("PARENT_ID"));
//					stringList.add(categoryInfo.getString("ID"));
//					
//					JSONObject string_list = new JSONObject();
//					string_list.put("string_list", stringList);
//					
//					JSONArray category_list = new JSONArray();
//					category_list.put(string_list);
//					
//					creatorJob(sessionId, startCount, maxCount, url, follower_filter, category_list);
//				}
//				
//				// 정해진 범위안에 creator 조회 및 저장이 완료되면 이후 범위로 변경 할수 있도록 DB를 업데이트 해야 한다.
//				// 해당 업테이트의 경우 tb_schedule_info 테이블에 left_bound / right_bound의 값을 조정한다.
//				if( right_bound >= maxCount ) {
//					eData.put("next_count", startCount);
//				}
//				else {
//					eData.put("next_count", right_bound);
//				}
				
				scheduleInfoMapper.modifyNextCount(eData);
				
//				creatorJob(eData);
				
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			System.out.println("???@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
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
//				pagination.put("size", paginationSize);
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
				
				if(readResult == null || "".equals(readResult)) {
					logger.error("readResualt data is null or empty");
					throw new Exception();
				}
				
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
				
//				Thread.sleep(creatorSleepTime);
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
