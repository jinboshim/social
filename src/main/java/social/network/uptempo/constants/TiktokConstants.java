package social.network.uptempo.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TiktokConstants {

	@Value("${project.test.yn}")
	public void setTestYN(boolean value) {
		this.TEST_YN = value;
	}
	public static boolean TEST_YN;
	
	
	//tiktok.creators.find.creators.url=https://api-partner-va.tiktokshop.com/api/v1/oec/affiliate/creator/marketplace/4partner/find?
	@Value("${tiktok.creators.find.creators.url}")
	public void setCreatorsFindCreatorsUrl(String value) {
		this.FIND_CREATORS_URL = value;
	}
	public static String FIND_CREATORS_URL;	
	
//tiktok.creators.find.creators.url.user_language=&user_language=en
	@Value("${tiktok.creators.find.creators.url.user_language}")
	public void setCreatorsFindCreatorsUrlLanguage(String value) {
		this.FIND_CREATORS_URL_LANGUAGE = value;
	}
	public static String FIND_CREATORS_URL_LANGUAGE;	
	
//tiktok.creators.find.creators.url.aid=&aid=360019
	@Value("${tiktok.creators.find.creators.url.aid}")
	public void setCreatorsFindCreatorsUrlAid(String value) {
		this.FIND_CREATORS_URL_AID = value;
	}
	public static String FIND_CREATORS_URL_AID;	
	
//tiktok.creators.find.creators.url.app_name=&app_name=i18n_ecom_alliance
	@Value("${tiktok.creators.find.creators.url.app_name}")
	public void setCreatorsFindCreatorsUrlAppname(String value) {
		this.FIND_CREATORS_URL_APPNAME = value;
	}
	public static String FIND_CREATORS_URL_APPNAME;	
	
//tiktok.creators.find.creators.url.device_id=&device_id=0
	@Value("${tiktok.creators.find.creators.url.device_id}")
	public void setCreatorsFindCreatorsUrlDeviceid(String value) {
		this.FIND_CREATORS_URL_DEVICEID = value;
	}
	public static String FIND_CREATORS_URL_DEVICEID;	
	
//tiktok.creators.find.creators.url.fp=&fp=verify_m24d1zwt_In2sv6SG_CYey_4t5s_9oJa_tt2N3i6HL0Gu
	@Value("${tiktok.creators.find.creators.url.fp}")
	public void setCreatorsFindCreatorsUrlFp(String value) {
		this.FIND_CREATORS_URL_FP = value;
	}
	public static String FIND_CREATORS_URL_FP;	
	
//tiktok.creators.find.creators.url.device_platform=&device_platform=web
	@Value("${tiktok.creators.find.creators.url.device_platform}")
	public void setCreatorsFindCreatorsUrlPlatform(String value) {
		this.FIND_CREATORS_URL_PLATFORM = value;
	}
	public static String FIND_CREATORS_URL_PLATFORM;	
	
//tiktok.creators.find.creators.url.cookie_enabled=&cookie_enabled=true
	@Value("${tiktok.creators.find.creators.url.cookie_enabled}")
	public void setCreatorsFindCreatorsUrlCookieenabled(String value) {
		this.FIND_CREATORS_URL_COOKIEENABLED = value;
	}
	public static String FIND_CREATORS_URL_COOKIEENABLED;	
	
//tiktok.creators.find.creators.url.screen_width=&screen_width=2560
	@Value("${tiktok.creators.find.creators.url.screen_width}")
	public void setCreatorsFindCreatorsUrlScreenwidth(String value) {
		this.FIND_CREATORS_URL_SCREENWIDTH = value;
	}
	public static String FIND_CREATORS_URL_SCREENWIDTH;	
	
//tiktok.creators.find.creators.url.screen_height=&screen_height=1600
	@Value("${tiktok.creators.find.creators.url.screen_height}")
	public void setCreatorsFindCreatorsUrlScreenheight(String value) {
		this.FIND_CREATORS_URL_SCREENHEIGHT = value;
	}
	public static String FIND_CREATORS_URL_SCREENHEIGHT;	
	
//tiktok.creators.find.creators.url.browser_language=&browser_language=ko-KR
	@Value("${tiktok.creators.find.creators.url.browser_language}")
	public void setCreatorsFindCreatorsUrlBrowserlanguag(String value) {
		this.FIND_CREATORS_URL_BROWSERLANGUAG = value;
	}
	public static String FIND_CREATORS_URL_BROWSERLANGUAG;	
	
//tiktok.creators.find.creators.url.browser_platform=&browser_platform=Win32
	@Value("${tiktok.creators.find.creators.url.browser_platform}")
	public void setCreatorsFindCreatorsUrlBrowserplatform(String value) {
		this.FIND_CREATORS_URL_BROWSERPLATFORM = value;
	}
	public static String FIND_CREATORS_URL_BROWSERPLATFORM;	
	
//tiktok.creators.find.creators.url.browser_name=&browser_name=Mozilla&browser_version=5.0+(Windows+NT+10.0%3B+Win64%3B+x64)+AppleWebKit%2F537.36+(KHTML,+like+Gecko)+Chrome%2F129.0.0.0+Safari%2F537.36
	@Value("${tiktok.creators.find.creators.url.browser_name}")
	public void setCreatorsFindCreatorsUrlBrowsername(String value) {
		this.FIND_CREATORS_URL_BROWSERNAME = value;
	}
	public static String FIND_CREATORS_URL_BROWSERNAME;	
	
//tiktok.creators.find.creators.url.browser_online=&browser_online=true&timezone_name=Asia%2FSeoul
	@Value("${tiktok.creators.find.creators.url.browser_online}")
	public void setCreatorsFindCreatorsUrlBrowseronline(String value) {
		this.FIND_CREATORS_URL_BROWSERONLINE = value;
	}
	public static String FIND_CREATORS_URL_BROWSERONLINE;	
	
//tiktok.creators.find.creators.url.partner_id=&partner_id=8646957757045835521
	@Value("${tiktok.creators.find.creators.url.partner_id}")
	public void setCreatorsFindCreatorsUrlPartnerid(String value) {
		this.FIND_CREATORS_URL_PARTNERID = value;
	}
	public static String FIND_CREATORS_URL_PARTNERID;	


	
	@Value("${tiktok.creators.find.creators.full_url}")
	public void setTiktokFullUrl(String value) {
		this.TIKTOK_FULL_URL = value;
	}
	public static String TIKTOK_FULL_URL;	
	
	
	
	
	public static final String MONGO_COLLECTION_NAME_TIKTOK = "tiktok";
	
	
	@Value("${tiktok.category.full_url}")
	public void setTiktokCategoryFullurl(String value) {
		this.TIKTOK_CATEGORY_FULLURL = value;
	}
	
	public static String TIKTOK_CATEGORY_FULLURL;
	
	@Value("${tiktok.category.body}")
	public void setTiktokCategoryBody(String value) {
		this.TIKTOK_CATEGORY_BODY = value;
	}
	
	public static String TIKTOK_CATEGORY_BODY;
	
	
	
	
	
	@Value("${tiktok.creators.find.creators.contact.full_url}")
	public void setCreatorsFindCreatorsContactFullurl(String value) {
		this.CONTACT_FULLURL = value;
	}
	public static String CONTACT_FULLURL;
	
	
	
	
	
}
