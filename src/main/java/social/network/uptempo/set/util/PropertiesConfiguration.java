/**
 * @author jinbos
 * @version 00.00.01
 * @date 2012.11.09
 * @category kr.co.etribe.framework.util
 * @description Properties를 불러오는 Util 파일이며, properties 변경에도 Was Restart가 필요 없다.
 */
package social.network.uptempo.set.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class PropertiesConfiguration {

	private static Logger logger = LoggerFactory.getLogger(PropertiesConfiguration.class);
	
	private static String root_path = "conf/";
	
	private PropertiesConfiguration(){
		super();
	}
	
	private static String _getProperties(String module, String key) throws Exception{
		Properties props = new Properties();
		InputStream in = null;
		
		try {
			File pFile = new ClassPathResource(root_path+module).getFile();
			in = new FileInputStream(pFile);
			props.load(in);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally{
			if(in != null){
				in.close();
			}
			
		}
		
		return props.getProperty(key);
	}
	
	public static String getString(String module, String key) throws Exception {
		_getProperties(module, key);
		return _getProperties(module, key);
	}
	
	public static Integer getInteger(String module, String key) throws Exception {
		String reVal = _getProperties(module, key);
		return Integer.parseInt(reVal);
	}
	
	public static Long getLong(String module, String key) throws Exception {
		return new Long(_getProperties(module, key));
	}
	
	public static boolean getBoolean(String module, String key) throws Exception {
		String strval = _getProperties(module, key);
		if (null == strval) return false;
		return Boolean.parseBoolean(strval);
	}
	
}
