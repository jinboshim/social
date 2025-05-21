package social.network.uptempo.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HospitalConstants {

	//http://medinavi.co.kr/medical_course/{region}_{gubun}_{startIndex}.html
	@Value("${hospital.medi.url}")
	public void setCreatorsFindCreatorsUrl(String value) {
		this.MEDI_URL = value;
	}
	public static String MEDI_URL;	
	
	//110000,210000,220000,230000
	@Value("${hospital.medi.regions.1}")
	public void setMediRegions1(String value) {
		this.MEDI_REGIONS_1 = value.split(",");
	}
	public static String[] MEDI_REGIONS_1;	
	
	//240000,250000,260000,310000
	@Value("${hospital.medi.regions.2}")
	public void setMediRegions2(String value) {
		this.MEDI_REGIONS_2 = value.split(",");
	}
	public static String[] MEDI_REGIONS_2;	
	
	//320000,330000,340000,350000
	@Value("${hospital.medi.regions.3}")
	public void setMediRegions3(String value) {
		this.MEDI_REGIONS_3 = value.split(",");
	}
	public static String[] MEDI_REGIONS_3;	
	
	//360000,370000,380000,390000,410000
	@Value("${hospital.medi.regions.4}")
	public void setMediRegions4(String value) {
		this.MEDI_REGIONS_4 = value.split(",");
	}
	public static String[] MEDI_REGIONS_4;	
	
	//360000,370000,380000,390000,410000
	@Value("${hospital.medi.gubun}")
	public void setMediGubun(String value) {
		this.MEDI_GUBUN = value.split(",");
	}
	public static String[] MEDI_GUBUN;	
	
	
	
	
	
	
	
	
}
