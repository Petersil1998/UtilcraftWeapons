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
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.spriteSetWithAge = spriteSetWithAge;
        float red = ColorHelper.PackedColor.red(color)/255.0f;
        float green = ColorHelper.PackedColor.green(color)/255.0f;
        float blue = ColorHelper.PackedColor.blue(color)/255.0f;
        Random random = new Random();
        blue += blue*((random.nextInt(20)-10)/100f);
        red += red*((random.nextInt(20)-10)/100f);
        green += green*((random.nextInt(20)-10)/100f);
        this.rCol = Math.max(Math.min(red, 1.0f), 0.0f);
        this.gCol = Math.max(Math.min(green, 1.0f), 0.0f);
        this.bCol = Math.max(Math.min(blue, 1.0f), 0.0f);
        this.quadSize *= 1.875F;
        this.lifetime = 500;
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSetWithAge);
    }

    @Nonnull
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public float getQuadSize(float scaleFactor) {
        return this.quadSize * MathHelper.clamp(((float)this.age + scaleFactor) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.spriteSetWithAge);
        }
    }
}
