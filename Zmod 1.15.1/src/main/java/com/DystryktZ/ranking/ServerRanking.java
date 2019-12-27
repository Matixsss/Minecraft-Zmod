package com.DystryktZ.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.DystryktZ.ZmodJson;
import com.DystryktZ.Capability.IZStat;

import net.minecraft.nbt.CompoundNBT;

public class ServerRanking {
	private Map<String, IZStat> players_scores;
	private CompoundNBT server_rank; //best 5 players in every category (Compounded)

	public ServerRanking()
	{
		players_scores = ZmodJson.RankingloadFromJson();
	}
	
	public int getIndex(String name, int cat)
	{
	    for(int i=1;i<=3;i++)
	    {
	    	if(server_rank.contains(i+":"+cat+":"+name, 3))
			{
				return i;
			}
	    }	
		return -1;
	}
	
	public void addScore(String name, IZStat iz)
	{
		if(players_scores.containsKey(name))
		{
			players_scores.replace(name, iz);
		}
		else
		{
			players_scores.put(name, iz);
		}
		
	}
	
	public void GenerateServerRank()
	{
	server_rank = new CompoundNBT();
	Set<Map.Entry<String, IZStat>> entrySet = players_scores.entrySet();
	List<RankPacker> best_miners = new ArrayList<RankPacker>(); //0
	List<RankPacker> best_diggers = new ArrayList<RankPacker>(); //1
	List<RankPacker> best_combat = new ArrayList<RankPacker>(); //2
	List<RankPacker> best_cutters = new ArrayList<RankPacker>(); //3
	List<RankPacker> best_farmers = new ArrayList<RankPacker>(); //4
	List<RankPacker> best_builders = new ArrayList<RankPacker>(); //5
	
	for(Map.Entry<String, IZStat> entry : entrySet)
	{
		//best miners
		best_miners.add(new RankPacker(entry.getKey(),entry.getValue().get_mining_xp()));
		best_diggers.add(new RankPacker(entry.getKey(),entry.getValue().get_digging_xp()));
		best_combat.add(new RankPacker(entry.getKey(),entry.getValue().get_combat_xp()));
		best_cutters.add(new RankPacker(entry.getKey(),entry.getValue().get_cutting_xp()));
		best_farmers.add(new RankPacker(entry.getKey(),entry.getValue().get_farm_xp()));
		best_builders.add(new RankPacker(entry.getKey(),entry.getValue().get_building_xp()));
	}
	
	Collections.sort(best_miners);
	Collections.sort(best_diggers);
	Collections.sort(best_combat);
	Collections.sort(best_cutters);
	Collections.sort(best_farmers);
	Collections.sort(best_builders);
	
	for(int i=0;i<5;i++)
	{
		if(i<best_miners.size())
		{
			server_rank.putInt((i+1)+":0:"+best_miners.get(i).getName(), best_miners.get(i).getScore());
		}
		
		if(i<best_diggers.size())
		{
			server_rank.putInt((i+1)+":1:"+best_diggers.get(i).getName(), best_diggers.get(i).getScore());
		}
		
		if(i<best_combat.size())
		{
			server_rank.putInt((i+1)+":2:"+best_combat.get(i).getName(), best_combat.get(i).getScore());
		}
		
		if(i<best_cutters.size())
		{
			server_rank.putInt((i+1)+":3:"+best_cutters.get(i).getName(), best_cutters.get(i).getScore());
		}
		
		if(i<best_farmers.size())
		{
			server_rank.putInt((i+1)+":4:"+best_farmers.get(i).getName(), best_farmers.get(i).getScore());
		}
		
		if(i<best_builders.size())
		{
			server_rank.putInt((i+1)+":5:"+best_builders.get(i).getName(), best_builders.get(i).getScore());
		}
	}
	
	}
	
	public void saveToJson()
	{
		ZmodJson.saveServerRanking(players_scores);
	}
	
	public CompoundNBT getRank()
	{
		if(server_rank != null)
		{
		return server_rank;
		}
		return null;
		
	}
	

}
