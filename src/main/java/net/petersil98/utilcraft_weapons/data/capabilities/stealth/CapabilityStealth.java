package net.petersil98.utilcraft_weapons.data.capabilities.stealth;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityStealth {
    @CapabilityInject(IStealth.class)
    public static Capability<IStealth> STEALTH_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IStealth.class, new Storage(), DefaultStealth::new);
    }

    public static class Storage implements Capability.IStorage<IStealth> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IStealth> capability, IStealth instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean("stealth", instance.isStealth());
            return tag;
        }

        @Override
        public void readNBT(Capability<IStealth> capability, IStealth instance, Direction side, INBT nbt) {
            boolean charge = ((CompoundNBT) nbt).getBoolean("stealth");
            instance.setStealth(charge);
        }
    }
}
