package com.ehelp.ehelp.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class HttpHelper extends AsyncTask<Void, Void, String>{
	
	public final static int HTTP_GET = 0;
	public final static int HTTP_POST = 1;
	public final static String ENCODING="UTF-8";
	
	private String urlString = null;
	private JSONObject body = null;
	private IResponse mIResponse = null;
	private int httpType = HTTP_GET;
	private String cookie = null;
	private BackPack backpack = null;
	
	public HttpHelper(String urlString, JSONObject body, IResponse mIResponse, int httpType, String cookie) {
		this.urlString = urlString;
		this.body = body;
		this.mIResponse = mIResponse;
		this.httpType = httpType;
		this.backpack = new BackPack();
	}
	
	private void http_get()
	{
		HttpURLConnection conn = null; 
		
		// 拼接参数
		String params = "";
		if (this.body != null)
		{
			Iterator iterator = this.body.keys();
			while(iterator.hasNext())
			{
				String key = iterator.next().toString();
				String value = null;
				try {
					value = this.body.getString(key);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (!params.equals(""))
				{
					params += "&";
				}
				try {
					params += (key + "=" + URLEncoder.encode(value, ENCODING));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!params.equals(""))
			{
				this.urlString += ("?" + params);
			}
		}
		
		try {
			conn = (HttpURLConnection) new URL(this.urlString).openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        
        conn.setRequestProperty("Connection", "Keep-Alive");  
        conn.setRequestProperty("Charset", ENCODING);  
        conn.setRequestProperty("Content-Type",  "application/json");
        if (this.cookie != null)
        	conn.setRequestProperty("Cookie", this.cookie);
        
        try {
			receive_response(conn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void http_post()
	{
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(this.urlString).openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        
        conn.setRequestProperty("Connection", "Keep-Alive");  
        conn.setRequestProperty("Charset", "UTF-8");  
        conn.setRequestProperty("Content-Type",  "application/json");
        if (this.cookie != null)
        	conn.setRequestProperty("Cookie", this.cookie);
        
    	try {
    		OutputStream out = conn.getOutputStream();
			out.write(this.body.toString().getBytes(ENCODING));
        	out.flush();
	        out.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
          
        try {
			receive_response(conn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
         
	private void receive_response(HttpURLConnection conn) throws IOException
	{
		if (conn.getResponseCode() == HttpStatusCode.HTTP_OK) 
		{  
            InputStream is = conn.getInputStream();   
            ByteArrayOutputStream baos = new ByteArrayOutputStream();   
            int len = 0;   
            byte buffer[] = new byte[1024];  
            while ((len = is.read(buffer)) != -1) 
            {  
                baos.write(buffer, 0, len);  
            }  
            is.close();
            baos.close();
            this.backpack.setBody(new String(baos.toByteArray()));
            this.backpack.setCookie(conn.getHeaderField("Cookie"));
		}
		else 
		{
			this.backpack.setBody(null);
			this.backpack.setBody(null);
		}
	}
     

	@Override
	protected String doInBackground(Void... params) {
		if (this.httpType == HTTP_GET)
			http_get();
		else if (this.httpType == HTTP_POST)
			http_post();
		return null;
	}
	
	@Override  
	protected void onPostExecute(String result) {  
		super.onPostExecute(result);  
		this.mIResponse.onResponse(this.backpack);
	}  
	

}
