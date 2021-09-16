package net.petersil98.utilcraft_weapons.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class UtilcraftWeaponsItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UtilcraftWeapons.MOD_ID);

    public static final RegistryObject<AssassinsKnife> ASSASSINS_KNIFE = ITEMS.register("assassins_knife", () -> new AssassinsKnife(new Item.Properties().tab(UtilcraftWeapons.ITEM_GROUP)));
    public static final RegistryObject<SniperRifle> SNIPER_RIFLE = ITEMS.register("sniper_rifle", () -> new SniperRifle(new Item.Properties().stacksTo(1).tab(UtilcraftWeapons.ITEM_GROUP)
    ));
    public static final RegistryObject<BulletItem> BULLET_ITEM = ITEMS.register("bullet", () -> new BulletItem(new Item.Properties().tab(UtilcraftWeapons.ITEM_GROUP)));
    public static final RegistryObject<SmokeGrenade> SMOKE_GRENADE = ITEMS.register("smoke_grenade", () -> new SmokeGrenade(new Item.Properties().tab(UtilcraftWeapons.ITEM_GROUP)));
}
