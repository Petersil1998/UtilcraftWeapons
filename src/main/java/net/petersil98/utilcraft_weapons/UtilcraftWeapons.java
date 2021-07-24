package net.petersil98.utilcraft_weapons;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.CapabilityStealth;
import net.petersil98.utilcraft_weapons.effects.StealthEffect;
import net.petersil98.utilcraft_weapons.entities.BulletEntity;
import net.petersil98.utilcraft_weapons.entities.SmokeGrenadeEntity;
import net.petersil98.utilcraft_weapons.entities.UtilcraftWeaponsEntities;
import net.petersil98.utilcraft_weapons.items.*;
import net.petersil98.utilcraft_weapons.network.PacketHandler;
import net.petersil98.utilcraft_weapons.particles.SmokeCloudParticleFactory;
import net.petersil98.utilcraft_weapons.particles.UtilcraftWeaponsParticleTypes;
import net.petersil98.utilcraft_weapons.renderer.BulletRenderer;

import javax.annotation.Nonnull;

@Mod(UtilcraftWeapons.MOD_ID)
public class UtilcraftWeapons
{
    public static final String MOD_ID = "utilcraft_weapons";

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {

        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(UtilcraftWeaponsItems.ASSASSINS_KNIFE);
        }
    };

    public UtilcraftWeapons() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        CapabilityStealth.register();
        PacketHandler.registerMessages();
    }

    private void doClientStuff(@Nonnull final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(UtilcraftWeaponsEntities.BULLET_ENTITY, BulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(UtilcraftWeaponsEntities.SMOKE_GRENADE_ENTITY, manager -> new SpriteRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        }

        @SubscribeEvent
        public static void registerItems(@Nonnull final RegistryEvent.Register<Item> itemRegistryEvent) {
            itemRegistryEvent.getRegistry().register(new AssassinsKnife().setRegistryName("assassins_knife"));
            itemRegistryEvent.getRegistry().register(new SniperRifle().setRegistryName("sniper_rifle"));
            itemRegistryEvent.getRegistry().register(new BulletItem().setRegistryName("bullet"));
            itemRegistryEvent.getRegistry().register(new SmokeGrenade().setRegistryName("smoke_grenade"));
        }

        @SubscribeEvent
        public static void registerEffects(@Nonnull final RegistryEvent.Register<Effect> effectRegistryEvent) {
            effectRegistryEvent.getRegistry().register(new StealthEffect().setRegistryName("stealth"));
        }

        @SubscribeEvent
        public static void registerEntities(@Nonnull final RegistryEvent.Register<EntityType<?>> entityRegister) {
            entityRegister.getRegistry().register(EntityType.Builder.<BulletEntity>of(BulletEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("bullet_entity").setRegistryName("bullet_entity"));
            entityRegister.getRegistry().register(EntityType.Builder.<SmokeGrenadeEntity>of(SmokeGrenadeEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("smoke_grenade_entity").setRegistryName("smoke_grenade_entity"));
        }
        @SubscribeEvent
        public static void registerParticleTypes(@Nonnull final RegistryEvent.Register<ParticleType<?>> particleTypeRegister) {
            particleTypeRegister.getRegistry().register(new SmokeCloudParticleFactory.SmokeCloudParticleType().setRegistryName("smoke_cloud"));
        }
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
    public static class RegistryParticle {

        @SubscribeEvent
        public static void registerParticles(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particleEngine.register(UtilcraftWeaponsParticleTypes.SMOKE_CLOUD, SmokeCloudParticleFactory::new);
        }
    }
}
