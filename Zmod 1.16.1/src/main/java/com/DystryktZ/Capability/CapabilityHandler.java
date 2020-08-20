package com.DystryktZ.Capability;



import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {
	public static final ResourceLocation ZStat_CAP = new ResourceLocation("zmod", "zstat");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	{
		Entity e = event.getObject();
		if ((e instanceof PlayerEntity)) //&& e.world.isRemote == false
		{
			event.addCapability(ZStat_CAP, new ZStatController());
		}
	}

}
