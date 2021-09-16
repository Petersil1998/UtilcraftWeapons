package net.petersil98.utilcraft_weapons.data.capabilities.stealth;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityStealth {
    @CapabilityInject(IStealth.class)
    public static Capability<IStealth> STEALTH_CAPABILITY = null;

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IStealth.class);
    }
}
