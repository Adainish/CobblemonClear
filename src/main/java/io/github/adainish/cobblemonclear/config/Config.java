package io.github.adainish.cobblemonclear.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobblemonclear.CobblemonClear;
import io.github.adainish.cobblemonclear.obj.ItemWhitelist;
import io.github.adainish.cobblemonclear.obj.PokemonWhitelist;
import io.github.adainish.cobblemonclear.util.Adapters;

import java.io.*;

public class Config
{
    public ItemWhitelist itemWhitelist = new ItemWhitelist();
    public String itemWarningMessage = "&c&lA Item Wipe will be occurring in %time%";
    public String itemsWipedMessage = "&4&lWiped %amount% items";
    public int itemWipeTimerMinutes = 10;
    public PokemonWhitelist pokemonWhitelist = new PokemonWhitelist();

    public String pokemonWarningMessage = "&c&lA Pokemon Wipe will be occurring in %time%";
    public String pokemonsWipedMessage = "&4&lWiped %amount% Pokemon";
    public int pokemonWipeTimerMinutes = 10;


    public Config()
    {

    }

    public static void writeConfig()
    {
        File dir = CobblemonClear.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        Config config = new Config();
        try {
            File file = new File(dir, "config.json");
            if (file.exists())
                return;
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String json = gson.toJson(config);
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            CobblemonClear.getLog().warn(e);
        }
    }

    public static Config getConfig()
    {
        File dir = CobblemonClear.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "config.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobblemonClear.getLog().error("Something went wrong attempting to read the Config");
            return null;
        }

        return gson.fromJson(reader, Config.class);
    }


}
