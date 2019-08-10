package com.DystryktZ;

import com.DystryktZ.GUI.ZGuiScreen;
import com.DystryktZ.GUI.ZRanking;
import com.DystryktZ.Network.ClientPacket;
import com.DystryktZ.Network.ZModPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ZEventHandlerClient {
	
	public static Minecraft mc_instance;
	public static ZRanking guiWaitingForPacket = null;
	public static KeyBinding[] keyBindings;


	
	public ZEventHandlerClient()
	{
		mc_instance = Minecraft.getInstance();
		keyBindings = new KeyBinding[1];
		keyBindings[0] = new KeyBinding("key.openSkillsGui", 71, "key.zmod.category");
		  
		for (int i = 0; i < keyBindings.length; ++i) 
		{
		    ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}
	
	 @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
	  public void onEventKeyInput(KeyInputEvent e)
	    {	
		 if(keyBindings[0].isPressed() &&  mc_instance.currentScreen == null)
		 {
			mc_instance.displayGuiScreen(new ZGuiScreen());
		 }
	    }
	 
	 @SubscribeEvent
		public static void onEntityJoinWorld(EntityJoinWorldEvent event)
		{
			
		}
	 
	 public static void getRanking(ZRanking zrank)
	 {
		 guiWaitingForPacket = zrank;
		 ZModPacketHandler.sendToServer(new ClientPacket((byte)5));
	 }
	 
}
