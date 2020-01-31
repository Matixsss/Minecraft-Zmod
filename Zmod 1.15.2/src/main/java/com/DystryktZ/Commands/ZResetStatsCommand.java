package com.DystryktZ.Commands;

import java.util.Collection;

import com.DystryktZ.Capability.ZStatController;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ZResetStatsCommand {
	   public static void register(CommandDispatcher<CommandSource> dispatcher) {
		      dispatcher.register(Commands.literal("zresetstats").requires((p_198521_0_) -> {
		         return p_198521_0_.hasPermissionLevel(4);
		      }).then(Commands.argument("targets", EntityArgument.entities()).executes((p_198520_0_) -> {		    	
		         return resetStats(p_198520_0_.getSource(), EntityArgument.getEntities(p_198520_0_, "targets"));
		      })));
		   }
	   
	   private static int resetStats(CommandSource source, Collection<? extends Entity> targets)
	   {
		   for(Entity entity : targets) {		
		         entity.getCapability(ZStatController.ZStat_CAP).ifPresent( zs -> {
		         zs.set_building_xp(0);
		         zs.set_mining_xp(0);
		         zs.set_combat_xp(0);
		         zs.set_cutting_xp(0);
		         zs.set_farm_xp(0);
		         zs.set_digging_xp(0);
		         zs.Sync((ServerPlayerEntity)entity);
		         entity.sendMessage(new StringTextComponent("ZStats restarted"));
		         });
		      }
		   return 1;
	   }
	 
	 
}
