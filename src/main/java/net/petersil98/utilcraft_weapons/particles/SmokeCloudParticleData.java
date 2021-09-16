package net.petersil98.utilcraft_weapons.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ColorHelper;

import javax.annotation.Nonnull;

public class SmokeCloudParticleData implements IParticleData {

    private final int color;

    public SmokeCloudParticleData(int color) {
        this.color = color;
    }

    @Nonnull
    @Override
    public ParticleType<?> getType() {
        return UtilcraftWeaponsParticleTypes.SMOKE_CLOUD.get();
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public void writeToNetwork(PacketBuffer buffer) {
        buffer.writeInt(ColorHelper.PackedColor.red(this.color));
        buffer.writeInt(ColorHelper.PackedColor.green(this.color));
        buffer.writeInt(ColorHelper.PackedColor.blue(this.color));
    }

    @Nonnull
    @Override
    public String writeToString() {
        return String.format("%d %d %d", ColorHelper.PackedColor.red(this.color), ColorHelper.PackedColor.green(this.color), ColorHelper.PackedColor.blue(this.color));
    }

    public static final Codec<SmokeCloudParticleData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(d -> d.color)
            ).apply(instance, SmokeCloudParticleData::new)
    );

    public static IDeserializer<SmokeCloudParticleData> DESERIALIZER = new IDeserializer<SmokeCloudParticleData>() {

        @Nonnull
        @Override
        public SmokeCloudParticleData fromCommand(@Nonnull ParticleType<SmokeCloudParticleData> particleTypeIn, @Nonnull StringReader reader) throws CommandSyntaxException {
            return new SmokeCloudParticleData(ColorHelper.PackedColor.color(255, reader.readInt(), reader.readInt(), reader.readInt()));
        }

        @Nonnull
        @Override
        public SmokeCloudParticleData fromNetwork(@Nonnull ParticleType<SmokeCloudParticleData> particleType, @Nonnull PacketBuffer buffer) {
            return new SmokeCloudParticleData(ColorHelper.PackedColor.color(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }
    };
}
