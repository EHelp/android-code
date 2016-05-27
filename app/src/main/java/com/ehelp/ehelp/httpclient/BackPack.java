package com.ehelp.ehelp.httpclient;

public class BackPack {

	private String body = null;
	private String cookie = null;
	
	public BackPack()
	{
		
	}
	
	public BackPack(String body) 
	{
		this.body = body;
	}
	
	public BackPack(String body, String cookie)
	{
		this.body = body;
		this.cookie = cookie;
	}

	public String getBody()
	{
		return this.body;
	}
	
	public String getCookie() 
	{
		return this.cookie;
	}
	
	public void setBody(String body)
	{
		this.body = body;
	}
	
	public void setCookie(String cookie)
	{
		this.cookie = cookie;
		if (this.cookie != null)
		{
			this.cookie = this.cookie.split(";")[0];
		}
	}
}
