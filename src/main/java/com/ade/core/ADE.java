package com.ade.core;

import com.ade.content.ContentHandler;
import com.ade.content.guns.ItemGun;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "ade", useMetadata = true)
public class ADE 
{
	
	@SidedProxy(clientSide = "com.ade.core.ClientProxy", serverSide = "com.ade.core.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		
	}
	
}
