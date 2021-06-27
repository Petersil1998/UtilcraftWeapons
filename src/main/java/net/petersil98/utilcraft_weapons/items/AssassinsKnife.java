package net.petersil98.utilcraft_weapons.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.effects.UtilcraftWeaponsEffects;

import javax.annotation.Nonnull;

public class AssassinsKnife extends SwordItem {

    private final int cooldown = 15;
    private final int duration = 10;

    public AssassinsKnife() {
        super(ItemTier.NETHERITE, 5, -2.4F, new Item.Properties()
                .group(UtilcraftWeapons.ITEM_GROUP)
        );
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        CooldownTracker tracker = player.getCooldownTracker();
        if(!tracker.hasCooldown(this)) {
            player.addPotionEffect(new EffectInstance(UtilcraftWeaponsEffects.STEALTH, 20 * this.duration));
            player.addPotionEffect(new EffectInstance(Effects.SPEED, 20 * this.duration, 3));
            tracker.setCooldown(this, 20 * this.cooldown);
            return ActionResult.resultConsume(player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        return 1.0F;
    }

    @Override
    public boolean canHarvestBlock(@Nonnull BlockState block) {
        return false;
    }
}
