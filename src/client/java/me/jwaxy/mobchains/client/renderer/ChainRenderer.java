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
    private static final Identifier CHAIN_TEXTURE_LOCATION =
            MobChains.id("textures/entity/mob_chain.png");

    public static final RenderType CHAIN_RENDER_TYPE =
            RenderTypes.entityCutout(CHAIN_TEXTURE_LOCATION);

    private static final float CHAIN_WIDTH = 3.0F / 16.0F;
    private static final float HALF_WIDTH = CHAIN_WIDTH / 2.0F;

    public static void render(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            ChainRenderState state
    ) {
        float dx = (float) (state.end.x - state.start.x);
        float dy = (float) (state.end.y - state.start.y);
        float dz = (float) (state.end.z - state.start.z);

        pose.translate(
                (float) state.offset.x,
                (float) state.offset.y,
                (float) state.offset.z
        );

        float horizontalLength = Mth.sqrt(dx * dx + dz * dz);

        float dirX = dx;
        float dirY = dy;
        float dirZ = dz;

        float length = Mth.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);

        dirX /= length;
        dirY /= length;
        dirZ /= length;

        float widthX = -dz / horizontalLength * HALF_WIDTH;
        float widthY = 0.0F;
        float widthZ = dx / horizontalLength * HALF_WIDTH;

        float[] rotated1 = rotateAroundAxis(
                widthX, widthY, widthZ,
                dirX, dirY, dirZ,
                Mth.DEG_TO_RAD * 45.0F
        );

        renderQuad(
                pose, buffer,
                dx, dy, dz,
                rotated1[0], rotated1[1], rotated1[2],
                0.0F
        );


        float[] rotated2 = rotateAroundAxis(
                widthX, widthY, widthZ,
                dirX, dirY, dirZ,
                Mth.DEG_TO_RAD * -45.0F
        );

        renderQuad(
                pose, buffer,
                dx, dy, dz,
                rotated2[0], rotated2[1], rotated2[2],
                0.5F
        );
    }

    private static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            float dx,
            float dy,
            float dz,
            float widthX,
            float widthY,
            float widthZ,
            float vOffset
    ) {
        float length = Mth.sqrt(dx * dx + dy * dy + dz * dz);

        float v = length * 2.0f + vOffset;

        vertex(pose, buffer,
                -widthX, -widthY, -widthZ,
                0.0F, vOffset);

        vertex(pose, buffer,
                widthX, widthY, widthZ,
                1.0F, vOffset);

        vertex(pose, buffer,
                dx + widthX, dy + widthY, dz + widthZ,
                1.0F, v);

        vertex(pose, buffer,
                dx - widthX, dy - widthY, dz - widthZ,
                0.0F, v);
    }

    private static float[] rotateAroundAxis(
            float x,
            float y,
            float z,
            float ax,
            float ay,
            float az,
            float angle
    ) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);

        float dot = x * ax + y * ay + z * az;

        float crossX = ay * z - az * y;
        float crossY = az * x - ax * z;
        float crossZ = ax * y - ay * x;

        return new float[]{
                x * cos + crossX * sin + ax * dot * (1.0F - cos),
                y * cos + crossY * sin + ay * dot * (1.0F - cos),
                z * cos + crossZ * sin + az * dot * (1.0F - cos)
        };
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