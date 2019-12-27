package com.DystryktZ.Commands;

import com.DystryktZ.ZmodJson;
import com.DystryktZ.Network.ServerPacket_UpdateSettings;
import com.DystryktZ.Network.ZModPacketHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

public class ZSettingsCommand   {
	
	 public static void register(CommandDispatcher<CommandSource> dispatcher) {
	      dispatcher.register(Commands.literal("zsettings").then(Commands.literal("notification").then(Commands.argument("bool", BoolArgumentType.bool()).executes( (arg) -> 
	      {
	    	  Notification(arg.getSource(),BoolArgumentType.getBool(arg, "bool"));
	    	  return 1;
	      }))).then(Commands.literal("blockinfo").requires((p_198521_0_) -> {
		         return p_198521_0_.hasPermissionLevel(4);
		      }).then(Commands.argument("bool", BoolArgumentType.bool()).executes((p_198726_0_) -> {
		         BlockInfoToggle( p_198726_0_.getSource(), BoolArgumentType.getBool( p_198726_0_, "bool"));
		         return 1;
		      }))).then(Commands.literal("entityinfo").requires((p_198521_0_) -> {
		         return p_198521_0_.hasPermissionLevel(4);
		      }).then(Commands.argument("bool", BoolArgumentType.bool()).executes((p_198726_0_) -> {
		         EntityInfoToggle( p_198726_0_.getSource(), BoolArgumentType.getBool( p_198726_0_, "bool"));
		         return 1;
		      }))));
	   }
	 
	 private static void Notification (CommandSource source, boolean check)
	 {
		 String message = "";
		 if(ZmodJson.players_settings.containsKey(source.getName()))
		 {
			 boolean[] tab =  ZmodJson.players_settings.get(source.getName());
			 tab[0] = check;
			 if(check)
			 {
				 message = "Notification turned - on";
				
			 }
			 else
			 {
				 message = "Notification turned - off";
			 }
			 ZmodJson.players_settings.put(source.getName(), tab);
			 try {
				 CompoundNBT data = new CompoundNBT();
				 data.putBoolean("notification", check);
				ServerPlayerEntity player = source.asPlayer();
				 ZModPacketHandler.sendTo(new ServerPacket_UpdateSettings(data), player);
			} catch (CommandSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 source.sendFeedback(new StringTextComponent(message), true);
		 }
       
	 }
	 
	 private static void BlockInfoToggle(CommandSource source, boolean check)
	 {
		 String message = "";
		 if(ZmodJson.players_settings.containsKey(source.getName()))
		 {
			 boolean[] temp = ZmodJson.players_settings.get(source.getName());
			 if(check)
			 {
				 message = "Show block info - on";
				
			 }
			 else
			 {
				 message = "Show block info - off";
			 }
			 temp[1] = check;
			 ZmodJson.players_settings.put(source.getName(), temp);
			 try {
				 CompoundNBT data = new CompoundNBT();
				 data.putBoolean("blockinfo", check);
				ServerPlayerEntity player = source.asPlayer();
				 ZModPacketHandler.sendTo(new ServerPacket_UpdateSettings(data), player);
			} catch (CommandSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 source.sendFeedback(new StringTextComponent(message), true);
		 }
	 }
	 
	 private static void EntityInfoToggle(CommandSource source, boolean check)
	 {
		 String message = "";
		 if(ZmodJson.players_settings.containsKey(source.getName()))
		 {
			 boolean[] temp = ZmodJson.players_settings.get(source.getName());
			 if(check)
			 {
				 message = "Show entity info - on";
				
			 }
			 else
			 {
				 message = "Show entity info - off";
			 }
			 temp[2] = check;
			 ZmodJson.players_settings.put(source.getName(), temp);
			 try {
				 CompoundNBT data = new CompoundNBT();
				 data.putBoolean("entityinfo", check);
				ServerPlayerEntity player = source.asPlayer();
				 ZModPacketHandler.sendTo(new ServerPacket_UpdateSettings(data), player);
			} catch (CommandSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 source.sendFeedback(new StringTextComponent(message), true);
		 }
	 }

	

}
