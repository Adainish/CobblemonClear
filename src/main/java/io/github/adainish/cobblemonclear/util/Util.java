package io.github.adainish.cobblemonclear.util;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public class Util
{
    public static List<Species> pokemonList() {
        return PokemonSpecies.INSTANCE.getImplemented();
    }

    public static Species getSpeciesFromString(String species)
    {
        Species sp = PokemonSpecies.INSTANCE.getByIdentifier(Identifier.of("cobblemon:%sp%".replace("%sp%", species), ":"));
        if (sp == null)
            sp = PokemonSpecies.INSTANCE.getByIdentifier(Identifier.of("cobblemon:vulpix", ':'));
        return sp;
    }

    public static List<Species> ultrabeastList() {
        List <Species> speciesList = new ArrayList<>(pokemonList());

        speciesList.removeIf(sp -> !sp.create(1).isUltraBeast());

        return speciesList;
    }

    public static List<Species> nonSpecialList() {
        List <Species> speciesList = new ArrayList <>(pokemonList());

        speciesList.removeIf(sp -> sp.create(1).isUltraBeast());

        speciesList.removeIf(sp -> sp.create(1).isLegendary());

        return speciesList;
    }

    public static List<Species> legendaryList() {

        List <Species> speciesList = new ArrayList <>(pokemonList());

        speciesList.removeIf(sp -> !sp.create(1).isLegendary());

        return speciesList;
    }
}
