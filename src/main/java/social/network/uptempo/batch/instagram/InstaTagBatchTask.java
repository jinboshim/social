package social.network.uptempo.batch.instagram;

public class InstaTagBatchTask {

	
	String url="http://www.instagram.com/explore/tags/{word}/";
	
	String[] words = {""};
	
	boolean is_run = false;
	
	public void doTask() throws Exception {
		if(!is_run) {
			is_run = true;
			
			for(String word : words) {
				
			}
			
			
			
			
			
			
			
			is_run = false;
		}
	}
	
	
}
