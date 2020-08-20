package com.DystryktZ.Network;

import java.util.function.Supplier;

import com.DystryktZ.ZEventHandlerClient;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerPacket_Ranking {
	private final CompoundNBT nbt;
	
	public ServerPacket_Ranking(CompoundNBT nbt)
	{
		this.nbt = nbt;
	}
	
	public static void encode(ServerPacket_Ranking msg, PacketBuffer buf)
	{
		buf.writeCompoundTag(msg.nbt);
	}
	
	public static ServerPacket_Ranking decode(PacketBuffer buf)
	{
		return new ServerPacket_Ranking(buf.readCompoundTag());
	}
	
	public static class Handler //client
	{
		public static void handle(final ServerPacket_Ranking message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				
			//get gui
				if(ZEventHandlerClient.guiWaitingForPacket != null)
				{
					ZEventHandlerClient.guiWaitingForPacket.PacketRespond(message.nbt);
					ZEventHandlerClient.guiWaitingForPacket = null;
				}
				
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
