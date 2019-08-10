package com.DystryktZ;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DystryktZ.Capability.IZStat;
import com.DystryktZ.Capability.ZStatController;
import com.DystryktZ.Network.ServerPacket_ServerInfo;
import com.DystryktZ.Network.ZModPacketHandler;
import com.DystryktZ.utils.LotteryPack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.MelonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class ZEventHandler {
	
	private Random r = new Random();
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event)
	{
	PlayerEntity player = event.getEntityPlayer();
	if(player.world.isRemote) { return; }
	PlayerEntity old =  event.getOriginal();
	old.revive();
	
	LazyOptional<IZStat> lo = old.getCapability(ZStatController.ZStat_CAP);
	IZStat old_zs = lo.orElse(null);
    if(old_zs != null)
    {
    	
    	LazyOptional<IZStat> znew = player.getCapability(ZStatController.ZStat_CAP);
    	IZStat zs = znew.orElse(null);
    	if(zs!=null)
    	{
    		zs.set_mining_xp(old_zs.get_mining_xp());
    		zs.set_combat_xp(old_zs.get_combat_xp());
    		zs.set_digging_xp(old_zs.get_digging_xp());
    		zs.set_cutting_xp(old_zs.get_cutting_xp());
    		zs.set_farm_xp(old_zs.get_farm_xp());
    		zs.set_building_xp(old_zs.get_building_xp());   		
    	}    
    }
	}
	
	
	@SubscribeEvent
	public void OnPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event)
	{
		ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
		if(!player.world.isRemote)
		{
			player.getCapability(ZStatController.ZStat_CAP).ifPresent(c -> c.Sync(player));
		}
	}
	
	@SubscribeEvent
	public void OnrespawnEvent(PlayerRespawnEvent evt)
	{
		if(!evt.getPlayer().world.isRemote)
		{
		evt.getPlayer().getCapability(ZStatController.ZStat_CAP).ifPresent(c -> c.Sync((ServerPlayerEntity) evt.getPlayer()));
		}
	}
	

	@SubscribeEvent
	public void playerConnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		if(!player.world.isRemote)
		{
		player.getCapability(ZStatController.ZStat_CAP).ifPresent(c -> c.Sync(player));		
		//send server info
		ZModPacketHandler.sendTo(new ServerPacket_ServerInfo(ZmodJson.server_configs), player);
    	}
	}	
	
	
	@SubscribeEvent
	public void onPlayerKillEntity(LivingDeathEvent event)
	{
		if(event.getSource().getTrueSource() instanceof PlayerEntity)
		{
		PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
		if(player.world.isRemote || player.isCreative()) { return; }
		LazyOptional<IZStat> lo = player.getCapability(ZStatController.ZStat_CAP);
		IZStat zs = lo.orElse(null);
		if(zs==null) { return; }
		String mob_name=event.getEntityLiving().getEntityString();
		if(event.getEntityLiving() instanceof ServerPlayerEntity)
		{
			mob_name = "player";
		}
		else
		if(mob_name.length() >=9)
		{
		if(mob_name.substring(0,9).equals("minecraft"))
		{
			mob_name = mob_name.substring(10,mob_name.length());
		}
		}
		//player.sendMessage(new StringTextComponent(mob_name)); //for tests

	    if(ZmodJson.combat_exp_table.containsKey(mob_name))
		{
	    KillLotteryBonus(ZmodJson.expToLevel(zs.get_combat_xp())*ZmodJson.server_configs.getDouble("combat_bonus")*getPlayerRankingBonus(player, 2), (PlayerEntity) event.getSource().getTrueSource(), event.getEntityLiving());	
		zs.add_combat_xp(ZmodJson.combat_exp_table.get(mob_name));
		zs.Sync((ServerPlayerEntity) player);
		UpdateRanking((ServerPlayerEntity)player, zs);
		}
		
		
		}
	}
	
	private double getPlayerRankingBonus(PlayerEntity player, int category)
	{
		if(ZEventHandlerServer.ranking != null)
		{
			int index = ZEventHandlerServer.ranking.getIndex(player.getName().getUnformattedComponentText(),category);
			switch(index)
			{
			case 1:
			return ZmodJson.RankingBonusSettings[0];
			case 2:
			return ZmodJson.RankingBonusSettings[1];
			case 3:
			return ZmodJson.RankingBonusSettings[2];
			}
		}
		return 1.0;
	}
	
	private void KillLotteryBonus(double chance, PlayerEntity player, Entity target)
	{
		int bonus = (int) Math.floor((chance/100.0));
		double losu = r.nextDouble()*100.0+0.001;
		if(chance-(100*bonus) >= losu)
		{
			bonus++;
		}
		if(bonus == 0 || ZmodJson.combat_lottery.size()<1){ return;}
		
		int next_dice = r.nextInt(ZmodJson.combat_lottery_sum)+1; //1-sum
		int actual_chance = 0;
		ArrayList<LotteryPack> combat_lottery = ZmodJson.combat_lottery;
		for(int i=0;i<combat_lottery.size();i++)
		{
			if(actual_chance < next_dice && next_dice <= combat_lottery.get(i).getY()+actual_chance)
			{
				ItemStack is = new ItemStack(combat_lottery.get(i).getX());
				is.setCount(bonus);
				player.world.addEntity(new ItemEntity(player.world, target.posX, target.posY, target.posZ, is));	
				player.sendMessage(new StringTextComponent("Combat bonus: "+bonus+" "+is.getDisplayName().getUnformattedComponentText()));
				break;
			}
			actual_chance += combat_lottery.get(i).getY();
		}
	}
	
	private void DiggingLotteryBonus(double chance, PlayerEntity player, BlockPos bp)
	{
		int bonus = (int) Math.floor((chance/100.0));
		double losu = r.nextDouble()*100.0+0.001;
		//player.sendMessage(new StringTextComponent("chance:"+String.format("%.4f", chance)+" losu:"+String.format("%.4f",losu)+" bonus:" + bonus)); //test
		if(chance-(100*bonus) >= losu)
		{
			bonus++;
		}
		if(bonus == 0 || ZmodJson.digging_lottery.size()<=0){ return;}
		
		int next_dice = r.nextInt(ZmodJson.digging_lottery_sum)+1;
		int actual_chance = 0;
		ArrayList<LotteryPack> digging_lottery = ZmodJson.digging_lottery;
		for(int i=0;i<digging_lottery.size();i++)
		{
			if(actual_chance < next_dice && next_dice <= digging_lottery.get(i).getY()+actual_chance)
			{
				ItemStack is = new ItemStack(digging_lottery.get(i).getX());
				is.setCount(bonus);
				player.world.addEntity(new ItemEntity(player.world, bp.getX(), bp.getY(), bp.getZ(), is));	
				player.sendMessage(new StringTextComponent("Digging bonus: "+bonus+" "+is.getDisplayName().getUnformattedComponentText()));
				break;
			}
			actual_chance += digging_lottery.get(i).getY();
		}
	}
	
	
	@SubscribeEvent
	public void onPlayerPlaceBlock(BlockEvent.EntityPlaceEvent event)
	{
		Block b = event.getPlacedBlock().getBlock();
		String s = b.getTranslationKey().substring(16);
		if(event.getEntity() instanceof PlayerEntity)
		{
		PlayerEntity player = (PlayerEntity) event.getEntity();
		if(player.world.isRemote || player.isCreative()) { return; }
		LazyOptional<IZStat> lo = player.getCapability(ZStatController.ZStat_CAP);
		IZStat zs = lo.orElse(null);
		if(zs==null) { return; }
		
		if(ZmodJson.mining_lottery.contains(s))
		{
			zs.sub_mining_xp(ZmodJson.block_map.get(s).getXp());
		}
		
		if(b instanceof MelonBlock)
		{
			
			zs.sub_farming_xp(ZmodJson.block_map.get(s).getXp());
		}
				
		zs.add_building_xp(1);	
		if(player instanceof ServerPlayerEntity)
		{
			zs.Sync((ServerPlayerEntity) player);
			UpdateRanking((ServerPlayerEntity)player, zs);
		}
		}
	}
	
	private String getRegistryName(Block b)
	{
		String s = "";
		if( b.getRegistryName().getNamespace().equals("minecraft"))
		{
		s = b.getRegistryName().getPath();
		}
		else
		{
			s = b.getRegistryName().toString();
		}
		
		//lazy gets all logs but you can add own woods
		if(b instanceof LogBlock)
		{
			s= "wood";
		}
		return s;
	}
	
	@SubscribeEvent
	public void onPlayerHarvest(BlockEvent.BreakEvent event)
	{	
		PlayerEntity player = event.getPlayer();
		if(player.world.isRemote || player.isCreative() || checkSilkTouch(player)) { return; }
		Block b = event.getState().getBlock();	
		BlockPos bp = event.getPos();
		String s = getRegistryName(b);	
		//player.sendMessage(new StringTextComponent(b.getRegistryName().getPath())); //for test
		LazyOptional<IZStat> lo = player.getCapability(ZStatController.ZStat_CAP);
		IZStat zs = lo.orElse(null);
		if(zs==null) { return; }	
		if(ZmodJson.block_map.containsKey(s))
		{
			BlockInfo bi = ZmodJson.block_map.get(s);		
			switch(bi.getCategory())
			{
			case 0: //mining
			    //loteria
				if(ZmodJson.mining_lottery.contains(s))
				{
				int bonus = LotteryBonus(ZmodJson.expToLevel(zs.get_mining_xp())*ZmodJson.server_configs.getDouble("mining_bonus")*getPlayerRankingBonus(player,0));
				if(bonus > 0)
				{
					if(player.world instanceof ServerWorld)
					{
						TileEntity tileentity = event.getState().hasTileEntity() ? player.world.getTileEntity(bp) : null;
						List<ItemStack> lis = Block.getDrops(event.getState(), (ServerWorld) player.world, bp, tileentity );
						lis.get(0).setCount(bonus);
							ItemEntity ie = new ItemEntity(player.world, bp.getX(), bp.getY(), bp.getZ(), lis.get(0));
							player.world.addEntity(ie);	
							player.sendMessage(new StringTextComponent("Mining bonus: "+bonus+" "+ie.getDisplayName().getUnformattedComponentText()));
					}
				}				
				}
				zs.add_mining_xp(bi.getXp()); 	//add exp
				break; //end mining
				
			case 2: //digging				
				DiggingLotteryBonus(ZmodJson.expToLevel(zs.get_digging_xp())*ZmodJson.server_configs.getDouble("digging_bonus")*getPlayerRankingBonus(player,1), player, bp);	
				zs.add_digging_xp(bi.getXp());	//add exp
				break; //end digging
				
			case 3: //woodcutting
				//lottery
				int bonus = LotteryBonus(ZmodJson.expToLevel(zs.get_cutting_xp())*ZmodJson.server_configs.getDouble("woodcutting_bonus")*getPlayerRankingBonus(player,3));
				if(bonus > 0)
				{
					if(player.world instanceof ServerWorld)
					{
						TileEntity tileentity = event.getState().hasTileEntity() ? player.world.getTileEntity(bp) : null;
						List<ItemStack> lis = Block.getDrops(event.getState(), (ServerWorld) player.world, bp, tileentity );
						lis.get(0).setCount(bonus);
							ItemEntity ie = new ItemEntity(player.world, bp.getX(), bp.getY(), bp.getZ(), lis.get(0));
							player.world.addEntity(ie);
							player.sendMessage(new StringTextComponent("Woodcutting bonus: "+bonus+" "+ie.getDisplayName().getUnformattedComponentText()));
					}
				}		
				zs.add_cutting_xp(bi.getXp()); 	//add exp
				break; //end woodcutting
				
			case 4: //farming
				//default crops				
				if(b instanceof MelonBlock)
				{
					FarmingAction(zs, event.getState(), player, bp, bi);
				}
				else
				{
					if(((CropsBlock) b).isMaxAge(event.getState()))
					{
						FarmingAction(zs, event.getState(), player, bp, bi);	
					}
				}
				break;
			}
				zs.Sync((ServerPlayerEntity) player);
				UpdateRanking((ServerPlayerEntity)player, zs);
		}//end block_map
		
	}
	
	private boolean checkSilkTouch(PlayerEntity player)
	{
		ListNBT enchant_list = player.inventory.getCurrentItem().getEnchantmentTagList();
		for(int i=0;i<enchant_list.size();i++)
		{		
			if(enchant_list.get(i).getString().contains("minecraft:silk_touch"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void FarmingAction(IZStat zs, BlockState bstate, PlayerEntity player, BlockPos bp, BlockInfo bi)
	{
		int bonus1 = LotteryBonus(ZmodJson.expToLevel(zs.get_farm_xp())*ZmodJson.server_configs.getDouble("farming_bonus")*getPlayerRankingBonus(player,4));
		if(bonus1 > 0)
		{
			if(player.world instanceof ServerWorld)
			{
				TileEntity tileentity = bstate.hasTileEntity() ? player.world.getTileEntity(bp) : null;
				List<ItemStack> lis = Block.getDrops(bstate, (ServerWorld) player.world, bp, tileentity );
				for(ItemStack ls : lis)
				{
					ls.setCount(bonus1);
					ItemEntity ie = new ItemEntity(player.world, bp.getX(), bp.getY(), bp.getZ(), ls);
					player.world.addEntity(ie);
				}						
					player.sendMessage(new StringTextComponent("Farming bonus: crops"));
			}
		}
		zs.add_farm_xp(bi.getXp()); //add exp
	}
	
	private void UpdateRanking(ServerPlayerEntity player, IZStat zs)
	{
		if(ZEventHandlerServer.ranking != null)
		{
			ZEventHandlerServer.ranking.addScore(player.getName().getFormattedText(), zs);
		}
	}
	
	private int LotteryBonus(double chance)
	{	
		int bonus = (int) Math.floor((chance/100.0));
		double losu = r.nextDouble()*100.0 + 0.001; //0.001~100
		if(chance-(bonus*100) >= losu)
		{
			bonus+=1;
		}
		return bonus;
	}
	
	@SubscribeEvent
	public void onBreakBonus(PlayerEvent.BreakSpeed event) //woodcutting i mining
	{
		PlayerEntity player = event.getEntityPlayer();
		LazyOptional<IZStat> lo = player.getCapability(ZStatController.ZStat_CAP);
		IZStat zs = lo.orElse(null);
		if(zs==null) { return; }
		if(event.getState().getMaterial() == Material.ROCK)
		{
			event.setNewSpeed((float) (event.getOriginalSpeed()*(ZmodJson.expToLevel(zs.get_mining_xp())*ZmodJson.server_configs.getDouble("break_bonus")+1))); 
		}
		else if(event.getState().getMaterial() == Material.WOOD)
		{
			event.setNewSpeed((float) (event.getOriginalSpeed()*(ZmodJson.expToLevel(zs.get_cutting_xp())*ZmodJson.server_configs.getDouble("break_bonus")+1))); 
		}
	}
	
	
	
	
	

}
