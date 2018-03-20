package net.holograms;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import net.holograms.command.HologramsCmd;
import net.holograms.expansion.PlayerExpansion;
import net.holograms.listener.MainListener;
import net.holograms.placeholder.PlaceholderAPI;
import net.holograms.task.HologramUpdateTask;

public class Holograms extends PluginBase {

    // The main instance of the plugin.
    private static Holograms instance;

    // The main listener for all events.
    private static MainListener listener;

    // The Manager for all Holograms!
    private static HologramManager hologramManager;

    public static Holograms getInstance() {
        return instance;
    }

    public static HologramManager getHologramManager() {
        return hologramManager;
    }

    @Override
    public void onEnable() {

        // Warn players about using /reload command.
        if (instance != null || System.getProperty("HologramsLoaded") != null) {
            this.getLogger().error("Please do not use /reload or plugin reloaders. You will receive no support for this operation");
        }

        System.setProperty("HologramsLoaded", "true");
        this.getLogger().info(TextFormat.YELLOW + "Holograms has been enabled");
        instance = this;

        PlaceholderAPI.registerPlaceholderHook(this, new PlayerExpansion());

        hologramManager = new HologramManager();

        getServer().getPluginManager().registerEvents(new MainListener(), this);

        getServer().getCommandMap().register("hologram", new HologramsCmd(this));

        getServer().getScheduler().scheduleRepeatingTask(this, new HologramUpdateTask(this), 2);

        // LOAD HOLOGRAMS IN CONFIG!
        HologramManager.getInstance().load();
    }

    @Override
    public void onDisable() {
        HologramManager.getInstance().save();
    }

}
