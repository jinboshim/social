package social.network.uptempo.batch.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class MedinaviMultiThreadBatchTask {
	
	boolean is_run = false;
	
	@Autowired 
	private MedinaviBatchTask1 medinaviTask1;
	@Autowired 
	private MedinaviBatchTask2 medinaviTask2;
	@Autowired 
	private MedinaviBatchTask3 medinaviTask3;
	@Autowired 
	private MedinaviBatchTask4 medinaviTask4;
	
	
	@Scheduled(initialDelay = 5000, fixedDelay=600000000) // 1min delay
	public void doTask() throws Exception {
		if( !is_run ) {
			medinaviTask1.doTask();
			medinaviTask2.doTask();
			medinaviTask3.doTask();
			medinaviTask4.doTask();
			is_run = true;
		}
		
	}

}
