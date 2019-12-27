package com.DystryktZ.Network;

import java.util.function.Supplier;

import com.DystryktZ.ZEventHandlerServer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientPacket {
	private final byte nbt;
	
	public ClientPacket(byte nbt)
	{
		this.nbt = nbt;
	}
	
	public static void encode(ClientPacket msg, PacketBuffer buf)
	{
		buf.writeByte(msg.nbt);
	}
	
	public static ClientPacket decode(PacketBuffer buf)
	{
		return new ClientPacket(buf.readByte());
	}
	
	public static class Handler //on server side
	{
		public static void handle(final ClientPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				ServerPlayerEntity server_player = ctx.get().getSender();
				switch(message.nbt)
				{
				case 5: //getRankingAndSentToClient
					//System.out.println("dostalem pakiecik od: "+server_player.getName().getFormattedText());
					if(ZEventHandlerServer.ranking != null && server_player != null)
					{
					if(ZEventHandlerServer.ranking.getRank() != null)
					{
					ZModPacketHandler.sendTo(new ServerPacket_Ranking(ZEventHandlerServer.ranking.getRank()),server_player);
					}
					}
				break;
				}							
				
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
