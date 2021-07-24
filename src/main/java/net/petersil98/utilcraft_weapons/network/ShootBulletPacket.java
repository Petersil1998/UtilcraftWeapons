package net.petersil98.utilcraft_weapons.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.petersil98.utilcraft_weapons.items.SniperRifle;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ShootBulletPacket {

    public ShootBulletPacket() {
    }

    public ShootBulletPacket(@Nonnull PacketBuffer packetBuffer) {
    }

    public void encode(@Nonnull PacketBuffer buf) {
    }

    public boolean handle(@Nonnull Supplier<NetworkEvent.Context> ctx) {
        AtomicBoolean successful = new AtomicBoolean(true);
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player != null) {
                Item item = player.getItemInHand(player.getUsedItemHand()).getItem();
                if(item instanceof SniperRifle) {
                    successful.set(((SniperRifle) item).shootBullet(player.level, player, player.getUsedItemHand()));
                }
            }
        });
        return successful.get();
    }
}
