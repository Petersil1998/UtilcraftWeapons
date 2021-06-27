package net.petersil98.utilcraft_weapons.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.CapabilityStealth;
import net.petersil98.utilcraft_weapons.network.PacketHandler;
import net.petersil98.utilcraft_weapons.network.SyncStealthPacket;

import javax.annotation.Nonnull;

public class StealthEffect extends Effect {

    public StealthEffect(){
        super(EffectType.BENEFICIAL, 745784);
    }

    @Override
    public void applyAttributesModifiersToEntity(@Nonnull LivingEntity entityLivingBase, @Nonnull AttributeModifierManager attributeMap, int amplifier) {
        super.applyAttributesModifiersToEntity(entityLivingBase, attributeMap, amplifier);
        if(entityLivingBase instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLivingBase;
            player.getCapability(CapabilityStealth.STEALTH_CAPABILITY).ifPresent(iStealth -> iStealth.setStealth(true));
            PacketHandler.sendToClients(new SyncStealthPacket(true, player.getEntityId()), player);
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(@Nonnull LivingEntity entityLivingBase, @Nonnull AttributeModifierManager attributeMap, int amplifier) {
        super.removeAttributesModifiersFromEntity(entityLivingBase, attributeMap, amplifier);
        if(entityLivingBase instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLivingBase;
            player.getCapability(CapabilityStealth.STEALTH_CAPABILITY).ifPresent(iStealth -> iStealth.setStealth(false));
            PacketHandler.sendToClients(new SyncStealthPacket(false, player.getEntityId()), player);
        }
    }
}
