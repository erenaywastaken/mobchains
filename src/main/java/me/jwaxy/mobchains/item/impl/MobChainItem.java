package me.jwaxy.mobchains.item.impl;

import me.jwaxy.mobchains.duck.LeashDataAccess;
import me.jwaxy.mobchains.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

public class MobChainItem extends Item {
    public MobChainItem(Properties properties) {
        super(properties);
    }

    private static boolean canHaveALeashAttachedTo(Leashable leashable, Entity holder) {
        return leashable.leashDistanceTo(holder) < leashable.leashSnapDistance();
    }

    public static InteractionResult handleUseAny(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (!level.isClientSide() && player.isSecondaryUseActive() && entity instanceof Leashable && entity.isAlive()) {
            List<Leashable> mobsToLeash = Leashable.leashableInArea(entity, l -> l.getLeashHolder() == player);
            if (!mobsToLeash.isEmpty()) {
                boolean anyLeashed = false;

                for (Leashable mob : mobsToLeash) {
                    if (canHaveALeashAttachedTo(mob, entity)) {
                        mob.setLeashedTo(entity, true);
                        ((LeashDataAccess) (Object) mob.getLeashData()).mobchains$setChained(true);
                        anyLeashed = true;
                    }
                }

                if (anyLeashed) {
                    entity.level().gameEvent(GameEvent.ENTITY_ACTION, entity.blockPosition(), GameEvent.Context.of(player));
                    entity.playSound(SoundEvents.LEAD_TIED);
                    return InteractionResult.SUCCESS_SERVER.withoutItem();
                }
            }
        }

        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(Items.SHEARS)) {
            if (entity instanceof Leashable leashable) {
                Leashable.LeashData data = leashable.getLeashData();
                if (data != null) ((LeashDataAccess) (Object) data).mobchains$setChained(false);
            }

            for (Leashable leashable : Leashable.leashableLeashedTo(entity)) {
                Leashable.LeashData data = leashable.getLeashData();
                if (data != null) ((LeashDataAccess) (Object) data).mobchains$setChained(false);
            }

            if (entity.shearOffAllLeashConnections(player)) {
                heldItem.hurtAndBreak(1, player, hand);
                return InteractionResult.SUCCESS;
            }
        } else if (entity.isAlive() && entity instanceof Leashable leashable) {
            if (leashable.getLeashHolder() == player) {
                if (!entity.level().isClientSide()) {
                    if (player.hasInfiniteMaterials()) {
                        leashable.removeLeash();
                    } else {
                        leashable.dropLeash();
                    }

                    ((LeashDataAccess) (Object) leashable.getLeashData()).mobchains$setChained(false);

                    entity.gameEvent(GameEvent.ENTITY_INTERACT, player);
                    entity.playSound(SoundEvents.LEAD_UNTIED);
                }

                return InteractionResult.SUCCESS.withoutItem();
            }

            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.is(ModItems.MOB_CHAIN) && !(leashable.getLeashHolder() instanceof Player)) {
                if (entity.level().isClientSide()) {
                    return InteractionResult.CONSUME;
                }

                if (canHaveALeashAttachedTo(leashable, player)) {
                    if (leashable.isLeashed()) {
                        leashable.dropLeash();
                    }

                    leashable.setLeashedTo(player, true);
                    ((LeashDataAccess) (Object) leashable.getLeashData()).mobchains$setChained(true);
                    entity.playSound(SoundEvents.LEAD_TIED);
                    itemStack.shrink(1);
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
