package net.petersil98.utilcraft_weapons.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.petersil98.utilcraft_weapons.combat.BulletDamageSource;

import javax.annotation.Nonnull;

public class BulletEntity extends Projectile {

    private float damage = 5f;

    public BulletEntity(EntityType<? extends BulletEntity> type, Level world) {
        super(type, world);
    }

    public BulletEntity(Level world, double x, double y, double z) {
        this(UtilcraftWeaponsEntities.BULLET_ENTITY.get(), world);
        this.setPos(x, y, z);
    }

    public BulletEntity(Level world, @Nonnull LivingEntity shooter) {
        this(world, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ());
        this.setOwner(shooter);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double f = motion.horizontalDistance();
            this.setYRot((float)(Mth.atan2(motion.x, motion.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(motion.y, f) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (this.isInWaterOrRain()) {
            this.clearFire();
        }
        Vec3 positionVec = this.position();
        Vec3 positionMoved = positionVec.add(motion);
        HitResult raytraceresult = this.level.clip(new ClipContext(positionVec, positionMoved, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            positionMoved = raytraceresult.getLocation();
        }

        while(isAlive()) {
            EntityHitResult entityraytraceresult = this.rayTraceEntities(positionVec, positionMoved);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }

            if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)raytraceresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }

            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
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
        double f1 = motion.horizontalDistance();
        this.setYRot((float)(Mth.atan2(motionX, motionZ) * (double)(180F / (float)Math.PI)));

        this.setXRot((float)(Mth.atan2(motionY, f1) * (double)(180F / (float)Math.PI)));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        float drag = 0.99F;
        if (this.isInWater()) {
            for(int j = 0; j < 4; ++j) {
                this.level.addParticle(ParticleTypes.BUBBLE, newX - motionX * 0.25D, newY - motionY * 0.25D, newZ - motionZ * 0.25D, motionX, motionY, motionZ);
            }
        }

        this.setDeltaMovement(motion.scale(drag));
        if (!this.isNoGravity()) {
            Vec3 vector3d4 = this.getDeltaMovement();
            this.setDeltaMovement(vector3d4.x, vector3d4.y - (double)0.05F, vector3d4.z);
        }

        this.setPos(newX, newY, newZ);
        this.checkInsideBlocks();
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        Entity hitEntity = rayTraceResult.getEntity();

        Entity shootingEntity = this.getOwner();

        DamageSource damagesource = new BulletDamageSource(shootingEntity);
        if (shootingEntity instanceof LivingEntity) {
            ((LivingEntity)shootingEntity).setLastHurtMob(hitEntity);
        }

        if (hitEntity.hurt(damagesource, getDamage())) {
            if (hitEntity instanceof LivingEntity livingentity) {

                if (!this.level.isClientSide && shootingEntity instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, shootingEntity);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)shootingEntity, livingentity);
                }

                if (livingentity != shootingEntity && livingentity instanceof Player && shootingEntity instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)shootingEntity).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
        }
        this.remove(RemovalReason.DISCARDED);
    }

    protected EntityHitResult rayTraceEntities(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(this.level, this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }
}
