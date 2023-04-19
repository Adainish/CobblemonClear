package io.github.adainish.cobblemonclear.util;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import io.github.adainish.cobblemonclear.CobblemonClear;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class Util
{


    public static void send(CommandSourceStack sender, String message) {
        sender.sendSystemMessage(Component.literal(((TextUtil.getMessagePrefix()).getString() + message).replaceAll("&([0-9a-fk-or])", "\u00a7$1")));
    }

    public static void doBroadcast(String message) {
        CobblemonClear.getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.sendSystemMessage(Component.literal(TextUtil.getMessagePrefix().getString() + formattedString(message)));
        });
    }

    public static String formattedString(String s) {
        return s.replaceAll("&", "§");
    }

    public static List<Species> pokemonList() {
        return PokemonSpecies.INSTANCE.getImplemented();
    }

    public static Species getSpeciesFromString(String species) {
        return PokemonSpecies.INSTANCE.getByName(species);
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
