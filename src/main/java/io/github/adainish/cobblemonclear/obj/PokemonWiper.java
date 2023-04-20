package io.github.adainish.cobblemonclear.obj;

import ca.landonjw.gooeylibs2.api.tasks.Task;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import io.github.adainish.cobblemonclear.CobblemonClear;
import io.github.adainish.cobblemonclear.util.Util;
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PokemonWiper
{
    public PokemonWhitelist whitelist;
    public Task task;
    public long lastWipe;
    public int wipeTimerMinutes = 5;
    public List<Integer> warningIntervals;
    public PokemonWiper()
    {

    }

    public void init()
    {
        this.lastWipe = System.currentTimeMillis();

        //pull whitelist from config
        this.whitelist = CobblemonClear.config.pokemonWhitelist;
        //pull timer from config
        this.wipeTimerMinutes = CobblemonClear.config.pokemonWipeTimerMinutes;
        this.warningIntervals = CobblemonClear.config.warningIntervalsSecondsPokemon;
        this.task = Task.builder().infinite().execute(this::attemptExecution).interval(20).build();
        CobblemonClear.getLog().warn("Pokemon wiper should have initialised!");
    }

    public void shutdown()
    {
        CobblemonClear.getLog().warn("Shutting down pokemon wiper task");
        this.task.setExpired();
        if (this.task.isExpired())
            CobblemonClear.getLog().warn("Task marked for expiration!");
        this.task = null;
        if (this.task == null)
            CobblemonClear.getLog().warn("Task marked as null... Preparing for new wiper instances!");
    }

    public boolean shouldWipe()
    {
        return System.currentTimeMillis() >= timeUntilWipe();
    }

    public long timeUntilWipe()
    {
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
        for (ServerLevel w: CobblemonClear.getServer().getAllLevels()) {
            List<PokemonEntity> entityList = new ArrayList<>();
            if (!w.isClientSide()) {

                w.getAllEntities().forEach(entity -> {
                    if (entity instanceof PokemonEntity)
                    {
                        if (((PokemonEntity) entity).getOwner() != null)
                            return;
                        if (((PokemonEntity) entity).getPokemon().getShiny())
                            return;
                        entityList.add((PokemonEntity) entity);
                    }
                });
                for (PokemonEntity e:entityList) {
                    if (!whitelist.whitelistedPokemon.isEmpty()) {
                        if (whitelist.isWhiteListed(e)) {
                            continue;
                        }
                    }
                    e.kill();
                    wipedCount.getAndIncrement();
                }
            }
        }
        lastWipe = System.currentTimeMillis();
        int finalAmount = wipedCount.get();
        //do broadcast
        String bc = CobblemonClear.config.pokemonsWipedMessage;
        bc = bc.replace("%amount%", String.valueOf(finalAmount));
        Util.doBroadcast(bc);
    }

    public void attemptExecution()
    {
        if (shouldWarn())
        {
            String warning = CobblemonClear.config.pokemonWarningMessage;
            warning = warning.replace("%time%", timeTillWipe());
            Util.doBroadcast(warning);
        }
        // check if enough time passed for wipe
        if (shouldWipe())
        {
            wipe();
        }
    }
}
