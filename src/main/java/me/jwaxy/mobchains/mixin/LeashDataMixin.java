package me.jwaxy.mobchains.mixin;

import me.jwaxy.mobchains.duck.LeashDataAccess;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Leashable.LeashData.class)
public class LeashDataMixin implements LeashDataAccess {
    @Unique
    private boolean isChained;

    @Override
    public boolean mobchains$isChained() {
        return isChained;
    }

    @Override
    public void mobchains$setChained(boolean value) {
        isChained = value;
    }
}