package net.petersil98.utilcraft_weapons.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BulletEntity extends ProjectileEntity {

    private float damage = 5f;

    public BulletEntity(EntityType<? extends BulletEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BulletEntity(World worldIn, double x, double y, double z) {
        this(UtilcraftWeaponsEntities.BULLET_ENTITY, worldIn);
        this.setPosition(x, y, z);
    }

    public BulletEntity(World worldIn, LivingEntity shooter) {
        this(worldIn, shooter.getPosX(), shooter.getPosYEye() - (double)0.1F, shooter.getPosZ());
        this.setShooter(shooter);
    }

    @Override
    protected void registerData() {

    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        Entity entity = this.func_234616_v_();
        return new SSpawnObjectPacket(this, entity == null ? 0 : entity.getEntityId());
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }
}
