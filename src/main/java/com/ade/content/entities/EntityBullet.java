package com.ade.content.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityBullet extends Entity implements IEntityAdditionalSpawnData {
	
	private static final Predicate<? super Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, entity -> entity.canBeCollidedWith());
	
	private int shooterId;
	private EntityLivingBase shooter;
	public double damage;
	private ItemStack item = ItemStack.EMPTY;
	
	public EntityBullet(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntityBullet(World worldIn, EntityLivingBase shooter, double damage, float inaccuracy)
    {
        this(worldIn);
        this.shooterId = shooter.getEntityId();
        this.shooter = shooter;
        this.damage = damage;

        double x, y, z;
        x = this.motionX;
        y = this.motionY;
        z = this.motionZ;
        
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        
		Vec3d dir = getVectorFromRotation(shooter.rotationPitch, shooter.getRotationYawHead());
        this.motionX = dir.x * 100.0F + shooter.motionX;
        this.motionY = dir.y * 100.0F;
        this.motionZ = dir.z * 100.0F + shooter.motionZ;
		updateHeading();

		this.setSize(1, 1);
		this.setPosition(shooter.posX + dir.x, shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D + dir.y, shooter.posZ + dir.z);
    }
	
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		updateHeading();

		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null)
		{
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = this.findEntityOnPath(vec3d1, vec3d);

		if (entity != null)
		{
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

			if (this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer))
			{
				raytraceresult = null;
			}
		}

		if (raytraceresult != null && !world.isRemote)
		{
			this.onHit(raytraceresult);
		}

		this.posX += this.motionX / 5.5F;
		this.posY += this.motionY / 5.5F;
		this.posZ += this.motionZ / 5.5F;

		this.setPosition(this.posX, this.posY, this.posZ);
		
		if(!world.isRemote) 
		{
			this.setDead();
		}
	}
	
	@Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ), ARROW_TARGETS);
        double closestDistance = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity hitEntity = list.get(i);

            if (hitEntity != this.shooter)
            {
				AxisAlignedBB axisalignedbb = hitEntity.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult result = axisalignedbb.calculateIntercept(start, end);

                if (result != null)
                {
                    double distanceToHit = start.squareDistanceTo(result.hitVec);

                    if (distanceToHit < closestDistance || closestDistance == 0.0D)
                    {
                        entity = hitEntity;
                        closestDistance = distanceToHit;
                    }
                }
            }
        }

        return entity;
    }
	
	protected void onHit(RayTraceResult raytraceResultIn)
    {
		Entity entity = raytraceResultIn.entityHit;

		if(entity != null)
		{
			if(entity.getEntityId() == this.shooterId) return;
			double damage = this.damage;
			
			DamageSource source = new EntityDamageSourceIndirect("bullet", this, shooter).setProjectile();
			entity.attackEntityFrom(source, (float)damage);
			entity.hurtResistantTime = 0;
			
			this.setDead();
			return;
		}

		if(raytraceResultIn.getBlockPos() != null)
		{
			BlockPos pos = raytraceResultIn.getBlockPos();
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if((block instanceof BlockBreakable || block instanceof BlockPane) && state.getMaterial() == Material.GLASS)
			{
				world.destroyBlock(raytraceResultIn.getBlockPos(), false);
			}
			if(block instanceof Block) 
			{
				world.spawnParticle(EnumParticleTypes.BLOCK_DUST, true, this.posX, this.posY, this.posZ, 1.0D, 1.0D, 1.0D);
			}
			if(!block.isReplaceable(world, raytraceResultIn.getBlockPos()))
			{
				this.setDead();
			}
		}
    }
	
	@Override
	public boolean shouldRenderInPass(int pass) 
	{
		return true;
	}

	@Override
	protected void entityInit() 
	{
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		buffer.writeInt(this.shooterId);
		buffer.writeFloat(this.rotationYaw);
		buffer.writeFloat(this.rotationPitch);
		ByteBufUtils.writeItemStack(buffer, item);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) 
	{
		this.shooterId = additionalData.readInt();
		this.rotationYaw = additionalData.readFloat();
		this.prevRotationYaw = this.rotationYaw;
		this.rotationPitch = additionalData.readFloat();
		this.prevRotationPitch = this.rotationPitch;
		this.item = ByteBufUtils.readItemStack(additionalData);
	}

	public void updateHeading()
	{
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
		this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	private Vec3d getVectorFromRotation(float pitch, float yaw)
	{
		float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
	}
}
