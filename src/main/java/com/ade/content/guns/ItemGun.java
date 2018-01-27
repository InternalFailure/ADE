package com.ade.content.guns;

import com.ade.content.IHasModel;
import com.ade.content.entities.EntityBullet;
import com.ade.core.ADE;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemGun extends Item implements IHasModel
{
	//Measured in ticks
	public int fireDelay;
	public double damage;
	public float inaccuracy;
	
	public ItemGun(String name, int fireDelay, double damage, float inaccuracy) 
	{
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setMaxStackSize(1);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.fireDelay = fireDelay;
        this.damage = damage;
        this.inaccuracy = inaccuracy;
	}
	
    private ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isBulletAvailable(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isBulletAvailable(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isBulletAvailable(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    protected boolean isBulletAvailable(ItemStack stack)
    {
        return stack.getItem() instanceof ItemMag;
    }
    
    public int getMaxItemUseDuration(ItemStack itemstack) 
    {
    	return fireDelay;
    }
    
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        boolean flag = !this.findAmmo(playerIn).isEmpty();
        
		ItemStack stack = this.findAmmo(playerIn);
		if(stack != ItemStack.EMPTY)
		{
			if(!worldIn.isRemote)
			{
	            float x = -MathHelper.sin(playerIn.rotationYaw * 0.017F) * MathHelper.cos(playerIn.rotationPitch * 0.017F);
	            float y = -MathHelper.sin(playerIn.rotationPitch * 0.017F);
	            float z = MathHelper.cos(playerIn.rotationYaw * 0.017F) * MathHelper.cos(playerIn.rotationPitch * 0.017F);
				EntityBullet bullet = new EntityBullet(worldIn, playerIn, damage, x, y, z);
				worldIn.spawnEntity(bullet);
			}
			stack.shrink(1);
			
			if(stack.isEmpty())
			{
				playerIn.inventory.deleteStack(stack);
			}
			
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	
	
	@Override
	public void registerModels() 
	{
		ADE.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
