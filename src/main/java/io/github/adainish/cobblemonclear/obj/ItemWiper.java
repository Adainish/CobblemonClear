package io.github.adainish.cobblemonclear.obj;

import ca.landonjw.gooeylibs2.api.tasks.Task;
import io.github.adainish.cobblemonclear.CobblemonClear;
import net.minecraft.entity.ItemEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemWiper {
    public ItemWhitelist whitelist;
    public Task task;
    public long lastWipe;
    public int wipeTimerMinutes = 5;

    public void init() {
        this.whitelist = CobblemonClear.config.itemWhitelist;         //pull whitelist from config
        this.wipeTimerMinutes = CobblemonClear.config.itemWipeTimerMinutes;         //pull timer from config
        this.lastWipe = 0;
        this.task = Task.builder().infinite().execute(this::wipe).interval(20).build();

    }

    public void shutdown() {
        this.task.setExpired();
        this.task = null;
    }

    public boolean shouldWipe() {
        return System.currentTimeMillis() >= timeUntilWipe();
    }

    public long timeUntilWipe() {
        return lastWipe + TimeUnit.MINUTES.toMillis(wipeTimerMinutes);
    }

    public void wipe() {
        // check if enough time passed for wipe
        if (shouldWipe()) {
            for (ServerWorld w : CobblemonClear.getServer().getWorlds()) {
                if (!w.isClient) {
                    AtomicInteger wipedCount = new AtomicInteger();
                    w.getEntitiesByClass(ItemEntity.class, null, EntityPredicates.VALID_ENTITY).forEach(e -> {
                        if (!whitelist.whitelistedItemIDs.isEmpty()) {
                            if (!whitelist.isWhiteListed(e)) {
                                return;
                            }
                        }
                        e.setDespawnImmediately();
                        wipedCount.getAndIncrement();
                    });
                    this.lastWipe = System.currentTimeMillis();
                    int finalAmount = wipedCount.get();
                    //dobroadcast
                    String bc = CobblemonClear.config.itemsWipedMessage;
                    bc = bc.replace("%amount%", String.valueOf(finalAmount));
                }
            }
        }
    }
}
