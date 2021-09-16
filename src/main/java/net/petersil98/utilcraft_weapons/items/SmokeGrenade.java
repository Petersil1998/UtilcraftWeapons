package net.petersil98.utilcraft_weapons.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.petersil98.utilcraft_weapons.entities.SmokeGrenadeEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SmokeGrenade extends Item {

    public SmokeGrenade(Properties properties) {
        super(properties);
    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 200);
        if (!world.isClientSide) {
            DyeColor color = getColor(itemstack);
            SmokeGrenadeEntity smokeGrenadeEntity = new SmokeGrenadeEntity(world, player, color.getTextColor());
            smokeGrenadeEntity.setItem(itemstack);
            smokeGrenadeEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(smokeGrenadeEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        DyeColor color = getColor(stack);
        tooltip.add(new TranslatableComponent(color.getName())
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color.getTextColor())))
                .withStyle(ChatFormatting.ITALIC)
        );
    }

    public static void setColor(@Nonnull ItemStack stack, @Nonnull DyeColor color) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("colorID", color.getId());
    }

    @Nonnull
    public static DyeColor getColor(@Nonnull ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return DyeColor.byId(tag.getInt("colorID"));
    }
}
