package net.petersil98.utilcraft_weapons.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.petersil98.utilcraft_weapons.effects.UtilcraftWeaponsEffects;

import javax.annotation.Nonnull;

public class AssassinsKnife extends SwordItem {

    private final int cooldown = 15;
    private final int duration = 10;

    public AssassinsKnife(Properties properties) {
        super(ItemTier.NETHERITE, 5, -2.4F, properties);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        CooldownTracker tracker = player.getCooldowns();
        if(!tracker.isOnCooldown(this)) {
            player.addEffect(new EffectInstance(UtilcraftWeaponsEffects.STEALTH.get(), 20 * this.duration));
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 20 * this.duration, 3));
            tracker.addCooldown(this, 20 * this.cooldown);
            return ActionResult.consume(player.getItemInHand(hand));
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
