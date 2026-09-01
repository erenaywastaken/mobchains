package me.jwaxy.mobchains.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.jwaxy.mobchains.MobChains;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ChainRenderer {
    private static final Identifier CHAIN_TEXTURE_LOCATION = MobChains.id("textures/entity/mob_chain.png");
    public static final RenderType CHAIN_RENDER_TYPE = RenderTypes.entityCutout(CHAIN_TEXTURE_LOCATION);

    public static void render(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            ChainRenderState state
    ) {
        float dx = (float) (state.end.x - state.start.x);
        float dy = (float) (state.end.y - state.start.y);
        float dz = (float) (state.end.z - state.start.z);

        float offsetFactor = Mth.invSqrt(dx * dx + dz * dz) * 0.05F / 2.0F;
        float dxOff = dz * offsetFactor;
        float dzOff = dx * offsetFactor;
        pose.translate((float)state.offset.x, (float)state.offset.y, (float)state.offset.z);

        vertex(pose, buffer,
                -dxOff,
                0.0F,
                dzOff,
                0.0F,
                0.0F);

        vertex(pose, buffer,
                dxOff,
                0.0F,
                -dzOff,
                1.0F,
                0.0F);

        vertex(pose, buffer,
                dx - dxOff,
                dy,
                dz + dzOff,
                1.0F,
                1.0F);

        vertex(pose, buffer,
                dx + dxOff,
                dy,
                dz - dzOff,
                0.0F,
                1.0F);
    }

    private static void vertex(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            float x,
            float y,
            float z,
            float u,
            float v
    ) {
        buffer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
