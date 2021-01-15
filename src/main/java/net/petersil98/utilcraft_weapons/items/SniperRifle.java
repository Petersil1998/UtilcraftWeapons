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

public class SniperRifle extends ShootableItem {

    private boolean isZoomedIn = false;

    public static final Predicate<ItemStack> BULLETS = (stack) -> stack.getItem() instanceof BulletItem;

    public SniperRifle() {
        super(new Properties()
                .maxStackSize(1)
                .group(UtilcraftWeapons.itemGroup)
        );
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        return false;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn) {
        if(worldIn.isRemote) {
            isZoomedIn = !isZoomedIn;
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public boolean shootBullet(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        ItemStack ammo = player.findAmmo(itemstack);
        boolean hasAmmo = !ammo.isEmpty();

        if (!player.abilities.isCreativeMode && !hasAmmo) {
            return false;
        } else {
            boolean shouldReduceAmmo = !player.abilities.isCreativeMode;
            BulletEntity bullet = new BulletEntity(world, player);
            bullet.setDamage(5);
            bullet.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 1.0F);
            world.addEntity(bullet);
            player.setActiveHand(hand);
            if(shouldReduceAmmo) {
                ammo.shrink(1);
                if (ammo.isEmpty()) {
                    player.inventory.deleteStack(ammo);
                }
            }
            return true;
        }
    }

    @Override
    @Nonnull
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return BULLETS;
    }

    @Override
    public int func_230305_d_() {
        return 15;
    }

    public boolean isZoomedIn() {
        return isZoomedIn;
    }
}
