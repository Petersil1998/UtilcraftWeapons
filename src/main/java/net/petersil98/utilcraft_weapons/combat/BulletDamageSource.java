package net.petersil98.utilcraft_weapons.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

import javax.annotation.Nonnull;

public class BulletDamageSource extends EntityDamageSource {

    public BulletDamageSource(Entity damageSource) {
        super("bullet", damageSource);
        setProjectile();
    }

    @Override
    @Nonnull
    public ITextComponent getLocalizedDeathMessage(@Nonnull LivingEntity killedEntity) {
        return new TranslationTextComponent(String.format("%s.kill.sniper", UtilcraftWeapons.MOD_ID), killedEntity.getDisplayName().getString(), this.entity.getDisplayName().getString());
    }
}
