package com.DystryktZ.Network;

import java.util.function.Supplier;

import com.DystryktZ.ZmodJsonClient;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerPacket_UpdateSettings {
	private final CompoundNBT nbt;
	
	public ServerPacket_UpdateSettings(CompoundNBT nbt)
	{
		this.nbt = nbt;
	}
	
	public static void encode(ServerPacket_UpdateSettings msg, PacketBuffer buf)
	{
		buf.writeCompoundTag(msg.nbt);
	}
	
	public static ServerPacket_UpdateSettings decode(PacketBuffer buf)
	{
		return new ServerPacket_UpdateSettings(buf.readCompoundTag());
	}
	
	public static class Handler //client
	{
		public static void handle(final ServerPacket_UpdateSettings message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
			
				//unpack message
				if(message.nbt.contains("notification"))
				{
				ZmodJsonClient.notification = message.nbt.getBoolean("notification");
				}
				
				if(message.nbt.contains("blockinfo"))
				{
				ZmodJsonClient.blockinfo = message.nbt.getBoolean("blockinfo");
				}
				
				if(message.nbt.contains("entityinfo"))
				{
				ZmodJsonClient.entityinfo = message.nbt.getBoolean("entityinfo");
				}
			
				ZmodJsonClient.saveConfigs();
				
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
