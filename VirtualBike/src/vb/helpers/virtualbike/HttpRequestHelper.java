package vb.helpers.virtualbike;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpRequestHelper {
	private HttpClient client = new DefaultHttpClient();
	private HttpUriRequest requestasp = null;

	public HttpRequestHelper(String url) {
		requestasp = new HttpPost(url);
	};

	public String execute() throws ClientProtocolException, IOException {

		HttpResponse response = client.execute(requestasp);
		String str = null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity responseEntity = response.getEntity();
			InputStream is = responseEntity.getContent();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
            byte[] data = new byte[4096];  
            int count = -1;  ;  
            while((count = is.read(data,0,4096)) != -1)  
            {
            	outStream.write(data, 0, count); 
            } 
            data = null;  
        	str = new String(outStream.toByteArray(),"utf-8");
		}
		return str;
	}
}
