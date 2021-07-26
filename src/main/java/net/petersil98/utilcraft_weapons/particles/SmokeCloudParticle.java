package net.petersil98.utilcraft_weapons.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;
import java.util.Random;

public class SmokeCloudParticle extends TextureSheetParticle {

    private final SpriteSet spriteSetWithAge;

    SmokeCloudParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteSetWithAge, int color) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.spriteSetWithAge = spriteSetWithAge;
        float red = FastColor.ARGB32.red(color)/255.0f;
        float green = FastColor.ARGB32.green(color)/255.0f;
        float blue = FastColor.ARGB32.blue(color)/255.0f;
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp(((float)this.age + scaleFactor) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
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
