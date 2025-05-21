package social.network.uptempo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = { "social.network.uptempo" })
@ImportResource({
	"classpath:spring/spring_service.xml"
//	,"classpath:spring/spring-servlet.xml"
	,"classpath:spring/spring_setting.xml"
		})
@EnableScheduling
@EnableAsync // threading을 위한 설정
public class MainApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApplication.class, args);
//		Properties p = System.getProperties();
//		for(Enumeration en = p.propertyNames(); en.hasMoreElements();) {
//			String key = (String)en.nextElement();
//			String value = p.getProperty(key);
//			System.out.println(key + " : " + value);
//		}
		
	}
}
