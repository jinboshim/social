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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import social.network.uptempo.set.data.EData;


public class HTTPSClientConnection {
	
	private static Logger logger = LoggerFactory.getLogger(HTTPClientConnection.class);
	
	HttpsURLConnection urlConnection = null;
	private final int CONNECT_TIMEOUT = 10000;
	private OutputStream os = null;
	private InputStream is = null;
	
	private String UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36";
	
	public HTTPSClientConnection() {
		
	}
	
	public boolean connect(String fullNotiURL) throws MalformedURLException, IOException{
		//HTTP connect
		urlConnection = (HttpsURLConnection) new URL(fullNotiURL).openConnection();	
		urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("User-Agent", UserAgent);
		
		urlConnection.setHostnameVerifier(new HostnameVerifier(){        
			public boolean verify(String hostname, SSLSession session){  
				return true;  
			}  
		}); 
		
		os = urlConnection.getOutputStream();
		is = urlConnection.getInputStream();
		
		return true;
	}
	public boolean connect(String fullNotiURL ,String contentType ,HttpMethod reqMethod) 
			throws MalformedURLException, 
				IOException{
		//HTTP connect
		urlConnection = (HttpsURLConnection) new URL(fullNotiURL).openConnection();	
		urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
		
		urlConnection.setRequestProperty("Content-Type", contentType);
		urlConnection.setRequestProperty("User-Agent", UserAgent);
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.setRequestProperty("Connection", "keep-alive");
		
		urlConnection.setHostnameVerifier(new HostnameVerifier(){        
			public boolean verify(String hostname, SSLSession session){  
				return true;  
			}  
		}); 
		
		if( reqMethod == HttpMethod.POST) {
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			
			os = urlConnection.getOutputStream();
		}
		else if( reqMethod == HttpMethod.GET) {
			urlConnection.setRequestMethod("GET");
		}
		urlConnection.disconnect();
		  
		return true;
	}
	
	public boolean connect(String fullNotiURL ,String contentType ,HttpMethod reqMethod, String cookie) 
			throws MalformedURLException, 
			IOException{
		//HTTP connect
		urlConnection = (HttpsURLConnection) new URL(fullNotiURL).openConnection();	
		urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
		
		urlConnection.setRequestProperty("Content-Type", contentType);
		urlConnection.setRequestProperty("User-Agent", UserAgent);
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.setRequestProperty("Connection", "keep-alive");
		
		urlConnection.setHostnameVerifier(new HostnameVerifier(){        
			public boolean verify(String hostname, SSLSession session){  
				return true;  
			}  
		}); 
		
		if(cookie != null && !cookie.isEmpty()) {
			urlConnection.setRequestProperty("Cookie", cookie);
		}
		
		if( reqMethod == HttpMethod.POST) {
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			
			os = urlConnection.getOutputStream();
		}
		else if( reqMethod == HttpMethod.GET) {
			urlConnection.setRequestMethod("GET");
		}
		urlConnection.disconnect();
		
		return true;
	}
	
	public boolean connect(EData data) throws MalformedURLException, IOException{
		//HTTP connect
		urlConnection = (HttpsURLConnection) new URL(data.getString("fullNotiURL")).openConnection();	
		urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
		
		urlConnection.setRequestProperty("Content-Type", data.getString("contentType"));
		urlConnection.setRequestProperty("User-Agent", data.getString("UserAgent"));
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.setRequestProperty("Connection", "keep-alive");
		
		urlConnection.setHostnameVerifier(new HostnameVerifier(){        
			public boolean verify(String hostname, SSLSession session){  
				return true;  
			}  
		}); 
		
		urlConnection.setRequestProperty("Cookie", data.getString("cookie"));
		
		if( data.getString("reqMethod","POST") ) {
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			
			os = urlConnection.getOutputStream();
		}
		else if( data.getString("reqMethod","GET") ) {
			urlConnection.setRequestMethod("GET");
		}
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
	public boolean connectGet(String fullNotiURL, String contentType, String cookie)
			throws MalformedURLException,
			IOException {
		return connect(fullNotiURL, contentType, HttpMethod.GET, cookie);
	}
	
	public boolean connectPost(String fullNotiURL, String contentType)
			throws MalformedURLException,
			IOException {
		return connect(fullNotiURL, contentType, HttpMethod.POST);
	}
	public boolean connectPost(String fullNotiURL, String contentType, String cookie)
			throws MalformedURLException,
			IOException {
		return connect(fullNotiURL, contentType, HttpMethod.POST, cookie);
	}
	
	public void close(){   
		//HTTP close
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
	
	public List<String> readCookies() throws Exception {
		List<String> headerFields = urlConnection.getHeaderFields().get("Set-Cookie");
		return headerFields;
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
		// HTTP result
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
//				System.out.println("@@@@@@@ = "+ioe);
				logger.error(ioe.getMessage());
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
		// HTTP result
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