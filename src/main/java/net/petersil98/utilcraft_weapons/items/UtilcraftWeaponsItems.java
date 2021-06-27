package net.petersil98.utilcraft_weapons.items;

import net.minecraftforge.registries.ObjectHolder;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

@ObjectHolder(UtilcraftWeapons.MOD_ID)
public class UtilcraftWeaponsItems {

    @ObjectHolder("assassins_knife")
    public static AssassinsKnife ASSASSINS_KNIFE;

    @ObjectHolder("sniper_rifle")
    public static SniperRifle SNIPER_RIFLE;

    @ObjectHolder("bullet")
    public static BulletItem BULLET_ITEM;

    @ObjectHolder("smoke_grenade")
    public static SmokeGrenade SMOKE_GRENADE;
}
