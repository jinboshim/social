package social.network.uptempo.batch.tiktok.us;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import social.network.uptempo.batch.context.ApplicationContextProvider;
import social.network.uptempo.batch.mapper.CategoryMapper;
import social.network.uptempo.batch.mapper.ScheduleInfoMapper;
import social.network.uptempo.constants.TiktokConstantsUS;
import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.HTTPSClientConnection;

//@RestController
//@Component
public class CategoryBatchTaskUS {

	private ApplicationContext applicationContext;
	private PlatformTransactionManager transactionManager;
	private DefaultTransactionDefinition def;
	private ScheduleInfoMapper scheduleInfoMapper;
	private CategoryMapper categoryMapper;
	
	private String cookie = "sessionid_tiktokseller="; 
	
	public CategoryBatchTaskUS() {
		applicationContext = ApplicationContextProvider.getApplicationContext();
		transactionManager = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
		def = new DefaultTransactionDefinition();
		
		scheduleInfoMapper = (ScheduleInfoMapper) applicationContext.getBean("scheduleInfoMapper");
		categoryMapper = (CategoryMapper)applicationContext.getBean("categoryMapper");
	}
	

//	@Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
	@Scheduled(initialDelay = 3000, fixedDelay=300000) // 50min delay
	public void doTask() {
		EData scheduleInfo = new EData();
		scheduleInfo.put("id", "categoryBatchUS");
		
		try {
			EData eData = scheduleInfoMapper.getInfo(scheduleInfo);

			if( eData.compareString("START_YN", "Y") ) {
				tiktokCategory(eData);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
	}
	
	public void tiktokCategory(EData eData){
		cookie += eData.getString("SESSIONID") + "; s_v_web_id=verify_m4i1kd9u_eLEMX4KB_HgKN_4RA4_947y_V28gHLI90hez";
		
		try {
			HTTPSClientConnection hcc = new HTTPSClientConnection();
			hcc.connectPost(TiktokConstantsUS.TIKTOK_CATEGORY_FULLURL, MediaType.APPLICATION_JSON_VALUE, cookie);
			hcc.sendMessage(TiktokConstantsUS.TIKTOK_CATEGORY_BODY);
			String readResualt = hcc.readResult();
			System.out.println(readResualt);
			
			JSONObject resultData = new JSONObject(readResualt);
			
			if(resultData == null || resultData.length() == 0) {
				System.out.println("data is null or empty");
				new NullPointerException();
			}
			if(resultData.getInt("code") > 0) {
				System.out.println("data is error");
				new Exception();
			}
			
			JSONObject options = resultData.getJSONObject("options");
			// brand
			JSONObject options1 = options.getJSONObject("1");
			// category
			JSONObject options2 = options.getJSONObject("2");
			// GMV
			JSONObject options3 = options.getJSONObject("3");
			
			JSONArray optionList = options2.getJSONArray("option_list");
			
			for(int i=0, j=optionList.length(); i<j; i++) {
				
				JSONObject optionData = optionList.getJSONObject(i);
				
				EData parentData = new EData();
				parentData.put("id", optionData.getInt("id"));
				parentData.put("parent_id", 21);
				parentData.put("code_name", optionData.getString("name"));
				parentData.put("code_value", optionData.getString("starling_key"));
				parentData.put("description", optionData.getString("starling_key"));
				parentData.put("order", i+1);
				parentData.put("insert_id", "scheduler");
				parentData.put("update_id", "scheduler");
				
				categoryMapper.categoryAdd(parentData);
				
				int nextDepth = parentData.getInt("nextDepth") + 1;
				
				JSONArray optionChildren = optionData.getJSONArray("option_children");
				for(int a=0, b=optionChildren.length(); a<b; a++) {
					EData childrenData = new EData();
					JSONObject childrenInfo = optionChildren.getJSONObject(a);
					
					childrenData.put("id", childrenInfo.getInt("id"));
					childrenData.put("parent_id", optionData.getInt("id"));
					childrenData.put("depth", nextDepth);
					childrenData.put("code_name", childrenInfo.getString("name"));
					childrenData.put("code_value", childrenInfo.getString("starling_key"));
					childrenData.put("description", childrenInfo.getString("starling_key"));
					childrenData.put("order", a+1);
					childrenData.put("insert_id", "scheduler");
					childrenData.put("update_id", "scheduler");
					
					categoryMapper.categoryAdd(childrenData);
				}
			}
//			transactionManager.commit(status);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}
