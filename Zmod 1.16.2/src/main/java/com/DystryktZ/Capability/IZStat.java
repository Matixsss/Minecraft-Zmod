package com.DystryktZ.Capability;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IZStat extends ICapabilitySerializable<INBT> {
	public void add_mining_xp(int xp);
	public void add_combat_xp(int xp);
	public void add_digging_xp(int xp);
	public void add_cutting_xp(int xp);
	public void add_farm_xp(int xp);
	public void add_building_xp(int xp);
	
	public void set_mining_xp(int xp);
	public void set_combat_xp(int xp);
	public void set_digging_xp(int xp);
	public void set_cutting_xp(int xp);
	public void set_farm_xp(int xp);
	public void set_building_xp(int xp);
	
	public void sub_mining_xp(int xp);
	public void sub_farming_xp(int xp);

	public int get_mining_xp(); //0
	public int get_combat_xp(); //1
	public int get_digging_xp(); //2
	public int get_cutting_xp(); //3
	public int get_farm_xp(); //4
	public int get_building_xp(); //5
	
	public void Sync(ServerPlayerEntity player);
}
