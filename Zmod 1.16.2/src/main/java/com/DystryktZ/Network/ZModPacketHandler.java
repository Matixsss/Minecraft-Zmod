package com.DystryktZ.Network;

import java.util.List;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ZModPacketHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation("zmod", "main_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();
	
	public static void Register()
	{
		int disc = 0;
		HANDLER.registerMessage(disc++, ZModCapPacket.class, ZModCapPacket::encode, ZModCapPacket::decode, ZModCapPacket.Handler::handle);
		HANDLER.registerMessage(disc++, ClientPacket.class, ClientPacket::encode, ClientPacket::decode, ClientPacket.Handler::handle);
		HANDLER.registerMessage(disc++, ClientPacketSettings.class, ClientPacketSettings::encode, ClientPacketSettings::decode, ClientPacketSettings.Handler::handle);
		HANDLER.registerMessage(disc++, ServerPacket_Ranking.class, ServerPacket_Ranking::encode, ServerPacket_Ranking::decode, ServerPacket_Ranking.Handler::handle);
		HANDLER.registerMessage(disc++, ServerPacket_ServerInfo.class, ServerPacket_ServerInfo::encode, ServerPacket_ServerInfo::decode, ServerPacket_ServerInfo.Handler::handle);
		HANDLER.registerMessage(disc++, ServerPacket_UpdateSettings.class, ServerPacket_UpdateSettings::encode, ServerPacket_UpdateSettings::decode, ServerPacket_UpdateSettings.Handler::handle);
	}
	
	
	/**
	 * Send a packet to a specific player.<br>
	 * Must be called Server side. 
	 */
	
	public static void sendTo(Object msg, ServerPlayerEntity player)
	{
		if (!(player instanceof FakePlayer))
		{
			HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}
	
	/**
	 * Sends a packet to the server.<br>
	 * Must be called Client side. 
	 */
	public static void sendToServer(Object msg)
	{
		HANDLER.sendToServer(msg);
	}
	
	/**on server side*/
	public static void sendToAllPlayers(Object msg, MinecraftServer serv)
	{
		List<ServerPlayerEntity> lista = serv.getPlayerList().getPlayers();
		for(ServerPlayerEntity e : lista)
		{
			sendTo(msg, e);
		}	
	}

}
