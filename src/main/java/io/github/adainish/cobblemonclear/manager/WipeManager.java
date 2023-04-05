package io.github.adainish.cobblemonclear.manager;

import io.github.adainish.cobblemonclear.CobblemonClear;
import io.github.adainish.cobblemonclear.obj.ItemWiper;
import io.github.adainish.cobblemonclear.obj.PokemonWiper;

public class WipeManager
{

    public ItemWiper itemWiper;
    public PokemonWiper pokemonWiper;

    public WipeManager()
    {

    }

    public void init()
    {
        if (CobblemonClear.config != null) {
            if (this.itemWiper != null)
                this.itemWiper.shutdown();
            if (this.pokemonWiper != null)
                this.pokemonWiper.shutdown();
            this.itemWiper = new ItemWiper();
            this.pokemonWiper = new PokemonWiper();
            this.itemWiper.init();
            this.pokemonWiper.init();
        } else {
            CobblemonClear.getLog().error("Failed to initialise Cobblemon Clear, the config failed to load or contained mismatched data");
        }
    }
}
