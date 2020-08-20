package com.DystryktZ.Network;

import java.util.function.Supplier;

import com.DystryktZ.ZmodJson;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientPacketSettings {
	private final CompoundNBT nbt;
	
	public ClientPacketSettings(CompoundNBT nbt)
	{
		this.nbt = nbt;
	}
	
	public static void encode(ClientPacketSettings msg, PacketBuffer buf)
	{
		buf.writeCompoundTag(msg.nbt);
	}
	
	public static ClientPacketSettings decode(PacketBuffer buf)
	{
		return new ClientPacketSettings(buf.readCompoundTag());
	}
	
	public static class Handler //on server side
	{
		public static void handle(final ClientPacketSettings message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				ServerPlayerEntity server_player = ctx.get().getSender();
				String name = server_player.getDisplayName().getString();
				boolean[] settings = new boolean[3];
				settings[0] = message.nbt.getBoolean("notification");
				if(server_player.hasPermissionLevel(4))
				{
				settings[1] = message.nbt.getBoolean("blockinfo");
				settings[2] = message.nbt.getBoolean("entityinfo");
				}
				else
				{
					settings[1] = false;
					settings[2] = false;
				}
				
				ZmodJson.players_settings.put(name, settings);
				
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
