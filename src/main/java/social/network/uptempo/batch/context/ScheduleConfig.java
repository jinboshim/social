package social.network.uptempo.batch.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer{

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
		
		threadPool.setPoolSize(5);
		threadPool.initialize();
		taskRegistrar.setTaskScheduler(threadPool);
	}
	

}
