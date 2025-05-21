package social.network.uptempo.batch.tiktok.vn.contact;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import social.network.uptempo.batch.context.ApplicationContextProvider;
import social.network.uptempo.batch.mapper.CreatorMapper;
import social.network.uptempo.batch.mapper.ScheduleInfoMapper;
import social.network.uptempo.constants.TiktokConstants;
import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.HTTPSClientConnection;
import social.network.uptempo.set.util.StringUtil;

//@Component
public class ContactInfo {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 조회 횟수가 정해져 있는것으로 보이며 해당 정보는 별도의 batch가 필요해 보임
	private static int contactSleepTime = 5 * 1000; //milliseconds
	
	ApplicationContext applicationContext;
	ScheduleInfoMapper scheduleInfoMapper;
	CreatorMapper creatorMapper;
	
	public ContactInfo() {
		applicationContext = ApplicationContextProvider.getApplicationContext();
		scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
		creatorMapper = (CreatorMapper) applicationContext.getBean("creatorMapper");
		
	}
	

//	@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
	@Scheduled(initialDelay = 5000, fixedDelay=60000) // 5min delay
	public void doTask() {
//		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
////		PlatformTransactionManager transactionManager = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
////		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		
//		ScheduleInfoMapper scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
//		CreatorMapper creatorMapper = (CreatorMapper) applicationContext.getBean("creatorMapper");
		
		EData scheduleInfo = new EData();
		scheduleInfo.put("id", "contactBatch");
		String cookie = "sessionid="; 
		try {
			EData eData = scheduleInfoMapper.getInfo(scheduleInfo);
			System.out.println(eData.toString());

			// start yn = Y 일경우에만 동작한다.
			if( eData.compareString("START_YN", "Y") ) {
				cookie += eData.getString("SESSIONID");
				
				EData pageData = new EData();
				pageData.put("page_num", 1);
				pageData.put("page_row", 200);
				List<EData> creatorArray = creatorMapper.getContactList(pageData);
				
				for(EData contactObject : creatorArray) {
					String id = contactObject.getString("ID");
					contactObject.put("id", id);
					// creator의 연락처 정보를 가지고 온다.
					HTTPSClientConnection hcc = new HTTPSClientConnection();
					hcc.connectGet(eData.getString("URL") + id, MediaType.APPLICATION_JSON_VALUE, cookie);
					
					String contactResualt = hcc.readResult();
					
					JSONObject contactData = new JSONObject(contactResualt);
					
					if(contactData == null || contactData.length() == 0) {
						logger.error("contact data is null or empty");
						break;
//						throw new Exception();
					}
					if(contactData.getInt("code") > 0) {
						logger.error("contact data is error");
						break;
//						throw new Exception();
					}
					
					JSONArray contactArray = contactData.getJSONArray("contact_info");
					if(contactArray.length() > 0) {
						creatorMapper.creatorContactRemove(contactObject);
					}
					
					for(int a=0, b=contactArray.length(); a<b; a++) {
						JSONObject contactInfo = contactArray.getJSONObject(a);
						int field = contactInfo.getInt("field");
						String value = contactInfo.getString("value");
						
						EData creatorContactData = new EData();
						creatorContactData.put("id", id);
						creatorContactData.put("type", field);
						creatorContactData.put("info", value);
						creatorContactData.put("insert_id", "schedule");
						System.out.println(creatorContactData);
						creatorMapper.creatorContactAdd(creatorContactData);
						
					}
					
					creatorMapper.creatorContactTypeModify(contactObject);
					
					Thread.sleep(contactSleepTime);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 
	}
	
}
