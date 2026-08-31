package me.jwaxy.mobchains.mixin;

import me.jwaxy.mobchains.duck.LeashDataAccess;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Leashable.class)
public interface LeashableMixin {
    @Inject(method = "readLeashData", at = @At("TAIL"))
    private void onReadLeashData(ValueInput input, CallbackInfo ci) {
        Leashable.LeashData data = ((Leashable) this).getLeashData();

        if (data != null) {
            LeashDataAccess access = (LeashDataAccess) (Object) data;
            access.mobchains$setChained(input.getBooleanOr("is_chained", false));
        }
    }

    @Inject(method = "writeLeashData", at = @At("TAIL"))
    private void onWriteLeashData(ValueOutput output, Leashable.LeashData leashData, CallbackInfo ci) {
        if (leashData != null) {
            LeashDataAccess access = (LeashDataAccess) (Object) leashData;
            output.putBoolean("is_chained", access.mobchains$isChained());
        }
    }
}