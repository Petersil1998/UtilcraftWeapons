package net.petersil98.utilcraft_weapons.data.capabilities.stealth;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StealthProvider implements ICapabilitySerializable<CompoundNBT> {

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
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CapabilityStealth.STEALTH_CAPABILITY.writeNBT(this.stealth, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CapabilityStealth.STEALTH_CAPABILITY.readNBT(this.stealth, null, nbt);
    }
}