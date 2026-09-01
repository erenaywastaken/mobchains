package me.jwaxy.mobchains.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.world.phys.Vec3;

public class ChainRenderState {
    public static final RenderStateDataKey<ChainRenderState> STATE_DATA_KEY = RenderStateDataKey.create(() -> "Chain Render State");

    public Vec3 offset;
    public Vec3 start;
    public Vec3 end;
    public int startBlockLight;
    public int endBlockLight;
    public int startSkyLight;
    public int endSkyLight;
    public boolean slack;

    public ChainRenderState() {
        this.offset = Vec3.ZERO;
        this.start = Vec3.ZERO;
        this.end = Vec3.ZERO;
        this.startBlockLight = 0;
        this.endBlockLight = 0;
        this.startSkyLight = 15;
        this.endSkyLight = 15;
        this.slack = true;
    }
}