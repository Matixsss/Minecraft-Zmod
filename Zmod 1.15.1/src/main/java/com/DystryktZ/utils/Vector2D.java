package com.DystryktZ.utils;

public class Vector2D<T, R>{
	private T x;
	private R y;
	
	public Vector2D(T x, R y)
	{
		this.x = x;
		this.y = y;
	}
	
	public T getX()
	{
		return x;
	}
	
	public R getY()
	{
		return y;
	}
	
	public void setX(T x)
	{
		this.x = x; 
	}
	
	public void setY(R y)
	{
		this.y = y; 
	}

}
