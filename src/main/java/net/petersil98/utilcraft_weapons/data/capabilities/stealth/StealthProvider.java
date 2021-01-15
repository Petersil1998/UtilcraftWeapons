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
    private final LazyOptional<IStealth> stealthOptional = LazyOptional.of(() -> stealth);

    public void invalidate() {
        stealthOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityStealth.STEALTH_CAPABILITY) {
            return stealthOptional.cast();
        } else {
            return LazyOptional.empty();
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (CapabilityStealth.STEALTH_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityStealth.STEALTH_CAPABILITY.writeNBT(stealth, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityStealth.STEALTH_CAPABILITY != null) {
            CapabilityStealth.STEALTH_CAPABILITY.readNBT(stealth, null, nbt);
        }
    }
}