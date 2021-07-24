package net.petersil98.utilcraft_weapons.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

import javax.annotation.Nonnull;

public class KnifeDamageSource extends EntityDamageSource {

    public KnifeDamageSource(Entity damageSource) {
        super("knife", damageSource);
    }

    @Override
    @Nonnull
    public ITextComponent getLocalizedDeathMessage(@Nonnull LivingEntity killedEntity) {
        return new TranslationTextComponent(String.format("%s.kill.knife", UtilcraftWeapons.MOD_ID), killedEntity.getDisplayName().getString(), this.entity.getDisplayName().getString());
    }
}
