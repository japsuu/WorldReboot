package org.japsu.worldreboot;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public final class WorldReboot extends JavaPlugin {

    @Override
    public void onEnable() {

        Logger pluginLogger = getLogger();
        FileConfiguration config = this.getConfig();

        config.addDefault("Enabled", false);
        config.addDefault("DisableAfterRegeneration", false);
        config.addDefault("WorldsToRegenerate", Arrays.asList("world", "world_nether", "world_the_end"));

        config.options().copyDefaults(true);
        saveConfig();

        // Return early if disabled.
        boolean enabled = config.getBoolean("Enabled");
        if(!enabled) return;

        // World regeneration.
        List<String> worldsToRegenerate = config.getStringList("WorldsToRegenerate");

        for (String world : worldsToRegenerate){
            deleteWorldFolder(world);
            pluginLogger.warning("Regenerating world " + world + "...");
        }

        if(config.getBoolean("DisableAfterRegeneration")){
            pluginLogger.warning("DisableAfterRegeneration was ON, disabling plugin!");
            getConfig().set("Enabled", false);
            saveConfig();
        }
    }

    private void deleteWorldFolder(String worldName) {

        File worldFolder = new File(getServer().getWorldContainer(), worldName);
        if (worldFolder.exists() && worldFolder.isDirectory()) {
            // Delete the world folder and all its contents
            deleteFolder(worldFolder);
        }
    }

    private void deleteFolder(File folder) {

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        // Do not delete the folder, since CraftBukkit might want to write some uid files there on initialization.
        // folder.delete();
    }
}
