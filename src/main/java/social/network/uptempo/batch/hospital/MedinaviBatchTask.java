package social.network.uptempo.batch.hospital;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import social.network.uptempo.batch.mapper.HospitalMapper;
import social.network.uptempo.constants.CommonConstants;
import social.network.uptempo.constants.HospitalConstants;
import social.network.uptempo.set.data.EData;

//@Component
public class MedinaviBatchTask {
	
	@Autowired
	private HospitalMapper mapper;
	
	
//	# 서울 : 110000
//	# 부산 : 210000
//	# 인천 : 220000
//	# 대구 : 230000
//	# 광주 : 240000
//	# 대전 : 250000
//	# 울산 : 260000
//	# 경기 : 310000
//	# 강원 : 320000
//	# 충북 : 330000
//	# 충남 : 340000
//	# 전북 : 350000
//	# 전남 : 360000
//	# 경북 : 370000
//	# 경남 : 380000
//	# 제주 : 390000
//	# 세종시 : 410000
//	String[] regions = {
//			"110000","210000","220000","230000"
////			,"240000","250000","260000","310000"
////			,"320000","330000","340000","350000"
////			,"360000","370000","380000","390000","410000"
//			};
//	# 피부과 : 14
//	# 성형외과 : 08
//	# 치과 : 49
//	String[] gubuns = {"08","49","14"};
//	String url = "http://medinavi.co.kr/medical_course/{region}_{gubun}_{startIndex}.html";
	
	
	boolean is_run = false;
	
	public void doTask() throws Exception {
		String url = HospitalConstants.MEDI_URL;
		String[] regions = HospitalConstants.MEDI_REGIONS_1;
		String[] gubuns = HospitalConstants.MEDI_GUBUN;
		System.out.println("@@@@@@@@@@@ MedinaviBatchTask call @@@@@@@@@@");
		
		if(!is_run) {
			is_run = true;
			System.out.println("@@@@@@@@@@@ MedinaviBatchTask start @@@@@@@@@@");
			for(int a=0,b=regions.length; a<b; a++) {
				for(int i=0,j=gubuns.length; i<j; i++) {
					try {
						int startIdx = 1;
						int lastIdx = 0;
						boolean loopStatus = true;
						while(loopStatus) {
							String targetUrl = url.replace("{region}", regions[a]).replace("{gubun}", gubuns[i]).replace("{startIndex}", startIdx+"");
							System.out.println("@@@@@@@@@@@ targetUrl : " + targetUrl);
							Connection conn = Jsoup.connect(targetUrl);
							// 3. HTML 파싱.
							Document html = conn.get(); // conn.post();
							// 4. HTML 출력
							Elements divList = html.select("div.list");
							
							if(startIdx == 1) {
								String all = html.text();
								
								try{
									int textStartIdx = all.indexOf("[Total Page:");
									String oneStepText = all.substring(textStartIdx+12);
									int textEndIdx = oneStepText.indexOf("]");
									String lastNum = oneStepText.substring(0, textEndIdx).trim();
									lastIdx = Integer.parseInt(lastNum);
								}
								catch(Exception e) {
//					    		e.printStackTrace();
									System.out.println("last idx error");
								}
							}
							for(Element el : divList) {
								EData data = hospitalScrapingMap(el);
								System.out.println(data.toString());
								data.put("insert_id", "schedule");
								data.put("update_id", "schedule");
								data.put("region", regions[a]);
								data.put("gubun", gubuns[i]);
								mapper.infoAdd(data);
							}
							
							startIdx++;
							if(startIdx > lastIdx) {
								loopStatus = false;
							}
							Thread.sleep(3 * 1000);
						}
					}
					catch(Exception e){
						e.printStackTrace();
						System.out.println("region = " + regions[a] + " : gubun = " + gubuns[i]);
					}
					Thread.sleep(7 * 1000);
				}
			}
			is_run = false;
		}

	}
	

	private EData hospitalScrapingMap(Element el) {
		EData eData = new EData();
		// 제거할 항목이 있으며 제거를 해야만 ID로써 사용 가능 ( /hospital/, .html )
		String id = el.getElementsByTag("a").attr("href").replace("/hospital/", "").replace(".html", "");
		eData.put("id", id);
		eData.put("name", el.getElementsByTag("a").get(0).text());
		
		String text = el.toString();
		try{
			int textStartIdx = text.indexOf("<br>");
			String oneStepText = text.substring(textStartIdx+4);
			int textEndIdx = oneStepText.indexOf("<br>");
			String address = oneStepText.substring(0, textEndIdx).trim().replace("\n", "");
			eData.put("address", address);
		}
		catch(Exception e) {
			eData.put("address", "not address");
		}
		
		eData.put("type", el.select("font.red").get(0).text());
		
		try {
			eData.put("link_url", el.getElementsByTag("a").get(2).text());
		}
		catch(Exception e) {
			eData.put("link_url", "not link");
		}
		
		try{
			int textStartIdx = text.indexOf("</font>");
			String oneStepText = text.substring(textStartIdx+7);
			int textEndIdx = oneStepText.indexOf("<br>");
			String etc = oneStepText.substring(0, textEndIdx).trim().replace("&nbsp;", "").replace("\n", "");
			eData.put("etc", etc);
		}
		catch(Exception e) {
			eData.put("etc", "not etc");
		}
		return eData;
	}
	
	
	private static List<EData> hospitalScraping(Document html) {
		List<EData> list = new ArrayList();
		Elements divList = html.select("div.list");
		
		for(Element el : divList) {
			EData eData = new EData();
//        	System.out.println(el.getElementsByTag("a"));
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			// 제거할 항목이 있으며 제거를 해야만 ID로써 사용 가능 ( /hospital/, .html )
			String id = el.getElementsByTag("a").attr("href").replace("/hospital/", "").replace(".html", "");
			String name = el.getElementsByTag("a").get(0).text();
			eData.put("id", id);
			eData.put("name", name);
			
			String text = el.toString();
			
			try{
				int textStartIdx = text.indexOf("<br>");
				String oneStepText = text.substring(textStartIdx+4);
				int textEndIdx = oneStepText.indexOf("<br>");
				String address = oneStepText.substring(0, textEndIdx).trim().replace("\n", "");
				eData.put("address", address);
			}
			catch(Exception e) {
//        		e.printStackTrace();
				eData.put("address", "not address");
			}
			
			String type = el.select("font.red").get(0).text();
			eData.put("type", type);
			
			try {
				String aLink = el.getElementsByTag("a").get(2).text();
				eData.put("aLink", aLink);
			}
			catch(Exception e) {
//        		e.printStackTrace();
				eData.put("aLink", "not link");
			}
			
			try{
				int textStartIdx = text.indexOf("</font>");
				String oneStepText = text.substring(textStartIdx+7);
				int textEndIdx = oneStepText.indexOf("<br>");
				String etc = oneStepText.substring(0, textEndIdx).trim().replace("&nbsp;", "").replace("\n", "");
				eData.put("etc", etc);
			}
			catch(Exception e) {
//        		e.printStackTrace();
				eData.put("etc", "not etc");
			}
			list.add(eData);
		}
		return list;
	}
	
	
	public static void main(String[] args) throws Exception {
		String targetUrl = "http://medinavi.co.kr/medical_course/110000_14_1.html";
		Connection conn = Jsoup.connect(targetUrl);
		 
        // 3. HTML 파싱.
        Document html = conn.get(); // conn.post();
        
        List<EData> list = hospitalScraping(html);
        System.out.println(list.toString());
        // 4. HTML 출력
////        System.out.println( html.toString() ); 
//        Elements divList = html.select("div.list");
//        
//        for(Element el : divList) {
////        	System.out.println(el.getElementsByTag("a"));
//        	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        	String id = el.getElementsByTag("a").attr("href").replace("/hospital/", "").replace(".html", "");
//        	System.out.println(id); // 제거할 항목이 있으며 제거를 해야만 ID로써 사용 가능 ( /hospital/, .html )
//        	String name = el.getElementsByTag("a").get(0).text();
//        	System.out.println(name);
//        	
//        	
//        	String text = el.toString();
//        	try{
//        		int textStartIdx = text.indexOf("<br>");
//        		String oneStepText = text.substring(textStartIdx+4);
//        		int textEndIdx = oneStepText.indexOf("<br>");
//        		String address = oneStepText.substring(0, textEndIdx).trim().replace("\n", "");
//        		System.out.println(address);
//        		
//        	}
//        	catch(Exception e) {
////        		e.printStackTrace();
//        		System.err.println("not address");
//        	}
//        	
//        	String type = el.select("font.red").get(0).text();
//        	System.out.println(type);
//        	
//        	try {
//        		String aLink = el.getElementsByTag("a").get(2).text();
//        		System.out.println(aLink);
//        	}
//        	catch(Exception e) {
////        		e.printStackTrace();
//        		System.err.println("not link");
//        	}
//        	
//        	try{
//        		int textStartIdx = text.indexOf("</font>");
//        		String oneStepText = text.substring(textStartIdx+7);
//        		int textEndIdx = oneStepText.indexOf("<br>");
//        		String etc = oneStepText.substring(0, textEndIdx).trim().replace("&nbsp;", "").replace("\n", "");
//        		System.out.println(etc);
//        	}
//        	catch(Exception e) {
////        		e.printStackTrace();
//        		System.err.println("not etc");
//        	}
//        }
//        
	}
}
