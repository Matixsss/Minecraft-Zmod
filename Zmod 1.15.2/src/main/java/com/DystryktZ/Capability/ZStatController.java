package com.DystryktZ.Capability;



import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ZStatController implements ICapabilitySerializable<INBT> {
	@CapabilityInject(IZStat.class)
	public static final Capability<IZStat> ZStat_CAP = null;
	
	private IZStat instance = ZStat_CAP.getDefaultInstance();
	
	private LazyOptional<IZStat> lazyop;

	public ZStatController()
	{
		lazyop = LazyOptional.of(()-> this.instance);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ZStat_CAP.orEmpty(cap, lazyop);		
	}

	@Override
	public INBT serializeNBT() {
		// TODO Auto-generated method stub
		return ZStat_CAP.getStorage().writeNBT(ZStat_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		ZStat_CAP.getStorage().readNBT(ZStat_CAP, this.instance, null, nbt);	
	}

}
