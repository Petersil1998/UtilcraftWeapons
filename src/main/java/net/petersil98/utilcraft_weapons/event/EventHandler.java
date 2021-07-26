package net.petersil98.utilcraft_weapons.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.PotionColorCalculationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.combat.KnifeDamageSource;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.CapabilityStealth;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.StealthProvider;
import net.petersil98.utilcraft_weapons.effects.StealthEffect;
import net.petersil98.utilcraft_weapons.items.AssassinsKnife;
import net.petersil98.utilcraft_weapons.items.SniperRifle;
import net.petersil98.utilcraft_weapons.network.PacketHandler;
import net.petersil98.utilcraft_weapons.network.SyncStealthPacket;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = UtilcraftWeapons.MOD_ID)
public class EventHandler {



    @SubscribeEvent
    public static void onFOVUpdateEvent(@Nonnull FOVUpdateEvent event) {
        Player player = event.getEntity();
        if(player.getMainHandItem().getItem() instanceof SniperRifle && ((SniperRifle) player.getMainHandItem().getItem()).isZoomedIn()) {
            event.setNewfov(event.getNewfov() * 0.35f);
        }
    }

    @SubscribeEvent
    public static void potionColorCalculationEvent(@Nonnull PotionColorCalculationEvent event) {
        event.getEffects().forEach(effectInstance -> {
            if(effectInstance.getEffect() instanceof StealthEffect) {
                event.shouldHideParticles(true);
            }
        });
    }

    @SubscribeEvent
    public static void livingAttackEvent(@Nonnull LivingAttackEvent event) {
        if(event.getSource() instanceof EntityDamageSource damageSource && !(event.getSource() instanceof KnifeDamageSource)) {
            if(damageSource.getEntity() instanceof ServerPlayer attacker) {
                if (attacker.getMainHandItem().getItem() instanceof AssassinsKnife) {
                    event.setCanceled(true);
                    event.getEntity().hurt(new KnifeDamageSource(damageSource.getEntity()), event.getAmount());
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerLoginEvent(@Nonnull PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(CapabilityStealth.STEALTH_CAPABILITY).ifPresent(iStealth -> PacketHandler.sendToClients(new SyncStealthPacket(iStealth.isStealth(), player.getId()), player));
        }
    }

    @SubscribeEvent
    public static void attachStealthCapability(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            StealthProvider provider = new StealthProvider();
            event.addCapability(new ResourceLocation(UtilcraftWeapons.MOD_ID, "stealth"), provider);
            event.addListener(provider::invalidate);
        }
    }
}
