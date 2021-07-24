package net.petersil98.utilcraft_weapons.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.network.NetworkHooks;
import net.petersil98.utilcraft_weapons.items.UtilcraftWeaponsItems;
import net.petersil98.utilcraft_weapons.particles.SmokeCloudParticleData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SmokeGrenadeEntity extends ProjectileItemEntity {

    private boolean hasLanded;
    private int radius;
    private int color;

    public static final DataParameter<Integer> COLOR_DATA = EntityDataManager.defineId(SmokeGrenadeEntity.class, DataSerializers.INT);

    public SmokeGrenadeEntity(EntityType<? extends SmokeGrenadeEntity> entityType, World world) {
        super(entityType, world);
        this.hasLanded = false;
        this.radius = 5;
    }

    public SmokeGrenadeEntity(World world, LivingEntity thrower, int color) {
        super(UtilcraftWeaponsEntities.SMOKE_GRENADE_ENTITY, thrower, world);
        this.getEntityData().set(COLOR_DATA, color);
        this.color = color;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(COLOR_DATA, color);
    }

    @Nonnull
    protected Item getDefaultItem() {
        return UtilcraftWeaponsItems.SMOKE_GRENADE;
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(@Nonnull EntityRayTraceResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        rayTraceResult.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onHit(@Nonnull RayTraceResult result) {
        super.onHit(result);
        this.hasLanded = true;
        if (!this.level.isClientSide && this.isAlive()) {
            this.remove();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive() && !this.hasLanded) {
            this.remove();
        } else {
            super.tick();
            if(this.hasLanded) {
                this.renderParticles();
                this.hasLanded = false;
            }
        }
    }

    @Nullable
    public Entity changeDimension(@Nonnull ServerWorld server, @Nonnull ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level.dimension() != server.dimension()) {
            this.setOwner(null);
        }

        return super.changeDimension(server, teleporter);
    }

    @Override
    @Nonnull
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void renderParticles() {
        for(double x = this.radius; x > -this.radius; x -= 0.2) {
            for (double z = this.radius; z > -this.radius; z -= 0.2) {
                for(double y = 0; y < this.radius; y += 0.4) {
                    double distance = Math.sqrt(x * x + z * z);
                    double distance3D = Math.sqrt(y * y + distance * distance);
                    if (distance3D <= this.radius && distance3D > this.radius-(this.radius/5.0)) {
                        this.level.addParticle(new SmokeCloudParticleData(this.getEntityData().get(COLOR_DATA)), this.getX() + x, this.getY() + y, this.getZ() + z, 0, 0, 0);
                    }
                }
            }
        }
    }
}
