package net.petersil98.utilcraft_weapons;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.petersil98.utilcraft.blocks.ModBlocks;
import net.petersil98.utilcraft.tile_entities.DisenchantmentTableTileEntity;
import net.petersil98.utilcraft.tile_entities.SecureChestTileEntity;
import net.petersil98.utilcraft_weapons.effects.SneakEffect;
import net.petersil98.utilcraft_weapons.entities.BulletEntity;
import net.petersil98.utilcraft_weapons.entities.UtilcraftWeaponsEntities;
import net.petersil98.utilcraft_weapons.items.AssassinsKnife;
import net.petersil98.utilcraft_weapons.items.BulletItem;
import net.petersil98.utilcraft_weapons.items.SniperRifle;
import net.petersil98.utilcraft_weapons.renderer.BulletRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod(UtilcraftWeapons.MOD_ID)
public class UtilcraftWeapons
{
    public static final String MOD_ID = "utilcraft_weapons";

    private static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup itemGroup = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.GOLD_BRICK);
        }
    };

    public UtilcraftWeapons() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(UtilcraftWeaponsEntities.BULLET_ENTITY, BulletRenderer::new);
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(UtilcraftWeapons.MOD_ID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
            itemRegistryEvent.getRegistry().register(new AssassinsKnife().setRegistryName("assassins_knife"));
            itemRegistryEvent.getRegistry().register(new SniperRifle().setRegistryName("sniper_rifle"));
            itemRegistryEvent.getRegistry().register(new BulletItem().setRegistryName("bullet"));
        }

        @SubscribeEvent
        public static void registerEffects(final RegistryEvent.Register<Effect> effectRegistryEvent) {
            effectRegistryEvent.getRegistry().register(new SneakEffect().setRegistryName("sneak"));
        }

        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> entityRegister) {
            entityRegister.getRegistry().register(EntityType.Builder.<BulletEntity>create(BulletEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).trackingRange(4).func_233608_b_(20).build("bullet_entity").setRegistryName("bullet_entity"));
        }
    }
}
