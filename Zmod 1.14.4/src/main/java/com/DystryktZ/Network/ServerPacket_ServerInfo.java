package com.DystryktZ.Network;

import java.util.function.Supplier;

import com.DystryktZ.ZmodJson;
import com.DystryktZ.ZmodJsonClient;

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
				CompoundNBT settings_nbt = new CompoundNBT();
				settings_nbt.putBoolean("notification", ZmodJsonClient.notification);
				settings_nbt.putBoolean("blockinfo", ZmodJsonClient.blockinfo);
				settings_nbt.putBoolean("entityinfo", ZmodJsonClient.entityinfo);
				ZModPacketHandler.sendToServer(new ClientPacketSettings(settings_nbt));
				
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
