package net.petersil98.utilcraft_weapons.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.effects.UtilcraftWeaponsEffects;
import net.petersil98.utilcraft_weapons.items.SniperRifle;

@Mod.EventBusSubscriber(modid = UtilcraftWeapons.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onInputUpdateEvent(InputUpdateEvent event) {
        if(event.getPlayer() != null && event.getPlayer().getActivePotionEffect(UtilcraftWeaponsEffects.SNEAK) != null) {
            event.getMovementInput().sneaking = true;
        }
    }

    @SubscribeEvent
    public static void onFOVUpdateEvent(FOVUpdateEvent event) {
        PlayerEntity player = event.getEntity();
        if(player.getHeldItemMainhand().getItem() instanceof SniperRifle) {
            event.setNewfov(event.getFov() * 0.35f);
        }
    }
}
