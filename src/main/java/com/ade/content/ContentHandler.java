package com.ade.content;

import java.util.ArrayList;
import java.util.List;

import com.ade.content.guns.EnumFireMode;
import com.ade.content.guns.ItemGun;
import com.ade.content.guns.ItemMag;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class ContentHandler 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item AK47 = new ItemGun("assault_rifle-ak47", 3, 10D, 2.5F, 1.25F, 30, EnumFireMode.AUTO);
	public static final Item AKS74U = new ItemGun("carbine-aks74u", 3, 10D, 3.0F, 1.25F, 30, EnumFireMode.AUTO);
	public static final Item ARX160 = new ItemGun("assault_rifle-arx160", 3, 10D, 2.0F, 1.25F, 30, EnumFireMode.AUTO);
	public static final Item DRAGUNOV = new ItemGun("sniper_rifle-dragunov", 5, 10D, 1.5F, 1.25F, 10, EnumFireMode.SEMI);
	public static final Item M16A4 = new ItemGun("assault_rifle-m16a4", 3, 10D, 1.8F, 1.25F, 30, EnumFireMode.BURST);
	public static final Item M4CARBINE = new ItemGun("carbine-m4carbine", 3, 10D, 1.8F, 1.25F, 30, EnumFireMode.BURST);
	public static final Item MAKAROV = new ItemGun("pistol-makarov", 10, 10D, 0.5F, 1.25F, 8, EnumFireMode.SEMI);
	public static final Item MK11 = new ItemGun("sniper_rifle-mk11", 5, 10D, 1.25F, 1.25F, 10, EnumFireMode.SEMI);
	public static final Item MPX = new ItemGun("pdw-mpx", 3, 10D, 0.45F, 1.25F, 30, EnumFireMode.AUTO);
	public static final Item USP45 = new ItemGun("pistol-usp45", 10, 10D, 0.45F, 1.25F, 10, EnumFireMode.SEMI);
	
	public static final Item MAGAZINE = new ItemMag("magazine");
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event)
	{
		final Item[] items = {
				
				AK47,
				AKS74U,
				ARX160,
				DRAGUNOV,
				M16A4,
				M4CARBINE,
				MAKAROV,
				MK11,
				MPX,
				USP45,
				
				MAGAZINE,
		};
		
		final IForgeRegistry<Item> registry = event.getRegistry();
		
		for (final Item item : items) 
		{
			registry.register(item);
			ITEMS.add(item);
		}
	}
	
}
