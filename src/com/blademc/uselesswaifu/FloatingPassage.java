package com.blademc.uselesswaifu;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.blademc.uselesswaifu.command.FloatingPassageCmd;
import com.blademc.uselesswaifu.listener.MainListener;

public class FloatingPassage extends PluginBase {

    // The main instance of the plugin.
    private static FloatingPassage instance;

    // The main listener for all events.
    private static MainListener listener;

    @Override
    public void onEnable(){

        // Warn players about using /reload command.
        if(instance != null || System.getProperty("FloatingPassageLoaded") != null){
            this.getLogger().error("Please do not use /reload or plugin reloaders. You will receive no support for this operation");
        }

        System.setProperty("FloatingPassageLoaded", "true");
        this.getLogger().info(TextFormat.YELLOW + "FloatingPassage has been enabled");
        instance = this;

        getServer().getCommandMap().register("floatingpassage", new FloatingPassageCmd(this));
    }

}
