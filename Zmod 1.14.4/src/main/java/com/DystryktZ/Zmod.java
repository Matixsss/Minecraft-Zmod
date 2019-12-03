package com.DystryktZ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.DystryktZ.Capability.CapabilityHandler;
import com.DystryktZ.Capability.IZStat;
import com.DystryktZ.Capability.ZStat;
import com.DystryktZ.Capability.ZStorage;
import com.DystryktZ.Commands.ZReloadCommand;
import com.DystryktZ.Commands.ZResetStatsCommand;
import com.DystryktZ.Commands.ZSettingsCommand;
import com.DystryktZ.Network.ZModPacketHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("zmod")
public class Zmod
{
	
	//public static ZModJson config;
	private String directory;
	public static boolean isClient;

    public Zmod() {
    	
    	 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	 
    	 if(Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER)
    	 {		 
    		 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    		 isClient = true;
    	 }
    	 else
    	 {    		
    		 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onDedicatedServerStarting);
    		 isClient = false;
    	 }

         MinecraftForge.EVENT_BUS.register(new CapabilityHandler());	
         MinecraftForge.EVENT_BUS.register(new ZEventHandler());
         MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    }
    
	 private void serverStarting(FMLServerStartingEvent event)
	    {
	    	ZReloadCommand.register(event.getCommandDispatcher());
	    	ZResetStatsCommand.register(event.getCommandDispatcher());
	    	ZSettingsCommand.register(event.getCommandDispatcher());
	    }
    
   
    
    private void doClientStuff(FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    	try {
			directory = event.getMinecraftSupplier().get().gameDir.getCanonicalPath()+File.separator+"config"+File.separator;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	SetupConfig();  	
    	MinecraftForge.EVENT_BUS.register(new ZEventHandlerClient());  	
    }
    
    private void onDedicatedServerStarting(FMLDedicatedServerSetupEvent event) {
        // do something when the server starts
        try {
			directory = event.getServerSupplier().get().getDataDirectory().getCanonicalPath()+File.separator+"config"+File.separator;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SetupConfig();
        MinecraftForge.EVENT_BUS.register(new ZEventHandlerServer());
    }
    
    private void SetupConfig()
    {
    	if(!Files.isDirectory(Paths.get(directory+"ZMod")))
    	{
    		try {
				Files.createDirectory(Paths.get(directory+"ZMod"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	ZmodJson.setPath(directory+"ZMod");
    	ZmodJson.LoadJson();
    	if(isClient)
    	{
    		ZmodJsonClient.setPath(directory+"ZMod");
    		ZmodJsonClient.loadConfigs();
    	}
    }
    
    private void setup(final FMLCommonSetupEvent event)
    { 
    	//przygotowac moda
    	 CapabilityManager.INSTANCE.register(IZStat.class, new ZStorage(), ZStat::new);
    	 ZModPacketHandler.Register();	
    }

   

}


