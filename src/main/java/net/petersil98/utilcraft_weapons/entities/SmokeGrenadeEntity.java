package net.petersil98.utilcraft_weapons.entities;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.petersil98.utilcraft_weapons.items.UtilcraftWeaponsItems;
import net.petersil98.utilcraft_weapons.particles.SmokeCloudParticleData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SmokeGrenadeEntity extends ThrowableItemProjectile {

    private int radius;
    private int color;

    public static final EntityDataAccessor<Integer> COLOR_DATA = SynchedEntityData.defineId(SmokeGrenadeEntity.class, EntityDataSerializers.INT);

    public SmokeGrenadeEntity(EntityType<? extends SmokeGrenadeEntity> entityType, Level world) {
        super(entityType, world);
        this.radius = 5;
    }

    public SmokeGrenadeEntity(Level world, LivingEntity thrower, int color) {
        super(UtilcraftWeaponsEntities.SMOKE_GRENADE_ENTITY.get(), thrower, world);
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
        return UtilcraftWeaponsItems.SMOKE_GRENADE.get();
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        this.renderParticles();
        this.discard();
    }

    @Nullable
    public Entity changeDimension(@Nonnull ServerLevel server, @Nonnull ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level.dimension() != server.dimension()) {
            this.setOwner(null);
        }

        return super.changeDimension(server, teleporter);
    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
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
