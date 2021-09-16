package net.petersil98.utilcraft_weapons.utils;

import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.entities.UtilcraftWeaponsEntities;
import net.petersil98.utilcraft_weapons.renderer.BulletRenderer;

@Mod.EventBusSubscriber(modid = UtilcraftWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(UtilcraftWeaponsEntities.BULLET_ENTITY.get(), BulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(UtilcraftWeaponsEntities.SMOKE_GRENADE_ENTITY.get(), manager -> new SpriteRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
    }
}
