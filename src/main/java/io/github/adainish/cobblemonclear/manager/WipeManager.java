package io.github.adainish.cobblemonclear.manager;

import ca.landonjw.gooeylibs2.api.tasks.Task;
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

    public void init() {
        if (CobblemonClear.config != null) {
            CobblemonClear.getLog().warn("Attempting to initialise Wipe Manager...");
            if (this.itemWiper != null) {
                CobblemonClear.getLog().warn("Item Wiper exists, shutting down before continuing");
                this.itemWiper.shutdown();
            }
            if (this.pokemonWiper != null) {
                CobblemonClear.getLog().warn("Pokemon Wiper exists, shutting down before continuing");
                this.pokemonWiper.shutdown();
            }
            CobblemonClear.getLog().warn("Initialising Item Wiper");
            this.itemWiper = new ItemWiper();
            CobblemonClear.getLog().warn("Initialising Pokemon Wiper");
            this.pokemonWiper = new PokemonWiper();
            this.itemWiper.init();
            this.pokemonWiper.init();

        } else {
            CobblemonClear.getLog().error("Failed to initialise Cobblemon Clear, the config failed to load or contained mismatched data");
        }
    }
}
