package me.jwaxy.mobchains.attachment;

import me.jwaxy.mobchains.MobChains;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class ModAttachments {
    public static final AttachmentType<UUID> CHAIN_ATTACHMENT = AttachmentRegistry.create(MobChains.id("chain"), builder ->
        builder.persistent(UUIDUtil.CODEC).syncWith(UUIDUtil.STREAM_CODEC, AttachmentSyncPredicate.all())
    );

    public static Entity getChainHolder(Entity entity) {
        UUID chainHolderUUID = entity.getAttached(ModAttachments.CHAIN_ATTACHMENT);
        if (chainHolderUUID != null) {
            return entity.level().getEntity(chainHolderUUID);
        }

        return null;
    }
}
