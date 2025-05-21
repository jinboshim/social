package social.network.uptempo.set.handler;

import java.util.ArrayList;

import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

//@Component
public class CustomRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter{
	
	public void afterPropertiesSet(){
		super.afterPropertiesSet();
		
		if(getArgumentResolvers() != null){
			ArrayList<HandlerMethodArgumentResolver> list = new ArrayList<>(getArgumentResolvers());
//			list.add(new EDataArgumentResolver());
			MapMethodProcessor mapMethodProcessor = null;
			for(int i=0; i<list.size(); i++){
				HandlerMethodArgumentResolver resolver = list.get(i);
				
				if(resolver instanceof MapMethodProcessor){
					mapMethodProcessor = (MapMethodProcessor) list.remove(i);
					break;
				}
			}
			
			if(mapMethodProcessor != null){
				for(int i=1; i<list.size(); i++){
					HandlerMethodArgumentResolver resolver = list.get(i);
					
					if(resolver instanceof EDataArgumentResolver){
						if(i+1 < list.size()){
							list.add(i+1, mapMethodProcessor);
						}
						else{
							list.add(i, mapMethodProcessor);
						}
						break;
					}
				}
			}
			setArgumentResolvers(list);
		}
		
	}

	

}
