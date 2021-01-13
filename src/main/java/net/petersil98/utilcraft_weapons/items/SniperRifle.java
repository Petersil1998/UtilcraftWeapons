package net.petersil98.utilcraft_weapons.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.entities.BulletEntity;

import javax.annotation.Nonnull;

public class SniperRifle extends Item {

    public SniperRifle() {
        super(new Properties()
                .maxStackSize(1)
                .group(UtilcraftWeapons.itemGroup)
        );
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        BulletEntity bullet = new BulletEntity(world, player);
        bullet.setDamage(5);
        bullet.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 1.0F);
        world.addEntity(bullet);
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }
}
