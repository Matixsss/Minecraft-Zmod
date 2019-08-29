package com.DystryktZ;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.DystryktZ.Capability.IZStat;
import com.DystryktZ.Capability.ZStat;
import com.DystryktZ.utils.LotteryPack;
import com.DystryktZ.utils.MyStringBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ZmodJson {
	
	private static String path;
	
	public static Set<String> c_show_block_info = new HashSet<String>();
	public static Set<String> c_show_entity_info = new HashSet<String>();
	public static Map<String, BlockInfo> block_map = new HashMap<String, BlockInfo>();
	public static Set<String> mining_lottery = new HashSet<String>();
	public static Map<String, Integer> combat_exp_table = new HashMap<String, Integer>();
	
	public static int combat_lottery_sum = 0;
	public static ArrayList<LotteryPack> combat_lottery = new ArrayList<LotteryPack>();
	public static int digging_lottery_sum = 0;
	public static ArrayList<LotteryPack> digging_lottery = new ArrayList<LotteryPack>();
	
	public static CompoundNBT server_configs;
	public static double[] RankingBonusSettings = new double[3];
	static {
		RankingBonusSettings[0] = 1.2;
		RankingBonusSettings[1] = 1.1;
		RankingBonusSettings[2] = 1.05;
	}

	public static void setPath(String p)
	{
		path = p;
	}
	
	public static void SaveBlockExpJson(String path)
	{	 
		 Gson gson = new Gson();
		 JsonObject mining = new JsonObject();
		 JsonObject miningDetail = new JsonObject();
		 miningDetail.addProperty("stone", 1);
		 miningDetail.addProperty("granite", 1);
		 miningDetail.addProperty("andesite", 1);
		 miningDetail.addProperty("diorite", 1);
		 miningDetail.addProperty("coal_ore", 3);
		 miningDetail.addProperty("iron_ore", 4);	 
		 miningDetail.addProperty("lapis_ore", 7);
		 miningDetail.addProperty("redstone_ore", 7);
		 miningDetail.addProperty("gold_ore", 5);
		 miningDetail.addProperty("diamond_ore", 30);
		 miningDetail.addProperty("emerald_ore", 50);
		 miningDetail.addProperty("nether_quartz_ore", 5); 
		 mining.add("Mining", miningDetail);
		 
		 JsonObject digging = new JsonObject();
		 JsonObject diggingDetail = new JsonObject();
		 diggingDetail.addProperty("dirt", 1);
		 diggingDetail.addProperty("farmland", 1);
		 diggingDetail.addProperty("grass_block", 1);
		 diggingDetail.addProperty("sand", 1);
		 diggingDetail.addProperty("gravel", 1);
		 diggingDetail.addProperty("clay", 1);
		 diggingDetail.addProperty("podzol", 1);
		 diggingDetail.addProperty("coarse_dirt", 1);
		 diggingDetail.addProperty("red_sand", 1);
		 diggingDetail.addProperty("soul_sand", 2);
		 digging.add("Digging",  diggingDetail);
		 
		 JsonObject woodcutting = new JsonObject();
		 JsonObject woodcuttingDetail = new JsonObject();
		 woodcuttingDetail.addProperty("wood", 4);
		 woodcutting.add("Woodcutting",  woodcuttingDetail);
		 
		 JsonObject farming = new JsonObject();
		 JsonObject farmingDetail = new JsonObject();
		 farmingDetail.addProperty("wheat", 3);
		 farmingDetail.addProperty("melon", 8);
		 farmingDetail.addProperty("beetroots", 4);
		 farmingDetail.addProperty("carrots", 4);
		 farmingDetail.addProperty("potatoes", 4);
		 farming.add("Farming",  farmingDetail);
		 
		 JsonArray j_array = new JsonArray();
		 j_array.add(mining);		
		 j_array.add(digging);
		 j_array.add(woodcutting);
		 j_array.add(farming);
		 
		 try(FileWriter f = new FileWriter(path))
		 {
			f.write(gson.toJson(j_array));
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
		 
	}
	
	public static void SaveCombatExpJson(String path)
	{
		 Gson gson = new Gson();
		 JsonObject monster = new JsonObject();
		 //aggresive
		 monster.addProperty("skeleton", 10);
		 monster.addProperty("blaze", 15);
		 monster.addProperty("zombie", 5);
		 monster.addProperty("spider", 7);
		 monster.addProperty("slime", 3);
		 monster.addProperty("cave_spider", 12);
		 monster.addProperty("zombie_pigman", 5);
		 monster.addProperty("zombie_villager", 5);
		 monster.addProperty("wither_skeleton", 10);
		 monster.addProperty("enderman", 30);
		 monster.addProperty("magma_cube", 3);
		 monster.addProperty("wither", 300);
		 monster.addProperty("witch", 15);
		 monster.addProperty("creeper", 10);
		 monster.addProperty("phantom", 10);	 
		 monster.addProperty("guardian", 15);
		 monster.addProperty("ghast", 30);
		 monster.addProperty("evoker", 50);
		 monster.addProperty("stray", 15);
		 monster.addProperty("drowned", 5);
		 monster.addProperty("endermite", 5);
		 monster.addProperty("vex", 15);
		 monster.addProperty("husk", 10);
		 monster.addProperty("shulker", 40);
		 monster.addProperty("zombie_horse", 10);
		 monster.addProperty("skeleton_horse", 10);
		 monster.addProperty("elder_guardian", 200);
		 monster.addProperty("ravager", 20);
		 monster.addProperty("player", 2);
			 
		 //dodaj moby
		 //neutral
		monster.addProperty("pig", 1);
		monster.addProperty("cow", 1);
		monster.addProperty("sheep", 1);
		monster.addProperty("chicken", 1);
		monster.addProperty("horse", 1);
		monster.addProperty("mule", 1);
		monster.addProperty("rabbit", 1);
		monster.addProperty("squid", 1);
		monster.addProperty("bat", 1);
		monster.addProperty("villager", 1);
		monster.addProperty("parrot", 1);
		monster.addProperty("panda", 1);
		monster.addProperty("turtle", 1);
		monster.addProperty("ocelot", 1);
		monster.addProperty("llama", 1);
		monster.addProperty("polar_bear", 3);
		monster.addProperty("wolf", 2);
		monster.addProperty("vindicator", 5);	
		
		 try(FileWriter f = new FileWriter(path))
		 {
			f.write(gson.toJson(monster));
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
		
	}
	
	public static void SaveCombatLotteryJson(String path)
	{
		Gson gson = new Gson();
		JsonObject lottery = new JsonObject();
		lottery.addProperty("bone",500);
		lottery.addProperty("arrow",400);
		lottery.addProperty("leather",300);
		lottery.addProperty("gunpowder",200);
		lottery.addProperty("melon_slice",300);
		lottery.addProperty("carrot",300);
		lottery.addProperty("potato",300);
		lottery.addProperty("diamond",1);
		
		 try(FileWriter f = new FileWriter(path))
		 {
			f.write(gson.toJson(lottery));
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }		 
	}
	
	public static void SaveDiggingLotteryJson(String path)
	{
		Gson gson = new Gson();
		JsonObject digging = new JsonObject();
		digging.addProperty("diamond",15);
		digging.addProperty("gold_ingot",50);
		digging.addProperty("iron_ingot",100);
		digging.addProperty("coal",400);
		digging.addProperty("glowstone_dust",300);
		digging.addProperty("redstone",200);
		
		
		 try(FileWriter f = new FileWriter(path))
		 {
			f.write(gson.toJson(digging));
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }		 
	}
	
	public static void SaveBonusChartsJson(String path)
	{
		Gson gson = new Gson();
		JsonObject bonus = new JsonObject();
		bonus.addProperty("break_bonus", 0.004F);
		bonus.addProperty("mining_bonus", 1F);
		bonus.addProperty("woodcutting_bonus", 1F);
		bonus.addProperty("digging_bonus", 0.2F);
		bonus.addProperty("combat_bonus", 1F);
		bonus.addProperty("farming_bonus", 1F);
		
		 try(FileWriter f = new FileWriter(path))
		 {
			f.write(gson.toJson(bonus));
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	private static void SaveMiningLottery(String path)
	{
	    MyStringBuilder ores = new MyStringBuilder();
		ores.append("coal_ore");
		ores.append("lapis_ore");
		ores.append("redstone ore");
		ores.append("gold_ore");
		ores.append("diamond_ore");
		ores.append("emerald_ore");
		ores.append("iron_ore");
		ores.append("redstone_ore");
		ores.append("nether_quartz_ore");
		
		 try(FileWriter f = new FileWriter(path))
		 {
			f.write(ores.toString());
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	private static void LoadMiningLottery()
	{
		if(!Files.exists(Paths.get(path+File.separator+"MiningLotterySettings.json")))
    	{
			SaveMiningLottery(path+File.separator+"MiningLotterySettings.json");
    	}
		mining_lottery.clear();
		
		StringBuilder ores = new StringBuilder();
		int i;
		try (Reader reader = new FileReader(path+File.separator+"MiningLotterySettings.json")) {
			while((i = reader.read()) != -1)
			{
				if((char)i == ';')
				{
					mining_lottery.add(ores.toString());
					//System.out.println(ores.toString());
					ores = new StringBuilder();
				}
				else
				{
				ores.append((char)i);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
	}
	
	public static void LoadJson()
	{
		LoadMiningLottery();
		LoadBlockExpSettings();
		LoadCombatExpSettings();
		LoadCombatLotterySettings();
		LoadBonusChartsSettings();
		LoadDiggingLotterySettings();
		LoadConfigFile();
	}
	
	
	private static void LoadConfigFile()
	{
		if(!Files.exists(Paths.get(path+File.separator+"Config.json")))
    	{
			SaveConfigFile(path+File.separator+"Config.json");
    	}
		
		 try(BufferedReader f = new BufferedReader(new FileReader(path+File.separator+"Config.json")))
		 {
			 String s;
			 while((s=f.readLine()) != null)
			 {
				 if(s.substring(0,1).equals("#"))
				 {
					 continue;
				 }
				 else
				 {
					 if(s.contains("save-intervals"))
					 {
						 int index = s.indexOf(":")+1;
						 try {
						 ZEventHandlerServer.setTimer(Long.parseLong(s.substring(index,s.length()))*1000);
						 }
						 catch(Exception ex)
						 {
							 ex.printStackTrace();
						 }
						 continue;
					 }
				
					 if(s.contains("bonus"))
					 {
						 int index = s.indexOf(":")+1;
						 String sub =s.substring(index,s.length());
						 String[] temp = sub.split(";");
						 int v=0;
						 for(String t : temp)
						 {
							 try {
						 RankingBonusSettings[v] = Double.parseDouble(t);			 
							 }
							 catch(Exception ex)
							 {
								 ex.printStackTrace();
							 }
							 v++;
						 }
						 continue;
					 }
				 }

			 }
			 
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	private static void SaveConfigFile(String path)
	{
		String[] lines = new String[5];
		lines[0] = "#Dedicated server settings"+System.lineSeparator();
		lines[1] = "#Saving ranking in time intervals(seconds)"+System.lineSeparator();
		lines[2] = "save-intervals:100"+System.lineSeparator();
		lines[3] = "#Luck multiply for three best players"+System.lineSeparator();
		lines[4] = "bonus:1.2;1.1;1.05";
	
		 try(FileWriter f = new FileWriter(path))
		 {
			for(String l : lines)
			{
				f.write(l);
			}
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	private static void LoadCombatLotterySettings()	
	{
		if(!Files.exists(Paths.get(path+File.separator+"CombatLotterySettings.json")))
    	{
			SaveCombatLotteryJson(path+File.separator+"CombatLotterySettings.json"); //create default file
    	}
		combat_lottery.clear();
		combat_lottery_sum=0;
		Gson gson = new GsonBuilder().setLenient().create();
		try (Reader reader = new FileReader(path+File.separator+"CombatLotterySettings.json")) {
			JsonObject jo = new JsonObject();
			jo = gson.fromJson(reader, JsonObject.class);
			Set<Map.Entry<String, JsonElement>> entrySet = jo.entrySet();
			for(Map.Entry<String, JsonElement> entry : entrySet)
			{
				ResourceLocation res = new ResourceLocation(entry.getKey().toLowerCase());
				if(ForgeRegistries.ITEMS.containsKey(res))
				{
					Item itemek = (Item) ForgeRegistries.ITEMS.getValue(res);
					LotteryPack newPack = new LotteryPack(itemek, entry.getValue().getAsInt());
				    combat_lottery.add(newPack);
				    combat_lottery_sum += entry.getValue().getAsInt();
				}		
				
			}
			Collections.sort(combat_lottery);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
	}
	
	private static void LoadDiggingLotterySettings()	
	{
		if(!Files.exists(Paths.get(path+File.separator+"DiggingLotterySettings.json")))
    	{
			SaveDiggingLotteryJson(path+File.separator+"DiggingLotterySettings.json");
    	}
		digging_lottery.clear();
		digging_lottery_sum=0;
		Gson gson = new GsonBuilder().setLenient().create();
		try (Reader reader = new FileReader(path+File.separator+"DiggingLotterySettings.json")) {
			JsonObject jo = new JsonObject();
			jo = gson.fromJson(reader, JsonObject.class);
			Set<Map.Entry<String, JsonElement>> entrySet = jo.entrySet();
			for(Map.Entry<String, JsonElement> entry : entrySet)
			{
				ResourceLocation res = new ResourceLocation(entry.getKey().toLowerCase());
				if(ForgeRegistries.ITEMS.containsKey(res))
				{
					Item itemek = (Item) ForgeRegistries.ITEMS.getValue(res);
					LotteryPack newPack = new LotteryPack(itemek, entry.getValue().getAsInt());
					digging_lottery.add(newPack);
					digging_lottery_sum += entry.getValue().getAsInt();
				}			
			}
			Collections.sort(digging_lottery);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
	}
	
	private static void LoadBonusChartsSettings()
	{
		if(!Files.exists(Paths.get(path+File.separator+"BonusChartsSettings.json")))
    	{
			SaveBonusChartsJson(path+File.separator+"BonusChartsSettings.json");
    	}
		server_configs = new CompoundNBT();
		Gson gson = new GsonBuilder().setLenient().create();
		try (Reader reader = new FileReader(path+File.separator+"BonusChartsSettings.json")) {
			JsonObject jo = new JsonObject();
			jo = gson.fromJson(reader, JsonObject.class);
			Set<Map.Entry<String, JsonElement>> entrySet = jo.entrySet();
			for(Map.Entry<String, JsonElement> entry : entrySet)
			{
				switch(entry.getKey())
				{
				case "break_bonus":
					server_configs.putDouble("break_bonus", entry.getValue().getAsDouble());
					break;
				
				case "mining_bonus":
					server_configs.putDouble("mining_bonus", entry.getValue().getAsDouble());
					break;
					
				case "woodcutting_bonus":
					server_configs.putDouble("woodcutting_bonus", entry.getValue().getAsDouble());
					break;
					
				case "digging_bonus":
					server_configs.putDouble("digging_bonus", entry.getValue().getAsDouble());
					break;
					
				case "combat_bonus":
					server_configs.putDouble("combat_bonus", entry.getValue().getAsDouble());
					break;
					
				case "farming_bonus":
					server_configs.putDouble("farming_bonus", entry.getValue().getAsDouble());
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
	}
	
	private static void LoadBlockExpSettings()
	{		
		if(!Files.exists(Paths.get(path+File.separator+"BlockExpSettings.json")))
    	{
			SaveBlockExpJson(path+File.separator+"BlockExpSettings.json");
    	}
		block_map.clear();
		Gson gson = new GsonBuilder().setLenient().create();
		int cat=0;
		try (Reader reader = new FileReader(path+File.separator+"BlockExpSettings.json")) {
			JsonArray ja = new JsonArray();
			ja = gson.fromJson(reader, JsonArray.class);
			for(JsonElement je : ja)
			{
				Set<Map.Entry<String, JsonElement>> entrySet = je.getAsJsonObject().entrySet();
				for(Map.Entry<String, JsonElement> entry : entrySet)
				{
					System.out.println("Loading: "+entry.getKey());
					if(entry.getKey().equals("Mining")) { cat=0; }
					if(entry.getKey().equals("Digging")) { cat=2;}
					if(entry.getKey().equals("Woodcutting")) { cat=3;}
					if(entry.getKey().equals("Farming")) { cat=4;}		
						Set<Map.Entry<String, JsonElement>> next = entry.getValue().getAsJsonObject().entrySet();
						for(Map.Entry<String, JsonElement> e : next)
						{							
							block_map.put(e.getKey(), new BlockInfo(cat,e.getValue().getAsInt()));
						}						
				}
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
	}
	
	private static void LoadCombatExpSettings()
	{		
		if(!Files.exists(Paths.get(path+File.separator+"CombatExpSettings.json")))
    	{
			SaveCombatExpJson(path+File.separator+"CombatExpSettings.json");
    	}
		combat_exp_table.clear();
		Gson gson = new GsonBuilder().setLenient().create();
		try (Reader reader = new FileReader(path+File.separator+"CombatExpSettings.json")) {
			JsonObject jo = new JsonObject();
			jo = gson.fromJson(reader, JsonObject.class);
			Set<Map.Entry<String, JsonElement>> entrySet = jo.entrySet();
			for(Map.Entry<String, JsonElement> entry : entrySet)
			{
				combat_exp_table.put(entry.getKey(),entry.getValue().getAsInt());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
	}
	
	public static void saveServerRanking(Map<String, IZStat> map)
	{
		Gson gson = new Gson();
		JsonArray j_array = new JsonArray(); //lista graczy
		Set<Map.Entry<String, IZStat>> map_entry = map.entrySet();
		for(Map.Entry<String, IZStat> entry : map_entry)
		{
			JsonObject player = new JsonObject();
			 JsonObject playerDetail = new JsonObject();
			 playerDetail.addProperty("Mining", entry.getValue().get_mining_xp());
			 playerDetail.addProperty("Digging", entry.getValue().get_digging_xp());
			 playerDetail.addProperty("Woodcutting", entry.getValue().get_cutting_xp());
			 playerDetail.addProperty("Combat", entry.getValue().get_combat_xp());
			 playerDetail.addProperty("Farming", entry.getValue().get_farm_xp());
			 playerDetail.addProperty("Building", entry.getValue().get_building_xp());
			 player.add(entry.getKey(), playerDetail);						
			 j_array.add(player); //dodaj gracza do listy
		}
		
 
		 try(FileWriter f = new FileWriter(path+File.separator+"ServerRanking.json"))
		 {
			f.write(gson.toJson(j_array));
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	public static Map<String, IZStat> RankingloadFromJson()
	{
		Map<String, IZStat> map = new HashMap<String, IZStat>();
		Path fp = Paths.get(path+File.separator+"ServerRanking.json");
		if(!Files.exists(fp))
    	{
			try {
				Files.createFile(fp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		else
		{		
		Gson gson = new GsonBuilder().setLenient().create();
		
		try (Reader reader = new FileReader(path+File.separator+"ServerRanking.json")) {
			String name;
			JsonArray ja = new JsonArray();
			ja = gson.fromJson(reader, JsonArray.class);
			for(JsonElement je : ja)
			{
				Set<Map.Entry<String, JsonElement>> entrySet = je.getAsJsonObject().entrySet();
				for(Map.Entry<String, JsonElement> entry : entrySet)
				{
						name = entry.getKey();
						IZStat zs = new ZStat();
						
						Set<Map.Entry<String, JsonElement>> next = entry.getValue().getAsJsonObject().entrySet();
						for(Map.Entry<String, JsonElement> e : next) //dodawanie parametrow
						{							
							switch(e.getKey())
							{
							case "Mining":
								zs.set_mining_xp(e.getValue().getAsInt());
								break;
								
							case "Digging":
								zs.set_digging_xp(e.getValue().getAsInt());
								break;
								
							case "Woodcutting":
								zs.set_cutting_xp(e.getValue().getAsInt());
								break;
								
							case "Combat":
								zs.set_combat_xp(e.getValue().getAsInt());
								break;
								
							case "Farming":
								zs.set_farm_xp(e.getValue().getAsInt());
								break;
								
							case "Building":
								zs.set_building_xp(e.getValue().getAsInt());
								break;
							}
						}
						
						map.put(name, zs);			
				}
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		}
		return map;
	}
	
	 public static int expToLevel(int exp)
	 {
		 if(exp < 60) { return 0;}
		 return (int)(Math.sqrt((double)(exp/10) + 6.25D) - (50D/20D));
	 }
	 
	 public static int levelToExp(int level)
	 {
		 return (int) (10*Math.pow(level, 2) + 50*level);
	 }
	

}
