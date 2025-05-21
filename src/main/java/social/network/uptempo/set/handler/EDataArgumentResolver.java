package social.network.uptempo.set.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.StringUtil;

//@Component
public class EDataArgumentResolver implements HandlerMethodArgumentResolver  {
	
	private static Logger logger = LoggerFactory.getLogger(EDataArgumentResolver.class);
	
//	@Autowired
//	SessionLocaleResolver localeResolver;
	
	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest,
			WebDataBinderFactory binderFactory) throws Exception {
			
		HttpServletRequest request = (HttpServletRequest)nativeWebRequest.getNativeRequest();
		
		Class<?> parameterType = methodParameter.getParameterType();
		
		//Controller method 파라미터에 EData가 있으면 request parameter값들을 EData에 저장하여 돌려준다.
		if(parameterType.equals(EData.class)){
			
			EData eData = new EData();
						
			Enumeration parameterNames = request.getParameterNames();
			
			String paramName;
			while(parameterNames.hasMoreElements()){
				paramName = (String)parameterNames.nextElement();
				
				String[] parameterValues = request.getParameterValues(paramName);
				
				// 파라미터값이 배열로 넘어오면 배열로 저장
				if(parameterValues != null){
					if(parameterValues.length > 0){
						
						//SQL Injection 방지 param값들 문자 치환
						for(int i=0; i < parameterValues.length; i++){
							
							for(int j=0; j < StringUtil.SQL_INJECTION_CHAR.length; j++){
								
								parameterValues[i] = parameterValues[i].replaceAll(StringUtil.SQL_INJECTION_CHAR[j][0]
																				, StringUtil.SQL_INJECTION_CHAR[j][1]);
							}
							
						}
						
						if(parameterValues.length > 1){
							eData.put(paramName, parameterValues);
						}else{
							eData.put(paramName, parameterValues[0]);
						}
					}
				}
			}
			
			String method = request.getMethod();
			String contentType = "";
			if(null != request.getContentType()){
				contentType = request.getContentType().toUpperCase();
			}
			
			if( 
					(
							HttpMethod.POST.matches(method)
							|| HttpMethod.PUT.matches(method)
							|| HttpMethod.DELETE.matches(method)
					)
					&& 
					contentType.contains("JSON") ){
				
				InputStream inputStream = request.getInputStream();
	            if (inputStream != null) {
	                StringBuilder stringBuilder = new StringBuilder();
	                BufferedReader bufferedReader = null;
	                try {
		                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		                char[] charBuffer = new char[128];
		                int bytesRead = -1;
		                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		                    stringBuilder.append(charBuffer, 0, bytesRead);
		                }
	                } catch (IOException ex) {
//	                    throw ex;
	                } finally {
	                    if (bufferedReader != null) {
	                        try {
	                            bufferedReader.close();
	                        } catch (IOException ex) {
//	                            throw ex;
	                        }
	                    }
	                }
	                
	                if(stringBuilder.length() > 0) {
	                	ObjectMapper mapper = new ObjectMapper();
	                	eData = mapper.readValue(stringBuilder.toString(), EData.class);
	                }
	            }
	            inputStream.close();
			}
			
			boolean isMobile = true;
			String MOBILE = "MOBI";
			String userAgent = request.getHeader("User-Agent").toUpperCase();
			//모바일이 아닌 경우
			if (StringUtils.isBlank(userAgent)) {
				isMobile = false;
			}
			if (userAgent.indexOf(MOBILE) == -1) {
				isMobile = false;
			}
			if (userAgent.indexOf("IPHONE") == -1) {
				isMobile = false;
			}
			eData.put("isMobile", isMobile);
//			Device device = DeviceUtils.getCurrentDevice(RequestContextHolder.currentRequestAttributes());
			// Mobile / Tablet = m
			// PC = p
			// 2019-05-24 : Mobile 페이지의 경우 이후 작업으로 인해서 m값을 p로 변경
			
			
//			HttpServletResponse res = (HttpServletResponse)nativeWebRequest.getNativeResponse();
//			res.setHeader("Access-Control-Allow-Credentials", "true");
//			res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//			res.setHeader("Access-Control-Max-Age", "3600");
//			res.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//			res.setHeader("Access-Control-Allow-Origin", "*"); 
//			res.setHeader("Access-Control-Allow-Origin", "http://203.227.22.174:63342"); 
//			eData.setHttpServletResponse(res);
			
//			if(logger.isDebugEnabled()){
//				logger.debug("@@>> request.getContentType() : " + request.getContentType());
//				logger.debug("@@>> EDataArgumentResolver EData requestMapping info : " + eData.toString());
//			}
			

			//EData에 request, response를 저장한다.
			eData.setHttpServletRequest(request);
			eData.setHttpServletResponse((HttpServletResponse)nativeWebRequest.getNativeResponse());
			
			
			if(logger.isDebugEnabled()){
				logger.debug("@@>> request.getContentType() : " + request.getContentType());
				logger.debug("@@>> EDataArgumentResolver EData requestMapping info : " + eData.toString());
			}
			
			return eData;
		}
		
		return WebArgumentResolver.UNRESOLVED;
	}

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
//		return true;
		return methodParameter.getParameterType().equals(EData.class);	}

}
