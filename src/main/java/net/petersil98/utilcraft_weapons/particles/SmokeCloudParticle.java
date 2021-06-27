package net.petersil98.utilcraft_weapons.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

public class SmokeCloudParticle extends SpriteTexturedParticle {

    private final IAnimatedSprite spriteSetWithAge;

    SmokeCloudParticle(ClientWorld world, double x, double y, double z, IAnimatedSprite spriteSetWithAge, int color) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.spriteSetWithAge = spriteSetWithAge;
        float red = ColorHelper.PackedColor.getRed(color)/255.0f;
        float green = ColorHelper.PackedColor.getGreen(color)/255.0f;
        float blue = ColorHelper.PackedColor.getBlue(color)/255.0f;
        Random random = new Random();
        blue += blue*((random.nextInt(20)-10)/100f);
        red += red*((random.nextInt(20)-10)/100f);
        green += green*((random.nextInt(20)-10)/100f);
        this.particleRed = Math.max(Math.min(red, 1.0f), 0.0f);
        this.particleGreen = Math.max(Math.min(green, 1.0f), 0.0f);
        this.particleBlue = Math.max(Math.min(blue, 1.0f), 0.0f);
        this.particleScale *= 1.875F;
        this.maxAge = 500;
        this.canCollide = false;
        this.selectSpriteWithAge(spriteSetWithAge);
    }

    @Nonnull
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public float getScale(float scaleFactor) {
        return this.particleScale * MathHelper.clamp(((float)this.age + scaleFactor) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.selectSpriteWithAge(this.spriteSetWithAge);
        }
    }
}
