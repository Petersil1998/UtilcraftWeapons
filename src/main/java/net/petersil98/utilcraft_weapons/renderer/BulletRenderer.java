package net.petersil98.utilcraft_weapons.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.entities.BulletEntity;

import javax.annotation.Nonnull;

public class BulletRenderer<T extends BulletEntity> extends EntityRenderer<T> {

    public static final ResourceLocation RES_BULLET = new ResourceLocation(UtilcraftWeapons.MOD_ID, "textures/entity/projectiles/bullet.png");

    public BulletRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public void render(@Nonnull T entity, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.push();
        matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));

        matrixStack.rotate(Vector3f.XP.rotationDegrees(45.0F));
        matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStack.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutout(this.getEntityTexture(entity)));
        MatrixStack.Entry entry = matrixStack.getLast();
        Matrix4f matrix4f = entry.getMatrix();
        Matrix3f matrix3f = entry.getNormal();
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight);

        for(int j = 0; j < 4; ++j) {
            matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight);
        }

        matrixStack.pop();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
    public void drawVertex(Matrix4f matrix, Matrix3f normals, @Nonnull IVertexBuilder vertexBuilder, int offsetX, int offsetY, int offsetZ, float textureX, float textureY, int p_229039_9_, int p_229039_10_, int p_229039_11_, int packedLight) {
        vertexBuilder.pos(matrix, (float)offsetX, (float)offsetY, (float)offsetZ).color(255, 255, 255, 255).tex(textureX, textureY).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLight).normal(normals, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }


    /**
     * Returns the location of an entity's texture.
     */
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull T entity) {
        return RES_BULLET;
    }
}
