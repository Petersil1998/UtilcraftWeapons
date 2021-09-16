package net.petersil98.utilcraft_weapons.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.petersil98.utilcraft_weapons.entities.BulletEntity;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SniperRifle extends ProjectileWeaponItem {

    private boolean isZoomedIn = false;

    public static final Predicate<ItemStack> BULLETS = (stack) -> stack.getItem() instanceof BulletItem;

    public SniperRifle(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return false;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if(world.isClientSide) {
            this.isZoomedIn = !this.isZoomedIn;
        }
        return super.use(world, player, hand);
    }

    public boolean shootBullet(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ItemStack ammo = player.getProjectile(itemstack);
        boolean hasAmmo = !ammo.isEmpty();

        if (!player.getAbilities().instabuild && !hasAmmo) {
            return false;
        } else {
            boolean shouldReduceAmmo = !player.getAbilities().instabuild;
            BulletEntity bullet = new BulletEntity(world, player);
            bullet.setDamage(5);
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
            world.addFreshEntity(bullet);
            player.startUsingItem(hand);
            if(shouldReduceAmmo) {
                ammo.shrink(1);
                if (ammo.isEmpty()) {
                    player.getInventory().removeItem(ammo);
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
