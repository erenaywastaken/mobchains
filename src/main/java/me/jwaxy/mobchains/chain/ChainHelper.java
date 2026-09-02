package me.jwaxy.mobchains.chain;

import me.jwaxy.mobchains.attachment.ModAttachments;
import me.jwaxy.mobchains.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class ChainHelper {
    public static final double CHAIN_LENGTH = 8.0;
    private static final double CHAIN_CORRECTION_THRESHOLD = 3.0;

    public static Entity getChainHolder(Entity entity) {
        UUID chainHolderUUID = entity.getAttached(ModAttachments.CHAIN_ATTACHMENT);
        if (chainHolderUUID != null) {
            return entity.level().getEntity(chainHolderUUID);
        }

        return null;
    }

    public static void dropChain(Entity entity) {
        entity.removeAttached(ModAttachments.CHAIN_ATTACHMENT);
        Level level = entity.level();
        if (!level.isClientSide()) {
            entity.spawnAtLocation((ServerLevel) level, ModItems.MOB_CHAIN);
        }
    }

    public static void applyChainConstraint(Entity entity, Entity chainHolder) {
        Vec3 delta = chainHolder.position().subtract(entity.position());
        double distance = delta.length();

        if (distance > CHAIN_LENGTH) {
            Vec3 correction = delta.scale(1.0 - CHAIN_LENGTH / distance);

            entity.setDeltaMovement(entity.getDeltaMovement().add(correction));

            if (correction.length() > CHAIN_CORRECTION_THRESHOLD) {
                chainHolder.setDeltaMovement( chainHolder.getDeltaMovement().subtract(correction.scale(0.5)));
            }
        }
    }
}
