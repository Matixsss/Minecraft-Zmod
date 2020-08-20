package com.DystryktZ.Commands;

import com.DystryktZ.ZmodJson;
import com.DystryktZ.Network.ServerPacket_ServerInfo;
import com.DystryktZ.Network.ZModPacketHandler;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class ZReloadCommand   {
	
	 public static void register(CommandDispatcher<CommandSource> dispatcher) {
	      dispatcher.register(Commands.literal("zreloadconfigs").requires((p_198521_0_) -> {
	         return p_198521_0_.hasPermissionLevel(4);
	      }).executes((p_198726_0_) -> {
	         ReloadConfigs( p_198726_0_.getSource());
	         p_198726_0_.getSource().sendFeedback(new StringTextComponent("ZConfigs reloaded"), true);
	         return 1;
	      }));
	   }
	 
	 private static void ReloadConfigs(CommandSource source)
	 {		 
		 System.out.println("Reloading zmod config files...");
		 ZmodJson.LoadJson();
		 //send packet to all players
		 ZModPacketHandler.sendToAllPlayers(new ServerPacket_ServerInfo(ZmodJson.server_configs), source.getServer());
	 }

	

}
