package io.github.adainish.cobblemonclear.obj;

import ca.landonjw.gooeylibs2.api.tasks.Task;
import io.github.adainish.cobblemonclear.CobblemonClear;
import io.github.adainish.cobblemonclear.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemWiper {
    public ItemWhitelist whitelist;
    public Task task;
    public long lastWipe;
    public int wipeTimerMinutes = 5;
    public List<Integer> warningIntervals;

    public void init() {
        this.whitelist = CobblemonClear.config.itemWhitelist;         //pull whitelist from config
        this.wipeTimerMinutes = CobblemonClear.config.itemWipeTimerMinutes;         //pull timer from config
        this.lastWipe = 0;
        this.warningIntervals = CobblemonClear.config.warningIntervalsSecondsItems;
        this.task = Task.builder().infinite().execute(this::attemptExecution).interval(20).build();

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

    public boolean shouldWarn()
    {
        long secondsSince = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWipe);
        for (Integer i:warningIntervals) {
            if (secondsSince == i.longValue())
                return true;
        }
        return false;
    }

    public String timeTillWipe()
    {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeUntilWipe()),
                ZoneId.systemDefault());
        ZonedDateTime date1 = ZonedDateTime.now();
        return DurationFormatUtils.formatDurationWords(Duration.between(date1, zdt).toMillis(), true, true);
    }

    public void wipe()
    {
        AtomicInteger wipedCount = new AtomicInteger();
        List<ItemEntity> itemEntityList = new ArrayList<>();
        for (ServerLevel w : CobblemonClear.getServer().getAllLevels()) {
            if (!w.isClientSide) {

                w.getAllEntities().forEach(entity -> {
                    if (entity instanceof ItemEntity)
                    {
                        itemEntityList.add((ItemEntity) entity);
                    }
                });
                for (ItemEntity e:itemEntityList) {
                    if (!whitelist.whitelistedItemIDs.isEmpty()) {
                        if (whitelist.isWhiteListed(e)) {
                            continue;
                        }
                    }
                    e.kill();
                    wipedCount.getAndIncrement();
                }
            }
        }
        this.lastWipe = System.currentTimeMillis();
        int finalAmount = wipedCount.get();
        //dobroadcast
        String bc = CobblemonClear.config.itemsWipedMessage;
        bc = bc.replace("%amount%", String.valueOf(finalAmount));
        Util.doBroadcast(bc);
    }

    public void attemptExecution() {
        // check if enough time passed for wipe
        if (shouldWarn())
        {
            String warning = CobblemonClear.config.itemWarningMessage;
            warning = warning.replace("%time%", timeTillWipe());
            Util.doBroadcast(warning);
        }
        if (shouldWipe()) {
            wipe();
        }
    }
}
