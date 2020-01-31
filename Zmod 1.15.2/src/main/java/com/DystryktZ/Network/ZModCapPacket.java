package com.DystryktZ.Network;

import java.util.function.Supplier;

import com.DystryktZ.Capability.ZStatController;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ZModCapPacket {
	private final IntArrayNBT nbt;
	
	public ZModCapPacket(IntArrayNBT nbt)
	{
		this.nbt = nbt;
	}
	
	public static void encode(ZModCapPacket msg, PacketBuffer buf)
	{
		buf.writeVarIntArray(msg.nbt.getIntArray());
	}
	
	public static ZModCapPacket decode(PacketBuffer buf)
	{
		return new ZModCapPacket(new IntArrayNBT(buf.readVarIntArray()));
	}
	
	public static class Handler
	{
		public static void handle(final ZModCapPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				
				Minecraft.getInstance().player.getCapability(ZStatController.ZStat_CAP)
						.ifPresent(cap -> cap.deserializeNBT(message.nbt));
				
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
