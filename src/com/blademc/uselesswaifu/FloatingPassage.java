package com.blademc.uselesswaifu;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.blademc.uselesswaifu.command.FloatingPassageCmd;
import com.blademc.uselesswaifu.expansion.PlayerExpansion;
import com.blademc.uselesswaifu.listener.MainListener;
import com.blademc.uselesswaifu.placeholder.PlaceholderAPI;
import com.blademc.uselesswaifu.task.HologramUpdateTask;

public class FloatingPassage extends PluginBase {

    // The main instance of the plugin.
    private static FloatingPassage instance;

    // The main listener for all events.
    private static MainListener listener;

    // The Manager for all Holograms!
    private static HologramManager hologramManager;

    public static FloatingPassage getInstance() {
        return instance;
    }

    public static HologramManager getHologramManager() {
        return hologramManager;
    }

    @Override
    public void onEnable() {

        // Warn players about using /reload command.
        if (instance != null || System.getProperty("FloatingPassageLoaded") != null) {
            this.getLogger().error("Please do not use /reload or plugin reloaders. You will receive no support for this operation");
        }

        System.setProperty("FloatingPassageLoaded", "true");
        this.getLogger().info(TextFormat.YELLOW + "FloatingPassage has been enabled");
        instance = this;

        PlaceholderAPI.registerPlaceholderHook(this, new PlayerExpansion());

        new HologramManager();

        getServer().getPluginManager().registerEvents(new MainListener(), this);

        getServer().getCommandMap().register("floatingpassage", new FloatingPassageCmd(this));

        getServer().getScheduler().scheduleRepeatingTask(this, new HologramUpdateTask(this), 2);

        // LOAD HOLOGRAMS IN CONFIG!
        HologramManager.getInstance().load();
    }

    @Override
    public void onDisable() {
        HologramManager.getInstance().save();
    }

}
