package com.ade.content.guns;

import com.ade.content.entities.EntityBullet;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMag extends Item 
{
	
    public ItemMag(String name)
    {
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
    }
    
}
