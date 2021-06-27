package net.petersil98.utilcraft_weapons.entities;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.petersil98.utilcraft_weapons.combat.BulletDamageSource;

import javax.annotation.Nonnull;

public class BulletEntity extends ProjectileEntity {

    private float damage = 5f;

    public BulletEntity(EntityType<? extends BulletEntity> type, World world) {
        super(type, world);
    }

    public BulletEntity(World world, double x, double y, double z) {
        this(UtilcraftWeaponsEntities.BULLET_ENTITY, world);
        this.setPosition(x, y, z);
    }

    public BulletEntity(World world, @Nonnull LivingEntity shooter) {
        this(world, shooter.getPosX(), shooter.getPosYEye() - (double)0.1F, shooter.getPosZ());
        this.setShooter(shooter);
    }

    @Override
    protected void registerData() {

    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        Vector3d motion = this.getMotion();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(horizontalMag(motion));
            this.rotationYaw = (float)(MathHelper.atan2(motion.x, motion.z) * (double)(180F / (float)Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(motion.y, f) * (double)(180F / (float)Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        if (this.isWet()) {
            this.extinguish();
        }
        Vector3d positionVec = this.getPositionVec();
        Vector3d positionMoved = positionVec.add(motion);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(positionVec, positionMoved, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            positionMoved = raytraceresult.getHitVec();
        }

        while(isAlive()) {
            EntityRayTraceResult entityraytraceresult = this.rayTraceEntities(positionVec, positionMoved);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }

            if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult)raytraceresult).getEntity();
                Entity entity1 = this.func_234616_v_();
                if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity)entity1).canAttackPlayer((PlayerEntity)entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }

            if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
                this.isAirBorne = true;
            }

            if (entityraytraceresult == null) {
                break;
            }

            raytraceresult = null;
        }

        motion = this.getMotion();
        double motionX = motion.x;
        double motionY = motion.y;
        double motionZ = motion.z;

        double newX = this.getPosX() + motionX;
        double newY = this.getPosY() + motionY;
        double newZ = this.getPosZ() + motionZ;
        float f1 = MathHelper.sqrt(horizontalMag(motion));
        this.rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (double)(180F / (float)Math.PI));

        this.rotationPitch = (float)(MathHelper.atan2(motionY, f1) * (double)(180F / (float)Math.PI));
        this.rotationPitch = func_234614_e_(this.prevRotationPitch, this.rotationPitch);
        this.rotationYaw = func_234614_e_(this.prevRotationYaw, this.rotationYaw);
        float drag = 0.99F;
        if (this.isInWater()) {
            for(int j = 0; j < 4; ++j) {
                this.world.addParticle(ParticleTypes.BUBBLE, newX - motionX * 0.25D, newY - motionY * 0.25D, newZ - motionZ * 0.25D, motionX, motionY, motionZ);
            }
        }

        this.setMotion(motion.scale(drag));
        if (!this.hasNoGravity()) {
            Vector3d vector3d4 = this.getMotion();
            this.setMotion(vector3d4.x, vector3d4.y - (double)0.05F, vector3d4.z);
        }

        this.setPosition(newX, newY, newZ);
        this.doBlockCollisions();
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult rayTraceResult) {
        super.onEntityHit(rayTraceResult);
        Entity hitEntity = rayTraceResult.getEntity();

        Entity shootingEntity = this.func_234616_v_();

        DamageSource damagesource = new BulletDamageSource(shootingEntity);
        if (shootingEntity instanceof LivingEntity) {
            ((LivingEntity)shootingEntity).setLastAttackedEntity(hitEntity);
        }

        if (hitEntity.attackEntityFrom(damagesource, getDamage())) {
            if (hitEntity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)hitEntity;

                if (!this.world.isRemote && shootingEntity instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingentity, shootingEntity);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity)shootingEntity, livingentity);
                }

                if (livingentity != shootingEntity && livingentity instanceof PlayerEntity && shootingEntity instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)shootingEntity).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241770_g_, 0.0F));
                }
            }
        } else {
            this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
        }
        this.remove();
    }

    protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec) {
        return ProjectileHelper.rayTraceEntities(this.world, this, startVec, endVec, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), this::func_230298_a_);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }
}
