package net.petersil98.utilcraft_weapons.particles;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;

public class SmokeCloudParticleFactory implements IParticleFactory<SmokeCloudParticleData> {

    private final IAnimatedSprite spriteSet;

    public SmokeCloudParticleFactory(IAnimatedSprite spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(@Nonnull SmokeCloudParticleData data, @Nonnull ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new SmokeCloudParticle(world, x, y, z, this.spriteSet, data.getColor());
    }

    public static class SmokeCloudParticleType extends ParticleType<SmokeCloudParticleData> {
        public SmokeCloudParticleType() {
            super(false, SmokeCloudParticleData.DESERIALIZER);
        }

        @Nonnull
        @Override
        public Codec<SmokeCloudParticleData> codec() {
            return SmokeCloudParticleData.CODEC;
        }
    }
}
