package net.petersil98.utilcraft_weapons.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.CapabilityStealth;
import net.petersil98.utilcraft_weapons.items.SniperRifle;
import net.petersil98.utilcraft_weapons.network.PacketHandler;
import net.petersil98.utilcraft_weapons.network.ShootBulletPacket;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = UtilcraftWeapons.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final Field shadowSize = ObfuscationReflectionHelper.findField(EntityRenderer.class, "f_114477_");
    private static float defaultShadowSize;

    @SubscribeEvent
    public static void renderPlayerEvent(@Nonnull RenderPlayerEvent event) {
        Player playerEntity = event.getPlayer();
        playerEntity.getCapability(CapabilityStealth.STEALTH_CAPABILITY).ifPresent(iStealth -> {
            try {
                float f = shadowSize.getFloat(event.getRenderer());
                if (f != 0.0f) {
                    defaultShadowSize = f;
                }
                if (iStealth.isStealth()) {
                    shadowSize.setFloat(event.getRenderer(), 0.0f);
                    event.setCanceled(true);
                } else {
                    shadowSize.setFloat(event.getRenderer(), defaultShadowSize);
                }
            } catch (IllegalAccessException ignored) {}
        });
    }

    @SubscribeEvent
    public static void onMouseClickEvent(@Nonnull InputEvent.MouseInputEvent event) {
        Player player = Minecraft.getInstance().player;
        if(player != null && player.getMainHandItem().getItem() instanceof SniperRifle && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && event.getAction() == GLFW.GLFW_PRESS) {
            PacketHandler.sendToServer(new ShootBulletPacket());
        }
    }
}
