package io.github.adainish.cobblemonclear;

import com.cobblemon.mod.fabric.net.FabricServerNetworkContext;
import io.github.adainish.cobblemonclear.config.Config;
import io.github.adainish.cobblemonclear.manager.WipeManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class CobblemonClear implements ModInitializer
{
    public static final String MOD_NAME = "CobblemonClear";
    public static final String VERSION = "1.0.0-Beta";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2023";
    private static final Logger log = LogManager.getLogger(MOD_NAME);
    private static File configDir;
    private static File storage;
    private static MinecraftServer server;
    public static Config config;

    public static WipeManager manager;
    public static Logger getLog() {
        return log;
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static void setServer(MinecraftServer server) {
        CobblemonClear.server = server;
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        CobblemonClear.configDir = configDir;
    }

    public static File getStorage() {
        return storage;
    }

    public static void setStorage(File storage) {
        CobblemonClear.storage = storage;
    }

    @Override
    public void onInitialize()
    {
        //do data set up
        setServer(CobblemonClear.server);
        initDirs();
        reload();
    }

    public void initDirs() {
        setConfigDir(new File("/CobblemonClear/"));
        getConfigDir().mkdir();
        setStorage(new File(getConfigDir(), "/storage/"));
        getStorage().mkdirs();
    }



    public void initConfigs() {
        Config.writeConfig();
        config = Config.getConfig();
    }

    public void reload() {
        initConfigs();
        if (manager == null) {
            manager = new WipeManager();
        }
        manager.init();
    }


}
