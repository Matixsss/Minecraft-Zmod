package com.DystryktZ;

public class BlockInfo {
	private int category;
	private int xp;
	
	public BlockInfo(int category, int xp)
	{
		this.category = category;
		this.xp = xp;
	}
	
	public int getCategory()
	{
		return category;
	}
	
	public int getXp()
	{
		return xp;
	}
}
