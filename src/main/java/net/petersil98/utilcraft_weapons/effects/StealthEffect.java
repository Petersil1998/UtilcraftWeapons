package net.petersil98.utilcraft_weapons.effects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.CapabilityStealth;
import net.petersil98.utilcraft_weapons.network.PacketHandler;
import net.petersil98.utilcraft_weapons.network.SyncStealthPacket;

import javax.annotation.Nonnull;

public class StealthEffect extends MobEffect {

    public StealthEffect(){
        super(MobEffectCategory.BENEFICIAL, 745784);
    }

    @Override
    public void addAttributeModifiers(@Nonnull LivingEntity entityLivingBase, @Nonnull AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entityLivingBase, attributeMap, amplifier);
        if(entityLivingBase instanceof ServerPlayer player) {
            player.getCapability(CapabilityStealth.STEALTH_CAPABILITY).ifPresent(iStealth -> iStealth.setStealth(true));
            PacketHandler.sendToClients(new SyncStealthPacket(true, player.getId()), player);
        }
    }

    @Override
    public void removeAttributeModifiers(@Nonnull LivingEntity entityLivingBase, @Nonnull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entityLivingBase, attributeMap, amplifier);
        if(entityLivingBase instanceof ServerPlayer player) {
            player.getCapability(CapabilityStealth.STEALTH_CAPABILITY).ifPresent(iStealth -> iStealth.setStealth(false));
            PacketHandler.sendToClients(new SyncStealthPacket(false, player.getId()), player);
        }
    }
}
