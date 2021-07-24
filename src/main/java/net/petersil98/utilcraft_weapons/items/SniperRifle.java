package net.petersil98.utilcraft_weapons.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.entities.BulletEntity;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

import net.minecraft.item.Item.Properties;

public class SniperRifle extends ShootableItem {

    private boolean isZoomedIn = false;

    public static final Predicate<ItemStack> BULLETS = (stack) -> stack.getItem() instanceof BulletItem;

    public SniperRifle() {
        super(new Properties()
                .stacksTo(1)
                .tab(UtilcraftWeapons.ITEM_GROUP)
        );
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        return false;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if(world.isClientSide) {
            this.isZoomedIn = !this.isZoomedIn;
        }
        return super.use(world, player, hand);
    }

    public boolean shootBullet(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ItemStack ammo = player.getProjectile(itemstack);
        boolean hasAmmo = !ammo.isEmpty();

        if (!player.abilities.instabuild && !hasAmmo) {
            return false;
        } else {
            boolean shouldReduceAmmo = !player.abilities.instabuild;
            BulletEntity bullet = new BulletEntity(world, player);
            bullet.setDamage(5);
            bullet.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.0F, 1.0F);
            world.addFreshEntity(bullet);
            player.startUsingItem(hand);
            if(shouldReduceAmmo) {
                ammo.shrink(1);
                if (ammo.isEmpty()) {
                    player.inventory.removeItem(ammo);
                }
            }
            return true;
        }
    }

    @Override
    @Nonnull
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return BULLETS;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    public boolean isZoomedIn() {
        return this.isZoomedIn;
    }
}
