package social.network.uptempo.common;

import java.util.Iterator;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import social.network.uptempo.constants.MessageConstants;
import social.network.uptempo.constants.ResultConstants;
import social.network.uptempo.set.data.EData;
import social.network.uptempo.set.util.MessageResourceBundle;
import social.network.uptempo.set.util.StringUtil;

public class CommonController {

	

	private int itemCount = 10;
	private int itemBlockSize = 10;
	protected void pagingSet(EData eData) {
		eData.put("itemCount", eData.isNull("itemCount") ? itemCount : eData.getInt("itemCount"));
		eData.put("page", StringUtil.nvl(eData.get("page"),"1"));
		eData.put("START_PAGE", (eData.getInt("page")-1)*eData.getInt("itemCount"));
		eData.put("END_PAGE", eData.getInt("page")*eData.getInt("itemCount"));
		eData.put("itemBlockSize", itemBlockSize);
	}
	
	
	
	protected ModelAndView successReturn(EData eData, Object obj) {
		ModelAndView mav = new ModelAndView(ResultConstants.JSON_VIEW);

		String uri = eData.getHttpServletRequest().getRequestURI();
		
		mav.addObject(ResultConstants.RESULT, ResultConstants.RESULT_SUCCESS);
			
		String[] propertiesCodeMsg = MessageResourceBundle.getString(MessageConstants.SUCCESS).split(":");
		mav.addObject(ResultConstants.RESULT_CODE, propertiesCodeMsg[0]);
		mav.addObject(ResultConstants.RESULT_MSG, propertiesCodeMsg[1]);
		if(obj != null) {
			if(obj instanceof List) {
				mav.addObject(ResultConstants.LIST, (List)obj);
			}
			else if(obj instanceof EData) {
				mav.addObject(ResultConstants.DATA, (EData)obj);
			}
		}
		return mav;
	}
	
	// resultCode로 감싸지 않고 그냥 Return 하는 Case 추가
	protected ModelAndView successReturn2(EData eData, Object obj) {
		ModelAndView mav = new ModelAndView(ResultConstants.JSON_VIEW);
		
		if(obj != null) {
			if(obj instanceof List) {
				mav.addObject(ResultConstants.LIST, (List)obj);
			}
			else if(obj instanceof EData) {
				Iterator<String> it = ((EData) obj).keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					mav.addObject(key, ((EData) obj).get(key));
				}
			}
		}
		
		return mav;
	}
	
	
}
