package net.petersil98.utilcraft_weapons.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class UtilcraftWeaponsEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, UtilcraftWeapons.MOD_ID);

    public static final RegistryObject<StealthEffect> STEALTH = EFFECTS.register("stealth", StealthEffect::new);
}
