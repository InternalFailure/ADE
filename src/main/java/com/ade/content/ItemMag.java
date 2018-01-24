package com.ade.content;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMag extends Item 
{
    public ItemMag()
    {
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    public EntityBullet createBullet(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
        EntityBullet entitytippedarrow = new EntityBullet(worldIn, shooter);
        return entitytippedarrow;
    }
}
