package net.petersil98.utilcraft_weapons.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class UtilcraftWeaponsEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, UtilcraftWeapons.MOD_ID);

    public static final RegistryObject<EntityType<BulletEntity>> BULLET_ENTITY = ENTITIES.register("bullet_entity", () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("bullet_entity"));
    public static final RegistryObject<EntityType<SmokeGrenadeEntity>> SMOKE_GRENADE_ENTITY = ENTITIES.register("smoke_grenade_entity", () -> EntityType.Builder.<SmokeGrenadeEntity>of(SmokeGrenadeEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("smoke_grenade_entity"));
}
