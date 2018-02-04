package com.ade.content.guns;

import org.lwjgl.input.Mouse;

import com.ade.content.IHasModel;
import com.ade.content.entities.EntityBullet;
import com.ade.core.ADE;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemGun extends Item implements IHasModel
{
	//Measured in ticks
	public int fireDelay;
	
	public double damage;
	public float inaccuracy;
	public float zoomAmt;
	public EnumFireMode fireMode;
	
	private int currentAmmoAmount;
		
	public ItemGun(String name, int fireDelay, double damage, float inaccuracy, float zoomAmt, int ammoAmount, EnumFireMode fireMode) 
	{
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setMaxStackSize(1);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.fireDelay = fireDelay;
        this.damage = damage;
        this.inaccuracy = inaccuracy;
        this.zoomAmt = zoomAmt;
        this.fireMode = fireMode;
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
    
	public ActionResult<ItemStack> shootGun(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        boolean flag = !this.findAmmo(playerIn).isEmpty();
        
		ItemStack stack = this.findAmmo(playerIn);
		if(stack != ItemStack.EMPTY || playerIn.capabilities.isCreativeMode)
		{
			if(!worldIn.isRemote)
			{
				EntityBullet bullet = new EntityBullet(worldIn, playerIn, damage, inaccuracy);
				worldIn.spawnEntity(bullet);
			}
			
			if(!playerIn.capabilities.isCreativeMode) 
			{
				stack.shrink(1);
			}
						
			if(stack.isEmpty())
			{
				playerIn.inventory.deleteStack(stack);
			}
			
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	public ActionResult<ItemStack> reloadGun(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	public void setAmmoAmount(ItemStack stack, int ammo) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("ammoAmount", ammo);
	}
	
	
	public int getAmmoAmount(ItemStack stack) 
	{
		if(stack.hasTagCompound())
			return stack.getTagCompound().getInteger("ammoAmount");
		return currentAmmoAmount;
	}
	
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        return true;
    }
	
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if(entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).inventory.getCurrentItem() == stack) 
		{
			EntityPlayer playerIn = (EntityPlayer)entityIn;
			
			if(playerIn.isSneaking()) 
			{
				inaccuracy = inaccuracy / 2;
			}
			
			if(Mouse.isButtonDown(0)) 
			{
				if(!(Minecraft.getMinecraft().currentScreen instanceof GuiScreen)) 
				{
					
				}
			}
		}
	}
	
	
	
	@Override
	public void registerModels() 
	{
		ADE.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
