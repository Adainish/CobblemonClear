package io.github.adainish.cobblemonclear.obj;

import ca.landonjw.gooeylibs2.api.tasks.Task;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import io.github.adainish.cobblemonclear.CobblemonClear;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PokemonWiper
{
    public PokemonWhitelist whitelist;
    public Task task;
    public long lastWipe;
    public int wipeTimerMinutes = 5;
    public PokemonWiper()
    {

    }

    public void init()
    {
        this.lastWipe = System.currentTimeMillis();

        //pull whitelist from config
        this.whitelist = CobblemonClear.config.pokemonWhitelist;
        //pull timer from config
        this.wipeTimerMinutes = CobblemonClear.config.itemWipeTimerMinutes;
        this.task = Task.builder().infinite().execute(this::wipe).interval(20).build();
    }

    public void shutdown()
    {
        this.task.setExpired();
        this.task = null;
    }

    public boolean shouldWipe()
    {
        return System.currentTimeMillis() >= timeUntilWipe();
    }

    public long timeUntilWipe()
    {
        return lastWipe + TimeUnit.MINUTES.toMillis(wipeTimerMinutes);
    }

    public void wipe()
    {
        // check if enough time passed for wipe
        if (shouldWipe())
        {
            for (ServerWorld w:CobblemonClear.getServer().getWorlds()) {
                if (!w.isClient) {
                    AtomicInteger wipedCount = new AtomicInteger();
//                    for (Entity e : w.getEntitiesByClass(PokemonEntity.class, null, EntityPredicates.VALID_ENTITY)) {
//                        if (!whitelist.whitelistedPokemon.isEmpty())
//                        {
//                            PokemonEntity pokemonEntity = (PokemonEntity) e;
//                        }
//                    }
                    lastWipe = System.currentTimeMillis();
                    int finalAmount = wipedCount.get();
                    //do broadcast
                    String bc = CobblemonClear.config.pokemonsWipedMessage;
                    bc = bc.replace("%amount%", String.valueOf(finalAmount));

                }
            }
        }
    }
}
