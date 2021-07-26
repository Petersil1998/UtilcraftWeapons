package net.petersil98.utilcraft_weapons.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1.0";
    private static int id = 0;
    private static SimpleChannel INSTANCE;

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(UtilcraftWeapons.MOD_ID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        INSTANCE.messageBuilder(ShootBulletPacket.class, id++)
                .encoder(ShootBulletPacket::encode)
                .decoder(ShootBulletPacket::new)
                .consumer(ShootBulletPacket::handle)
                .add();

        INSTANCE.messageBuilder(SyncStealthPacket.class, id++)
                .encoder(SyncStealthPacket::encode)
                .decoder(SyncStealthPacket::new)
                .consumer(SyncStealthPacket::handle)
                .add();
    }

    public static void sendToServer(ShootBulletPacket myPacket){
        INSTANCE.sendToServer(myPacket);
    }

    public static void sendToClients(SyncStealthPacket myPacket, Player affectedEntity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> affectedEntity), myPacket);
    }
}
