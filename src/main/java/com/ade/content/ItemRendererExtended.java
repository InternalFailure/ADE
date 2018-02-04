package com.ade.content;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemRendererExtended extends ItemRenderer 
{
	public ItemRendererExtended(Minecraft mcIn) 
	{
		super(mcIn);
	}
	
	public void renderItemInFirstPersond(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_) 
	{
		super.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_, stack, p_187457_7_);
		
		ItemGunStack gunStack = ItemGunStack.getGunStack();
		
		switch (gunStack.getItemUseAction()) 
		{
		case NONE:
			break;
		case GUN:
			break;
		case MELEE:
			break;
		default:
			break;
			
		}
	}
}
