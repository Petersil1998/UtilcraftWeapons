package net.petersil98.utilcraft_weapons.data.capabilities.stealth;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StealthProvider implements ICapabilitySerializable<CompoundTag> {

    private final DefaultStealth stealth = new DefaultStealth();
    private final LazyOptional<IStealth> stealthOptional = LazyOptional.of(() -> this.stealth);

    public void invalidate() {
        this.stealthOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityStealth.STEALTH_CAPABILITY) {
            return this.stealthOptional.cast();
        } else {
            return LazyOptional.empty();
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        IStealth instance = stealthOptional.orElseThrow(() -> new IllegalArgumentException("Lazy optional is uninitialized"));
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("stealth", instance.isStealth());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        IStealth instance = stealthOptional.orElseThrow(() -> new IllegalArgumentException("Lazy optional is uninitialized"));
        boolean charge = nbt.getBoolean("stealth");
        instance.setStealth(charge);
    }
}