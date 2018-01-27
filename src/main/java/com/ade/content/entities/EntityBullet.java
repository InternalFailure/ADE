package com.ade.content.entities;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBullet extends Entity 
{
	
	//Positioning
	public float x, y, z;
	
	public double damage;
	public Entity shootingEntity;
	
	public EntityBullet(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntityBullet(World worldIn, EntityPlayer shooterIn, double damage, float x, float y, float z) 
	{
		super(worldIn);
		this.damage = damage;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public EntityBullet(World worldIn, EntityLivingBase shooter) 
	{
		super(worldIn);
	}

	@Override
	protected void entityInit() 
	{
		
	}
	
	public void onUpdate() 
	{
		super.onUpdate();
		posX += x;
		posY += y;
		posZ += z;
		
        Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
        vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		
        if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult))
        {
            this.onHit(raytraceresult);
        }
		
	}
	protected void onHit(RayTraceResult raytraceresult)
	{
		if (this.shootingEntity == null)
        {
            DamageSource.causeThrownDamage(this, this);
        }
        else
        {
            DamageSource.causeThrownDamage(this, this.shootingEntity);
        }
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		
	}
	
}
