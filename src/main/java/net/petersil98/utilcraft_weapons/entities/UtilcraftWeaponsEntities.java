package net.petersil98.utilcraft_weapons.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

@ObjectHolder(UtilcraftWeapons.MOD_ID)
public class UtilcraftWeaponsEntities {

    @ObjectHolder("bullet_entity")
    public static EntityType<BulletEntity> BULLET_ENTITY;

    @ObjectHolder("smoke_grenade_entity")
    public static EntityType<SmokeGrenadeEntity> SMOKE_GRENADE_ENTITY;
}
