package net.petersil98.utilcraft_weapons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.CapabilityStealth;
import net.petersil98.utilcraft_weapons.effects.UtilcraftWeaponsEffects;
import net.petersil98.utilcraft_weapons.entities.UtilcraftWeaponsEntities;
import net.petersil98.utilcraft_weapons.items.UtilcraftWeaponsItems;
import net.petersil98.utilcraft_weapons.network.PacketHandler;
import net.petersil98.utilcraft_weapons.particles.SmokeCloudParticleFactory;
import net.petersil98.utilcraft_weapons.particles.UtilcraftWeaponsParticleTypes;
import net.petersil98.utilcraft_weapons.renderer.BulletRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(UtilcraftWeapons.MOD_ID)
public class UtilcraftWeapons
{
    public static final String MOD_ID = "utilcraft_weapons";

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MOD_ID) {

        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(UtilcraftWeaponsItems.ASSASSINS_KNIFE.get());
        }
    };

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public UtilcraftWeapons() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::registerCapabilities);

        UtilcraftWeaponsItems.ITEMS.register(eventBus);
        UtilcraftWeaponsEntities.ENTITIES.register(eventBus);
        UtilcraftWeaponsEffects.EFFECTS.register(eventBus);
        UtilcraftWeaponsParticleTypes.PARTICLES.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
    }

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        CapabilityStealth.register(event);
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
    public static class RegistryParticle {

        @SubscribeEvent
        public static void registerParticles(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particleEngine.register(UtilcraftWeaponsParticleTypes.SMOKE_CLOUD.get(), SmokeCloudParticleFactory::new);
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(UtilcraftWeaponsEntities.BULLET_ENTITY.get(), BulletRenderer::new);
            event.registerEntityRenderer(UtilcraftWeaponsEntities.SMOKE_GRENADE_ENTITY.get(), ThrownItemRenderer::new);
        }
    }
}
