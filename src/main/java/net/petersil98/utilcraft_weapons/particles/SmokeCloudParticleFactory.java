package net.petersil98.utilcraft_weapons.particles;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;

import javax.annotation.Nonnull;

public record SmokeCloudParticleFactory(
        SpriteSet spriteSet) implements ParticleProvider<SmokeCloudParticleData> {

    @Override
    public Particle createParticle(@Nonnull SmokeCloudParticleData data, @Nonnull ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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
