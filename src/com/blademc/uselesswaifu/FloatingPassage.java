package com.blademc.uselesswaifu;

import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.blademc.uselesswaifu.command.FloatingPassageCmd;
import com.blademc.uselesswaifu.expansion.PlayerExpansion;
import com.blademc.uselesswaifu.listener.MainListener;
import com.blademc.uselesswaifu.object.CraftParticle;
import com.blademc.uselesswaifu.placeholder.PlaceholderAPI;

import java.io.File;
import java.util.Map;

public class FloatingPassage extends PluginBase {

    // The main instance of the plugin.
    private static FloatingPassage instance;

    // The main listener for all events.
    private static MainListener listener;

    public static FloatingPassage getInstance() {
        return instance;
    }

    @Override
    public void onEnable(){

        // Warn players about using /reload command.
        if(instance != null || System.getProperty("FloatingPassageLoaded") != null){
            this.getLogger().error("Please do not use /reload or plugin reloaders. You will receive no support for this operation");
        }

        System.setProperty("FloatingPassageLoaded", "true");
        this.getLogger().info(TextFormat.YELLOW + "FloatingPassage has been enabled");
        instance = this;

        PlaceholderAPI.registerPlaceholderHook(this, new PlayerExpansion());

        getServer().getCommandMap().register("floatingpassage", new FloatingPassageCmd(this));

        for(Command command : this.getServer().getCommandMap().getCommands().values())
        {
            command.setPermissionMessage("You cannot do that sir!");
        }
        double number = - Math.PI / 2;
    }

    @Override
    public void onDisable(){

    }

}
