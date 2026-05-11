package com.dikiytechies.ger.client.render;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.entity.GerStandEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import com.github.standobyte.jojo.util.general.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class GerRenderer extends StandEntityRenderer<GerStandEntity, StandEntityModel<GerStandEntity>> {

    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation(GerMain.MOD_ID, "textures/entity/ger_beam.png");

    public GerRenderer(EntityRendererManager manager) {
        super(manager,
                StandModelRegistry.registerModel(new ResourceLocation(GerMain.MOD_ID, "ger"), GerModel::new),
                new ResourceLocation(GerMain.MOD_ID, "textures/entity/stand/ger.png"), 0);
    }
    protected Vector3d getNextTickPos(Vector3d entityPos, GerStandEntity livingEntity) {
        return livingEntity != null ? entityPos.add(new Vector3d(
                livingEntity.getBbWidth() * 0.6 * (-1),
                livingEntity.getBbHeight() * (livingEntity.isShiftKeyDown() ? 0.25 : 0.67),
                livingEntity.getBbWidth() * 0.7)
                .yRot(-livingEntity.yBodyRot * MathUtil.DEG_TO_RAD)) : null;
    }
    @Override
    public boolean shouldRender(GerStandEntity entity, ClippingHelper frustum, double camX, double camY, double camZ) {
        if (entity.getBeamTicks() > 0) {
            return true;
        }
        return super.shouldRender(entity, frustum, camX, camY, camZ);
    }

    @Override
    public void render(GerStandEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);

        if (entity.getBeamTicks() > 0) {
            Vector3d startPos = entity.getEyePosition(partialTicks);
            Vector3d endPos = entity.getBeamEnd();

            Vector3d dir = endPos.subtract(startPos);
            float length = (float) dir.length();
            dir = dir.normalize();

            float yaw = (float) (MathHelper.atan2(dir.x, dir.z) * (180F / Math.PI));
            float pitch = (float) (-MathHelper.atan2(dir.y, MathHelper.sqrt(dir.x * dir.x + dir.z * dir.z)) * (180F / Math.PI));
            matrixStack.pushPose();
            Vector3d offset = getNextTickPos(Vector3d.ZERO, entity);
            matrixStack.translate(offset.x(), offset.y(), offset.z());

            matrixStack.mulPose(Vector3f.YP.rotationDegrees(yaw));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            long gameTime = entity.level.getGameTime();
            //TODO : colormap
            float[] color = new float[]{1.0F, 0.8F, 0.2F};

            renderDirectedBeam(matrixStack, buffer, BEAM_LOCATION, partialTicks, 1.0F, gameTime, length, color, 0.1F, 0.125F, entity.getBeamTicks());

            matrixStack.popPose();
        }
    }
    private static float sosiTechies(int ticks) {
        float alpha = ticks < 5 ? (float) ticks / 5 : (float) (10 - ticks) / 5;
        return MathHelper.clamp(alpha, 0.0F, 1.0F);
    }

    public static void renderDirectedBeam(MatrixStack matrixStack, IRenderTypeBuffer buffer, ResourceLocation texture, float partialTicks, float textureScale, long gameTime, float length, float[] color, float innerRadius, float outerRadius, int ticks) {
        matrixStack.pushPose();
        float animation = (float) Math.floorMod(gameTime, 40L) + partialTicks;
        float vOffset = MathHelper.frac(animation * 0.2F - (float) MathHelper.floor(animation * 0.1F));

        float r = color[0];
        float g = color[1];
        float b = color[2];

        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(animation * 2.25F - 45.0F));

        float vMax = length * textureScale * (0.5F / innerRadius) + (-1.0F + vOffset);

        renderPart(matrixStack, buffer.getBuffer(RenderType.beaconBeam(texture, true)), r, g, b, sosiTechies(ticks), length,
                0.0F, innerRadius, innerRadius, 0.0F, -innerRadius, 0.0F, 0.0F, -innerRadius, 0.0F, 1.0F, vMax, -1.0F + vOffset);
        matrixStack.popPose();

        float vMaxOuter = length * textureScale + (-1.0F + vOffset);
        renderPart(matrixStack, buffer.getBuffer(RenderType.beaconBeam(texture, true)), r, g, b, 0.125F * sosiTechies(ticks), length,
                -outerRadius, -outerRadius, outerRadius, -outerRadius, -outerRadius, outerRadius, outerRadius, outerRadius, 0.0F, 1.0F, vMaxOuter, -1.0F + vOffset);
        matrixStack.popPose();
    }

    private static void renderPart(MatrixStack matrixStack, IVertexBuilder vertexBuilder, float r, float g, float b, float alpha, float length, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float uMin, float uMax, float vMax, float vMin) {
        MatrixStack.Entry entry = matrixStack.last();
        Matrix4f pose = entry.pose();
        Matrix3f normal = entry.normal();
        renderQuad(pose, normal, vertexBuilder, r, g, b, alpha, length, x1, z1, x2, z2, uMin, uMax, vMax, vMin);
        renderQuad(pose, normal, vertexBuilder, r, g, b, alpha, length, x4, z4, x3, z3, uMin, uMax, vMax, vMin);
        renderQuad(pose, normal, vertexBuilder, r, g, b, alpha, length, x2, z2, x4, z4, uMin, uMax, vMax, vMin);
        renderQuad(pose, normal, vertexBuilder, r, g, b, alpha, length, x3, z3, x1, z1, uMin, uMax, vMax, vMin);
    }

    private static void renderQuad(Matrix4f pose, Matrix3f normal, IVertexBuilder vertexBuilder, float r, float g, float b, float alpha, float length, float x1, float z1, float x2, float z2, float uMin, float uMax, float vMax, float vMin) {
        addVertex(pose, normal, vertexBuilder, r, g, b, alpha, length, x1, z1, uMax, vMin);
        addVertex(pose, normal, vertexBuilder, r, g, b, alpha, 0.0F, x1, z1, uMax, vMax);
        addVertex(pose, normal, vertexBuilder, r, g, b, alpha, 0.0F, x2, z2, uMin, vMax);
        addVertex(pose, normal, vertexBuilder, r, g, b, alpha, length, x2, z2, uMin, vMin);
    }

    private static void addVertex(Matrix4f pose, Matrix3f normal, IVertexBuilder vertexBuilder, float r, float g, float b, float alpha, float y, float x, float z, float u, float v) {
        vertexBuilder.vertex(pose, x, y, z).color(r, g, b, alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }
}