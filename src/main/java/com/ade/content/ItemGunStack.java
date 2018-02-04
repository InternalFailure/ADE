package com.ade.content;

import net.minecraft.item.ItemStack;

public class ItemGunStack 
{
	private static ItemGunStack gunStack;
	
	public EnumActionPlus getItemUseAction() 
	{
		return EnumActionPlus.NONE;
	}
	
	public static ItemGunStack getGunStack() 
	{
		return gunStack;
	}
}
