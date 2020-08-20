package com.DystryktZ.Capability;


import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ZStorage implements Capability.IStorage<IZStat> {

	@Override
	public INBT writeNBT(Capability<IZStat> capability, IZStat instance, Direction side) {
		int[] temp = new int[6];
		temp[0] = instance.get_mining_xp();
		temp[1] = instance.get_combat_xp();
		temp[2] = instance.get_digging_xp();
		temp[3] = instance.get_cutting_xp();
		temp[4] = instance.get_farm_xp();
		temp[5] = instance.get_building_xp();
		return new IntArrayNBT(temp);
	}

	@Override
	public void readNBT(Capability<IZStat> capability, IZStat instance, Direction side, INBT nbt) {
		int[] temp = new int[6];
		temp = ((IntArrayNBT) nbt).getIntArray();
		instance.set_mining_xp(temp[0]);
		instance.set_combat_xp(temp[1]);
		instance.set_digging_xp(temp[2]);
		instance.set_cutting_xp(temp[3]);
		instance.set_farm_xp(temp[4]);
		instance.set_building_xp(temp[5]);
	}

}
