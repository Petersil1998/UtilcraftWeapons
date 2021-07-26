package net.petersil98.utilcraft_weapons.combat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

import javax.annotation.Nonnull;

public class BulletDamageSource extends EntityDamageSource {

    public BulletDamageSource(Entity damageSource) {
        super("bullet", damageSource);
        setProjectile();
    }

    @Override
    @Nonnull
    public Component getLocalizedDeathMessage(@Nonnull LivingEntity killedEntity) {
        return new TranslatableComponent(String.format("%s.kill.sniper", UtilcraftWeapons.MOD_ID), killedEntity.getDisplayName().getString(), this.entity.getDisplayName().getString());
    }
}
