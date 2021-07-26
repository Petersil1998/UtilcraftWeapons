package net.petersil98.utilcraft_weapons.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.petersil98.utilcraft_weapons.items.SniperRifle;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ShootBulletPacket {

    public ShootBulletPacket() {
    }

    public ShootBulletPacket(@Nonnull FriendlyByteBuf packetBuffer) {
    }

    public void encode(@Nonnull FriendlyByteBuf buf) {
    }

    public boolean handle(@Nonnull Supplier<NetworkEvent.Context> ctx) {
        AtomicBoolean successful = new AtomicBoolean(true);
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
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
