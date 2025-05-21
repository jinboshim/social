/**
 * @author jinbos
 * @version 00.00.01
 * @date 2012.11.09
 * @category kr.co.etribe.framework.data
 * @description HasMap 을 상속받아 Data를 원하는 형식으로 Output 가능(ex:String,Int,Boolean,NullCheck,Date)
 */
package social.network.uptempo.set.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import social.network.uptempo.set.util.StringUtil;

//public class EData extends LinkedHashMap{
public class EData extends HashMap{
	
	private static final String REQUEST_KEY = "_servletRequest";
	private static final String RESPONSE_KEY = "_servletResponse";
	
	private static final String SESSIONLOCALERESOLVER_KEY = "_sessionLocaleResolver";
	
	public EData(){
		super();
//		setSysdate();
	}
	private EData(LinkedHashMap map){
		super(map);
//		setSysdate();
	}
	
	private void setSysdate() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date sysdate = new Date();
		super.put("sysdate", sysdate);
		super.put("sysdateStr",df.format(sysdate));
	}
	
	/**
	 * @param key
	 * @param values
	 * @description before SpecailCharacter Replace after Map Set 
	 */
	public void set(Object key, String[] values){
		if(values != null){
			for(int i=0, j=values.length; i<j; i++){
				values[i] = StringUtil.getSpecialCharacters(values[i]);
			}
		}
		
		super.put(key, values);
	}
	
	/**
	 * @param key
	 * @param value
	 * @description before SpecailCharacter Replace after Map Set 
	 */
	public void set(Object key, String value){
		super.put(key, getSpecialCharacters(value));
	}

	private String getSpecialCharacters(String str) {
		return StringUtil.getSpecialCharacters(str);
	}
	
	/**
	 * @param key
	 * @return object
	 * @description a key is mapped has value return 
	 */
	public Object getObject(Object key){
		Object obj = super.get(key);
		if(obj == null){
			return "";
		}
		return obj;
	}
	
	/**
	 * @param String
	 * @return String
	 */
	public String getString(String key){
		Object obj = super.get(key);
		if(obj == null){
			return "";
		}
		return obj.toString();
	}
	
	public boolean getString(String key, String str){
		if(str.equals(getString(key))){
			return true;
		}
		return false;
	}
	
	public boolean compareString(String key, String str){
		if(str.equals(getString(key))){
			return true;
		}
		return false;
	}
	

	/**
	 * @param String
	 * @return Int
	 */
	public int getInt(String key){
		Object obj = super.get(key);
		if(obj == null){
			return 0;
		}
		return Integer.parseInt(obj.toString());
	}
	
	/**
	 * @param String
	 * @return null or empty is false / not null and not empty is true
	 */
	public boolean isNull(String key){
		Object obj = super.get(key);
		if(obj == null || "".equals(obj)){
			return true;
		}
		return false;
	}

	/**
	 * @param String
	 * @return int
	 */
	public int getLength(String key){
		Object obj = super.get(key);
		if(obj == null){
			return 0;
		}
		return obj.toString().length();
	}
	
	/**
	 * @param String
	 * @return Int
	 */
	public String getDateFormat(String key, int iMode){
		return StringUtil.getDateFormat((String)super.get(key), iMode);
	}
	
	/**
	 * @param String
	 * @return String
	 */
	public String deleteHtmlTag(String key){
		return StringUtil.htmlTagDelete((String)super.get(key));
	}

	
	
	/**
	 * @param String
	 * @return Boolean
	 */
	public boolean isNumber(String key) {
		Object obj = super.get(key);
		if(obj == null){
			return false;
		}
		String str = obj.toString();
		boolean check = true;
       if (str == null || str == "") {
    	   return false;
    	   
        }
       for(int i = 0; i < str.length(); i++) {
    	   if(!Character.isDigit(str.charAt(i))){
    		   check = false;
    		   break; 
    	   }// end if
       } //end for
       return check;  
	} //isNumber 
	
	/**
	 * Map에 저장된 값이 배열(array) 인지 여부
	 * @param key
	 * @return 배열인 경우 true, 아니면 false
	 */
	public boolean isArray(String key){
		
		Object obj = super.get(key);
		
		if(obj == null){
			return false;
		}
		
		if(obj.getClass().isArray()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 해당 key 값을 String 배열로 리턴 한다.
	 * @param key
	 * @return
	 */
	public String[] getStringValues(String key){
		
		if(isArray(key)){
			
			return (String[])super.get(key);
		}else{
			
			return new String[]{ getString(key) };
		}
	}
	
	public void setHttpServletRequest(HttpServletRequest req){
		super.put(REQUEST_KEY, req);
	}
	
	public void setHttpServletResponse(HttpServletResponse res){
		super.put(RESPONSE_KEY, res);
	}
	
	/**
	 * HttpServletRequest를 리턴.
	 * @return 값이 없을시 null
	 */
	public HttpServletRequest getHttpServletRequest(){
		return (HttpServletRequest)super.get(REQUEST_KEY);
	}
	
	/**
	 * HttpServletResponse를 리턴.
	 * @return 값이 없을시 null
	 */
	public HttpServletResponse getHttpServletResponse(){
		return (HttpServletResponse)super.get(RESPONSE_KEY);
	}
	
	public void removeHttpServletRequest(){
		super.remove(REQUEST_KEY);
	}
	public void removeHttpServletResponse(){
		super.remove(RESPONSE_KEY);
	}
}
