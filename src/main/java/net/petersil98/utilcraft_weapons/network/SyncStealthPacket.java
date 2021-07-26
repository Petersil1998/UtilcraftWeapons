package net.petersil98.utilcraft_weapons.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.petersil98.utilcraft_weapons.data.capabilities.stealth.CapabilityStealth;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SyncStealthPacket {

    private final boolean stealth;
    private final int entityID;

    public SyncStealthPacket(boolean stealth, int entityID) {
        this.stealth = stealth;
        this.entityID = entityID;
    }

    public SyncStealthPacket(@Nonnull PacketBuffer packetBuffer) {
        this.stealth = packetBuffer.readBoolean();
        this.entityID = packetBuffer.readInt();
    }

    public void encode(@Nonnull PacketBuffer buf) {
        buf.writeBoolean(stealth);
        buf.writeInt(entityID);
    }

    public boolean handle(@Nonnull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
            net.minecraft.client.entity.player.ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                Entity entity = player.level.getEntity(this.entityID);
                if (entity instanceof PlayerEntity) {
                    entity.getCapability(CapabilityStealth.STEALTH_CAPABILITY).ifPresent(iStealth -> iStealth.setStealth(this.stealth));
                }
            }
        }));
        return true;
    }
}
