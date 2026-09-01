package me.jwaxy.mobchains.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.jwaxy.mobchains.attachment.ModAttachments;
import me.jwaxy.mobchains.client.renderer.ChainRenderState;
import me.jwaxy.mobchains.client.renderer.ChainRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Inject(method = "submit", at = @At(value = "HEAD"))
    private void onSubmit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        ChainRenderState chainState = state.getData(ChainRenderState.STATE_DATA_KEY);
        if (chainState != null) {
            submitNodeCollector.submitCustomGeometry(poseStack, ChainRenderer.CHAIN_RENDER_TYPE, ((pose, buffer) -> {
                ChainRenderer.render(pose, buffer, chainState);
            }));
        }
    }

    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;extractNameTags(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V"))
    private void onExtractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        Entity chainHolder = ModAttachments.getChainHolder(entity);
        if (chainHolder != null) {
            ChainRenderState chainState = state.getData(ChainRenderState.STATE_DATA_KEY);
            if (chainState == null) chainState = new ChainRenderState();

            Vec3 leashOffset = Vec3.ZERO;
            if (entity instanceof Leashable leashable) {
                leashOffset = leashable.getLeashOffset(partialTicks);
                float entityYRot = entity.getPreciseBodyRotation(partialTicks) * (float) (Math.PI / 180.0);
                leashOffset = leashOffset.yRot(-entityYRot);
            }

            chainState.offset = leashOffset;
            chainState.start = entity.getPosition(partialTicks).add(leashOffset);
            chainState.end = chainHolder.getRopeHoldPosition(partialTicks);

            state.setData(ChainRenderState.STATE_DATA_KEY, chainState);
        }
    }

    @WrapOperation(
            method = "shouldRender",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Leashable;getLeashHolder()Lnet/minecraft/world/entity/Entity;")
    )
    private Entity getChainHolder(Leashable leashable, Operation<Entity> original) {
        Entity chainHolder = ModAttachments.getChainHolder((Entity) leashable);
        if (chainHolder != null) {
            return chainHolder;
        }

        return original.call(leashable);
    }
}