package social.network.uptempo.set.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * MessageResourceBundle
 * 기본 classpath root 의 conf/message.properties 파일을 읽어온다.
 * @author etribe
 *
 */
public class MessageResourceBundle {
	
	private static ResourceBundle rb;
	
	public MessageResourceBundle(){
		super();
	}
	
	public static void init(){
		rb = ResourceBundle.getBundle("conf/message");
	}
	
	/**
	 * key에 해당하는 문자열 값을 가져온다.
	 * @param key property key
	 * @return
	 */
	public static String getString(String key){
		init();
		String val = null;
		try {
			val = rb.getString(key);
		} catch (Exception e) {
			val = "";
		}
		
		return val;
	}
	
	/**
	 * key에 해당하는 문자열 값에 parameter로 넘긴 값을 주입하여 가져온다.
	 * ex) MessageResourceBundle.getString("field.max", 10);
	 *  
	 * @param key property key
	 * @param params property 메세지의 {0}, {1} 의 문자들을 대체할 값 
	 * @return
	 */
	public static String getString(String key, Object... params){
		String msg = "";
		
		try {
			msg = getString(key);
			msg = MessageFormat.format(msg, params);
		} catch (Exception e) {}
		
		return msg; 
	}
}
