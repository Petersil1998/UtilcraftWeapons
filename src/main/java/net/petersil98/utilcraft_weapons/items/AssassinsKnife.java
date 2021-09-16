package net.petersil98.utilcraft_weapons.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.petersil98.utilcraft_weapons.effects.UtilcraftWeaponsEffects;

import javax.annotation.Nonnull;

public class AssassinsKnife extends SwordItem {

    private final int cooldown = 15;
    private final int duration = 10;

    public AssassinsKnife(Properties properties) {
        super(Tiers.NETHERITE, 5, -2.4F, properties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemCooldowns tracker = player.getCooldowns();
        if(!tracker.isOnCooldown(this)) {
            player.addEffect(new MobEffectInstance(UtilcraftWeaponsEffects.STEALTH.get(), 20 * this.duration));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * this.duration, 3));
            tracker.addCooldown(this, 20 * this.cooldown);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return super.use(world, player, hand);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        return 1.0F;
    }

    @Override
    public boolean isCorrectToolForDrops(@Nonnull BlockState block) {
        return false;
    }
}
