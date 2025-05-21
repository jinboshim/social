package social.network.uptempo.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TiktokConstantsUS {

	@Value("${us.tiktok.category.full_url}")
	public void setTiktokCategoryFullurl(String value) {
		this.TIKTOK_CATEGORY_FULLURL = value;
	}
	
	public static String TIKTOK_CATEGORY_FULLURL;
	
	@Value("${us.tiktok.category.body}")
	public void setTiktokCategoryBody(String value) {
		this.TIKTOK_CATEGORY_BODY = value;
	}
	
	public static String TIKTOK_CATEGORY_BODY;
	
	
	
	
	@Value("${us.tiktok.creators.find.creators.contact.full_url}")
	public void setCreatorsFindCreatorsContactFullurl(String value) {
		this.CONTACT_FULLURL = value;
	}
	public static String CONTACT_FULLURL;
	
	
	
	
	
}
