package com.DystryktZ.utils;

public class MyStringBuilder {
	
	private StringBuilder builder;
	
	public MyStringBuilder()
	{
		builder = new StringBuilder();
	}
	
	public void append(String s)
	{
		builder.append(s);
		builder.append(";");
	}
	
	public String toString()
	{
		return builder.toString();
	}

}
