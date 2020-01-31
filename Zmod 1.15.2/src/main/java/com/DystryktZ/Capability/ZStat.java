package com.DystryktZ.Capability;

import com.DystryktZ.Network.ZModCapPacket;
import com.DystryktZ.Network.ZModPacketHandler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class ZStat implements IZStat {
	
	private int mining_xp = 0;
	private int digging_xp = 0;
	private int cutting_xp = 0;
	private int combat_xp = 0;
	private int farm_xp = 0;
	private int building_xp = 0;
	
	@Override
	public void add_mining_xp(int xp) {
		mining_xp += xp;
	}
	@Override
	public void add_combat_xp(int xp) {
		combat_xp += xp;
	}
	@Override
	public void add_digging_xp(int xp) {
		digging_xp += xp;
	}
	@Override
	public void add_cutting_xp(int xp) {
		cutting_xp += xp;
	}
	@Override
	public void add_farm_xp(int xp) {
		farm_xp += xp;
	}
	
	@Override
	public void add_building_xp(int xp) {
		building_xp += xp;	
	}
	
	@Override
	public int get_farm_xp() {
		return farm_xp;
	}
	@Override
	public int get_mining_xp() {
		return mining_xp;
	}
	@Override
	public int get_combat_xp() {
		return combat_xp;
	}
	@Override
	public int get_cutting_xp() {
		return cutting_xp;
	}
	@Override
	public int get_digging_xp() {
		return digging_xp;
	}
	
	@Override
	public int get_building_xp() {
		return building_xp;		
	}
	
	@Override
	public void set_mining_xp(int xp) {
		mining_xp = xp;
	}
	@Override
	public void set_combat_xp(int xp) {
		combat_xp = xp;
	}
	@Override
	public void set_digging_xp(int xp) {
		digging_xp = xp;
	}
	@Override
	public void set_cutting_xp(int xp) {
		cutting_xp = xp;
	}
	@Override
	public void set_farm_xp(int xp) {
		farm_xp = xp;
	}
	
	@Override
	public void set_building_xp(int xp) {
		building_xp = xp;	
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public INBT serializeNBT() {
		int[] temp = new int[6];
		temp[0] = get_mining_xp();
		temp[1] = get_combat_xp();
		temp[2] = get_digging_xp();
		temp[3] = get_cutting_xp();
		temp[4] = get_farm_xp();
		temp[5] = get_building_xp();
		return new IntArrayNBT(temp);
	}
	@Override
	public void deserializeNBT(INBT nbt) {
		int[] temp = new int[6];
		temp = ((IntArrayNBT) nbt).getIntArray();
		set_mining_xp(temp[0]);
		set_combat_xp(temp[1]);
	    set_digging_xp(temp[2]);
	    set_cutting_xp(temp[3]);
		set_farm_xp(temp[4]);
		set_building_xp(temp[5]);
		
	}
	@Override
	public void Sync(ServerPlayerEntity player) {
			ZModPacketHandler.sendTo(new ZModCapPacket((IntArrayNBT)serializeNBT()), player);	
	}

	@Override
	public void sub_mining_xp(int xp) {
		mining_xp -= xp;	
		if(mining_xp <0)
		{
			mining_xp = 0;
		}
	}
	@Override
	public void sub_farming_xp(int xp) {
		farm_xp -= xp;	
		if(farm_xp <0)
		{
			farm_xp = 0;
		}
		
	}

}
