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
                .group(UtilcraftWeapons.ITEM_GROUP)
        );
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        //player.getCooldownTracker().setCooldown(this, 200);
        if (!world.isRemote) {
            DyeColor color = getColor(itemstack);
            SmokeGrenadeEntity smokeGrenadeEntity = new SmokeGrenadeEntity(world, player, color.getColorValue());
            smokeGrenadeEntity.setItem(itemstack);
            smokeGrenadeEntity.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(smokeGrenadeEntity);
        }

        player.addStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        return ActionResult.func_233538_a_(itemstack, world.isRemote());
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        DyeColor color = getColor(stack);
        tooltip.add(new TranslationTextComponent(color.getTranslationKey())
                .setStyle(Style.EMPTY.setColor(Color.fromInt(color.getTextColor())))
                .mergeStyle(TextFormatting.ITALIC)
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
