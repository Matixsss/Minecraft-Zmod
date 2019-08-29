package com.DystryktZ.Commands;

import com.DystryktZ.ZmodJson;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class ZBlockInfoCommand   {
	
	 public static void register(CommandDispatcher<CommandSource> dispatcher) {
	      dispatcher.register(Commands.literal("zblockinfo").requires((p_198521_0_) -> {
	         return p_198521_0_.hasPermissionLevel(4);
	      }).executes((p_198726_0_) -> {
	         BlockInfoToggle( p_198726_0_.getSource());
	         return 1;
	      }));
	   }
	 
	 private static void BlockInfoToggle(CommandSource source)
	 {
		 String message = "";
		 if(ZmodJson.c_show_block_info.contains(source.getName()))
		 {
			 message = "Show block info - off";
			 ZmodJson.c_show_block_info.remove(source.getName());
		 }
		 else
		 {
			 message = "Show block info - on";
			 ZmodJson.c_show_block_info.add(source.getName());
		 }
        source.sendFeedback(new StringTextComponent(message), true);
	 }

	

}
