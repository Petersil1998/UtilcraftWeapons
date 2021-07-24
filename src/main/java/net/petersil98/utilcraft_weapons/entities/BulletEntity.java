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
        this.setPos(x, y, z);
    }

    public BulletEntity(World world, @Nonnull LivingEntity shooter) {
        this(world, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ());
        this.setOwner(shooter);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    @Nonnull
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        Vector3d motion = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            float f = MathHelper.sqrt(getHorizontalDistanceSqr(motion));
            this.yRot = (float)(MathHelper.atan2(motion.x, motion.z) * (double)(180F / (float)Math.PI));
            this.xRot = (float)(MathHelper.atan2(motion.y, f) * (double)(180F / (float)Math.PI));
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
        }

        if (this.isInWaterOrRain()) {
            this.clearFire();
        }
        Vector3d positionVec = this.position();
        Vector3d positionMoved = positionVec.add(motion);
        RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(positionVec, positionMoved, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            positionMoved = raytraceresult.getLocation();
        }

        while(isAlive()) {
            EntityRayTraceResult entityraytraceresult = this.rayTraceEntities(positionVec, positionMoved);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }

            if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult)raytraceresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity)entity1).canHarmPlayer((PlayerEntity)entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }

            if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
                this.hasImpulse = true;
            }

            if (entityraytraceresult == null) {
                break;
            }

            raytraceresult = null;
        }

        motion = this.getDeltaMovement();
        double motionX = motion.x;
        double motionY = motion.y;
        double motionZ = motion.z;

        double newX = this.getX() + motionX;
        double newY = this.getY() + motionY;
        double newZ = this.getZ() + motionZ;
        float f1 = MathHelper.sqrt(getHorizontalDistanceSqr(motion));
        this.yRot = (float)(MathHelper.atan2(motionX, motionZ) * (double)(180F / (float)Math.PI));

        this.xRot = (float)(MathHelper.atan2(motionY, f1) * (double)(180F / (float)Math.PI));
        this.xRot = lerpRotation(this.xRotO, this.xRot);
        this.yRot = lerpRotation(this.yRotO, this.yRot);
        float drag = 0.99F;
        if (this.isInWater()) {
            for(int j = 0; j < 4; ++j) {
                this.level.addParticle(ParticleTypes.BUBBLE, newX - motionX * 0.25D, newY - motionY * 0.25D, newZ - motionZ * 0.25D, motionX, motionY, motionZ);
            }
        }

        this.setDeltaMovement(motion.scale(drag));
        if (!this.isNoGravity()) {
            Vector3d vector3d4 = this.getDeltaMovement();
            this.setDeltaMovement(vector3d4.x, vector3d4.y - (double)0.05F, vector3d4.z);
        }

        this.setPos(newX, newY, newZ);
        this.checkInsideBlocks();
    }

    @Override
    protected void onHitEntity(@Nonnull EntityRayTraceResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        Entity hitEntity = rayTraceResult.getEntity();

        Entity shootingEntity = this.getOwner();

        DamageSource damagesource = new BulletDamageSource(shootingEntity);
        if (shootingEntity instanceof LivingEntity) {
            ((LivingEntity)shootingEntity).setLastHurtMob(hitEntity);
        }

        if (hitEntity.hurt(damagesource, getDamage())) {
            if (hitEntity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)hitEntity;

                if (!this.level.isClientSide && shootingEntity instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, shootingEntity);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)shootingEntity, livingentity);
                }

                if (livingentity != shootingEntity && livingentity instanceof PlayerEntity && shootingEntity instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)shootingEntity).connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.yRot += 180.0F;
            this.yRotO += 180.0F;
        }
        this.remove();
    }

    protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec) {
        return ProjectileHelper.getEntityHitResult(this.level, this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }
}
