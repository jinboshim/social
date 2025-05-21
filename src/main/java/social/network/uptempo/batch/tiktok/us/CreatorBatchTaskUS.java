package social.network.uptempo.batch.tiktok.us;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import social.network.uptempo.batch.context.ApplicationContextProvider;
import social.network.uptempo.batch.mapper.CategoryMapper;
import social.network.uptempo.batch.mapper.CreatorMapper;
import social.network.uptempo.batch.mapper.ScheduleInfoMapper;
import social.network.uptempo.constants.TiktokConstants;
import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.HTTPSClientConnection;
import social.network.uptempo.set.util.StringUtil;

//@Component
public class CreatorBatchTaskUS {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext;
	private PlatformTransactionManager transactionManager;
	private DefaultTransactionDefinition def;
	private ScheduleInfoMapper scheduleInfoMapper;
	private CreatorMapper creatorMapper;
	
	private static int creatorSleepTime = 5 * 1000; //milliseconds
	
	private static int paginationSize = 12;
	
	private static int contactSleepTime = 1 * 1000; //milliseconds
	
	public CreatorBatchTaskUS() {
		applicationContext = ApplicationContextProvider.getApplicationContext();
		transactionManager = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
		def = new DefaultTransactionDefinition();
		
		scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
		creatorMapper = (CreatorMapper)applicationContext.getBean("creatorMapper");
	}
	
	
	@Scheduled(initialDelay = 3000, fixedDelay=120000) // 2min delay
	public void doTask() {
		EData scheduleInfo = new EData();
		scheduleInfo.put("id", "creatorBatchUS");
		try {
			EData eData = scheduleInfoMapper.getInfo(scheduleInfo);
			System.out.println(eData.toString());

			// start yn = Y 일경우에만 동작한다.
			if( eData.compareString("START_YN", "Y") ) {
				creatorJob(scheduleInfoMapper, creatorMapper, eData);
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			logger.error(e.getMessage());
		}
		 
	}


	private void creatorJob(ScheduleInfoMapper scheduleInfoMapper, CreatorMapper creatorMapper, EData eData) {
//		String cookie = "sessionid_tiktokseller=" + eData.getString("SESSIONID");
		String cookie = "sessionid_tiktokseller=061e9b7d6856b040afd703f7c6f606bb";
		
		int page = 0;
		boolean hasMore = true;
		String searchKey = "";
		int next_item_cursor = 0;
		int left_bound = eData.getInt("NEXT_COUNT");
		int right_bound = left_bound + eData.getInt("NEXT_STEP");
		
		logger.info("tiktok creator start : " + StringUtil.nowTime("yyyy/MM/dd E HH:mm"));
		
		try {
			while(hasMore) {
//						TransactionStatus status = transactionManager.getTransaction(def);
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
				
				if(eData.getInt("START_COUNT") >= 0 && eData.getInt("MAX_COUNT") > 0) {
					JSONObject follower_filter = new JSONObject();
					follower_filter.put("left_bound", left_bound);
					
					follower_filter.put("right_bound", right_bound);
					filter_params.put("follower_filter", follower_filter);
				}
				
				JSONObject bodyData = new JSONObject();
				bodyData.put("pagination", pagination);
				bodyData.put("filter_params", filter_params);
				bodyData.put("algorithm", 1);
				bodyData.put("query", "");
				logger.info("tiktok creator ing - 1 : " + page + " bodyData : " + bodyData.toString());
				
				HTTPSClientConnection hcc = new HTTPSClientConnection();
//						hcc.connectPost(TiktokConstants.TIKTOK_FULL_URL.toString(), MediaType.APPLICATION_JSON_VALUE, cookie);
				hcc.connectPost(eData.getString("URL"), MediaType.APPLICATION_JSON_VALUE, cookie);
				hcc.sendMessage(bodyData.toString());
				String readResualt = hcc.readResult();
				
				
				JSONObject resultData = new JSONObject(readResualt);
				
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
					if(categoryList.length() >= 0) {
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
			
			// 정해진 범위안에 creator 조회 및 저장이 완료되면 이후 범위로 변경 할수 있도록 DB를 업데이트 해야 한다.
			// 해당 업테이트의 경우 tb_schedule_info 테이블에 left_bound / right_bound의 값을 조정한다.
			if(right_bound >= eData.getInt("MAX_COUNT")) {
				eData.put("next_count", eData.getInt("START_COUNT"));
			}
			else {
				eData.put("next_count", right_bound);
			}
			
			scheduleInfoMapper.modifyNextCount(eData);

			logger.info("tiktok creator end : " + StringUtil.nowTime("yyyy/MM/dd E HH:mm"));
		}
		catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
}
