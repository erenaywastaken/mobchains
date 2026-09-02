package me.jwaxy.mobchains.mixin;

import me.jwaxy.mobchains.chain.ChainHelper;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo info) {
        Entity entity = (Entity) (Object) this;
        Entity chainHolder = ChainHelper.getChainHolder(entity);
        if (chainHolder == null) return;

        if (!entity.canInteractWithLevel() || !chainHolder.canInteractWithLevel()) {
            ChainHelper.dropChain(entity);
            return;
        }

        ChainHelper.applyChainConstraint(entity, chainHolder);
    }
}