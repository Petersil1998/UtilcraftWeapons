package net.petersil98.utilcraft_weapons.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.petersil98.utilcraft_weapons.items.SniperRifle;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ShootBulletPacket {

    public ShootBulletPacket() {
    }

    public ShootBulletPacket(PacketBuffer packetBuffer) {
    }

    public void encode(PacketBuffer buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        AtomicBoolean successful = new AtomicBoolean(true);
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player != null) {
                Item item = player.getHeldItem(player.getActiveHand()).getItem();
                if(item instanceof SniperRifle) {
                    successful.set(((SniperRifle) item).shootBullet(player.world, player, player.getActiveHand()));
                }
            }
        });
        return successful.get();
    }
}
