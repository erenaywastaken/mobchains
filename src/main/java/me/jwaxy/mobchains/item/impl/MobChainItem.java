package me.jwaxy.mobchains.item.impl;

import me.jwaxy.mobchains.attachment.ModAttachments;
import me.jwaxy.mobchains.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;

public class MobChainItem extends Item {
    public MobChainItem(Properties properties) {
        super(properties);
    }

    public static InteractionResult handleUseAny(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (entity.isAlive() && entity instanceof Leashable) {
            Entity chainHolder = ModAttachments.getChainHolder(entity);
            if (chainHolder != null && chainHolder == player) {
                ItemEntity droppedItem = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.MOB_CHAIN));
                level.addFreshEntity(droppedItem);
                entity.removeAttached(ModAttachments.CHAIN_ATTACHMENT);
                entity.gameEvent(GameEvent.ENTITY_INTERACT, player);
                entity.playSound(SoundEvents.LEAD_UNTIED);

                return InteractionResult.SUCCESS.withoutItem();
            } else {
                ItemStack itemStack = player.getItemInHand(hand);
                if (itemStack.is(ModItems.MOB_CHAIN)) {
                    entity.setAttached(ModAttachments.CHAIN_ATTACHMENT, player.getUUID());
                    entity.playSound(SoundEvents.LEAD_TIED);
                    itemStack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
