package com.DystryktZ.ranking;

public class RankPacker implements Comparable<RankPacker>  {
	private String name;
	private int score;
	private int rank;
	
	public RankPacker(String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	public RankPacker(String name, int rank, int score)
	{
		this.name = name;
		this.score = score;
		this.rank = rank;
	}
	
	@Override
	public int compareTo(RankPacker o) {
		// TODO Auto-generated method stub
		return  o.score - this.score;
	}
	
	public String getName()
	{
		return name;					
	}
	
	public int getScore()
	{
		return score;
	}
	
	public int getRank()
	{
		return rank;
	}
}
