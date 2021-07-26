package net.petersil98.utilcraft_weapons.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.FastColor;

import javax.annotation.Nonnull;

public record SmokeCloudParticleData(int color) implements ParticleOptions {

    @Nonnull
    @Override
    public ParticleType<?> getType() {
        return UtilcraftWeaponsParticleTypes.SMOKE_CLOUD;
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(FastColor.ARGB32.red(this.color));
        buffer.writeInt(FastColor.ARGB32.green(this.color));
        buffer.writeInt(FastColor.ARGB32.blue(this.color));
    }

    @Nonnull
    @Override
    public String writeToString() {
        return String.format("%d %d %d", FastColor.ARGB32.red(this.color), FastColor.ARGB32.green(this.color), FastColor.ARGB32.blue(this.color));
    }

    public static final Codec<SmokeCloudParticleData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(d -> d.color)
            ).apply(instance, SmokeCloudParticleData::new)
    );

    public static Deserializer<SmokeCloudParticleData> DESERIALIZER = new Deserializer<SmokeCloudParticleData>() {

        @Nonnull
        @Override
        public SmokeCloudParticleData fromCommand(@Nonnull ParticleType<SmokeCloudParticleData> particleTypeIn, @Nonnull StringReader reader) throws CommandSyntaxException {
            return new SmokeCloudParticleData(FastColor.ARGB32.color(255, reader.readInt(), reader.readInt(), reader.readInt()));
        }

        @Nonnull
        @Override
        public SmokeCloudParticleData fromNetwork(@Nonnull ParticleType<SmokeCloudParticleData> particleType, @Nonnull FriendlyByteBuf buffer) {
            return new SmokeCloudParticleData(FastColor.ARGB32.color(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }
    };
}
