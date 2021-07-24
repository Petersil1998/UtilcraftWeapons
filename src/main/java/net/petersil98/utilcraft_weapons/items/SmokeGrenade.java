package net.petersil98.utilcraft_weapons.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.entities.SmokeGrenadeEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SmokeGrenade extends Item {

    public SmokeGrenade() {
        super(new Item.Properties()
                .tab(UtilcraftWeapons.ITEM_GROUP)
        );
    }

    @Nonnull
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        //player.getCooldownTracker().setCooldown(this, 200);
        if (!world.isClientSide) {
            DyeColor color = getColor(itemstack);
            SmokeGrenadeEntity smokeGrenadeEntity = new SmokeGrenadeEntity(world, player, color.getColorValue());
            smokeGrenadeEntity.setItem(itemstack);
            smokeGrenadeEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(smokeGrenadeEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild) {
            itemstack.shrink(1);
        }

        return ActionResult.sidedSuccess(itemstack, world.isClientSide());
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        DyeColor color = getColor(stack);
        tooltip.add(new TranslationTextComponent(color.getName())
                .setStyle(Style.EMPTY.withColor(Color.fromRgb(color.getTextColor())))
                .withStyle(TextFormatting.ITALIC)
        );
    }

    public static void setColor(@Nonnull ItemStack stack, @Nonnull DyeColor color) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("colorID", color.getId());
    }

    @Nonnull
    public static DyeColor getColor(@Nonnull ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return DyeColor.byId(tag.getInt("colorID"));
    }
}
