package social.network.uptempo.set.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;


public class HTTPClientConnection {
	
	private static Logger logger = LoggerFactory.getLogger(HTTPClientConnection.class);
	
	HttpURLConnection urlConnection = null;
	private final int CONNECT_TIMEOUT = 3000;
	private OutputStream os = null;
	private InputStream is = null;
	
	public HTTPClientConnection() {
		
	}
	
	public boolean connect(String fullNotiURL) throws MalformedURLException, IOException{
		//HTTP �뿰寃�
		urlConnection = (HttpURLConnection) new URL(fullNotiURL).openConnection();	
		urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		os = urlConnection.getOutputStream();
		is = urlConnection.getInputStream();
		
		return true;
	}
	public boolean connect(String fullNotiURL ,String contentType ,HttpMethod reqMethod) 
			throws MalformedURLException, 
				IOException{
		//HTTP �뿰寃�
		urlConnection = (HttpURLConnection) new URL(fullNotiURL).openConnection();	
		urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
		
		urlConnection.setRequestProperty("Content-Type", contentType);
		urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.setRequestProperty("Connection", "keep-alive");

		
		if( reqMethod == HttpMethod.POST) {
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			
			os = urlConnection.getOutputStream();
		}
		else if( reqMethod == HttpMethod.GET) {
			urlConnection.setRequestMethod("GET");
		}
		
//		switch(reqMethod) {
//		case POST:
//			urlConnection.setDoOutput(true);
//			urlConnection.setRequestMethod("POST");
//
//			os = urlConnection.getOutputStream();
//			break;
//		case GET:
//			urlConnection.setRequestMethod("GET");
//			break;
//		default:
//			break;
//		}
		
//		is = urlConnection.getInputStream();
		urlConnection.disconnect();
		  
		return true;
	}
	
	/**
	 * 
	 * @param fullNotiURL
	 * @param contentType
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public boolean connectPost(String fullNotiURL, String contentType)
			throws MalformedURLException,
				IOException {
		return connect(fullNotiURL, contentType, HttpMethod.POST);
	}
	
	public void close(){   
		//HTTP �떕湲�
	   	try{
	   		if(is != null) is.close();        
	   		if(os != null) os.close();        
	        if(urlConnection != null) urlConnection.disconnect();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public  String readMessage() throws Exception {
		Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
		return headerFields.get("RESP").toString();
	}

	public  String readResult() throws Exception {
		if(is == null) {
			try {
				if(HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
					is = urlConnection.getInputStream();
				}
				else { 
					return null;
				}
				
			} catch (Exception e) {
				return null;
			}
		}
		
		StringBuffer httpResponse = new StringBuffer();
		BufferedReader httpBufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String inputLine = null;
		while((inputLine = httpBufferedReader.readLine()) != null) {
			httpResponse.append(inputLine);
		}
		String response = httpResponse.toString();
		// HTTP 硫붿꽭吏� �쟾�떖
		return response;
	}
	
	public String getContent(int contentsLength, InputStream is) throws IOException{
		byte tempByteBuffer[] = new byte[contentsLength];
		try{
			is.read(tempByteBuffer);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ioe) {
				System.out.println("@@@@@@@ = "+ioe);
//				logger.error(ioe.getMessage());
			}
		}
		return new String(tempByteBuffer).trim();
	}
	
	public boolean sendMessage(String strData) throws IOException{
		byte[] input = strData.getBytes("UTF-8");
		os.write(input, 0, input.length);
		os.flush();

		return true;
	}
	
	public  String readResultEuc() throws Exception {
		if(is == null)
			is = urlConnection.getInputStream();

		StringBuffer httpResponse = new StringBuffer();
		BufferedReader httpBufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String inputLine = null;
		while((inputLine = httpBufferedReader.readLine()) != null) {
			httpResponse.append(inputLine);
		}
		String response = httpResponse.toString();
		// HTTP 硫붿꽭吏� �븳湲� 泥섎━
		is = null;
		return response;
	}
	
	public int getResponseCode() {
		try {
			return urlConnection.getResponseCode();
		} catch (IOException ioe) {
			System.out.println("@@@@@@@ = "+ioe);
		}
		
		return -1;
	}
	
	public String getResponseMessage() {
		try {
			return urlConnection.getResponseMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}