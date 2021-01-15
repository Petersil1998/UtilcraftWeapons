package net.petersil98.utilcraft_weapons.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
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

    public static void sendToClients(SyncStealthPacket myPacket, PlayerEntity affectedEntity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> affectedEntity), myPacket);
    }
}
