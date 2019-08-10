package com.DystryktZ.Network;

import java.util.function.Supplier;

import com.DystryktZ.ZmodJson;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerPacket_ServerInfo {
private final CompoundNBT nbt;
	
	public ServerPacket_ServerInfo(CompoundNBT nbt)
	{
		this.nbt = nbt;
	}
	
	public static void encode(ServerPacket_ServerInfo msg, PacketBuffer buf)
	{
		buf.writeCompoundTag(msg.nbt);
	}
	
	public static ServerPacket_ServerInfo decode(PacketBuffer buf)
	{
		return new ServerPacket_ServerInfo(buf.readCompoundTag());
	}
	
	public static class Handler //client
	{
		public static void handle(final ServerPacket_ServerInfo message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				
				ZmodJson.server_configs = message.nbt;
			
				
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
