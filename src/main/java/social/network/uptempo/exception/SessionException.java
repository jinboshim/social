package social.network.uptempo.exception;

import social.network.uptempo.set.util.MessageResourceBundle;

public class SessionException extends RuntimeException{

	private static final long serialVersionUID = -6834998936102827521L;

	
	
	//TODO final 사용 여부 확인
	private final String resultCode;
	private final String resultMsg;
	
	public SessionException(String resultType, String message){
		String[] propertiesCodeMsg;
		if(message != null && !"".equals(message)){
			propertiesCodeMsg = MessageResourceBundle.getString(resultType, (Object)message).split(":");
		}
		else{
			propertiesCodeMsg = MessageResourceBundle.getString(resultType).split(":");
		}
		this.resultCode = propertiesCodeMsg[0];
		this.resultMsg = propertiesCodeMsg[1];
	}
	
	public String getResultCode(){
		return resultCode;
	}
	
	public String getResultMsg(){
		return resultMsg;
	}
}
