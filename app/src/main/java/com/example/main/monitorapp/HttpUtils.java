package com.example.main.monitorapp;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.os.StrictMode;

@SuppressLint("NewApi")
public class HttpUtils {

	public static boolean sendGet() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
        try {  
            URL url = new URL("http://www.baidu.com/");  
            HttpURLConnection httpURLConnection = (HttpURLConnection) url  
                    .openConnection();  
            httpURLConnection.setRequestMethod("GET");  
            if (httpURLConnection.getResponseCode() == 200) {  
                return true;
            }  
            httpURLConnection.disconnect();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
           e.printStackTrace();
        }  
        return false;
	}
	
	public static String linkService() {
		String content = "";
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		//String baseUrl = "http://218.16.118.136:9000/g/sers/ext_status.jsp?check=true";
        String baseUrl = "http://211.154.156.80:9000/AXGServer/status.jsp";
		HttpGet getMethod = new HttpGet(baseUrl);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			try {
				HttpResponse response = httpClient.execute(getMethod);
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				if (result!="normal") {
					content = "页面正常";
				} else {
					content = "页面不正常";
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			content = "业务服务器故障";
	        return content;
		}
		return content;
	}
}
