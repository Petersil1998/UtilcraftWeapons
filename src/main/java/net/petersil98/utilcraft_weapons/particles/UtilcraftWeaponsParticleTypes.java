package net.petersil98.utilcraft_weapons.particles;

import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class UtilcraftWeaponsParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, UtilcraftWeapons.MOD_ID);

    public static final RegistryObject<SmokeCloudParticleFactory.SmokeCloudParticleType> SMOKE_CLOUD = PARTICLES.register("smoke_cloud", SmokeCloudParticleFactory.SmokeCloudParticleType::new);
}
