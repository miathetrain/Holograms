package com.blademc.uselesswaifu.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import com.blademc.uselesswaifu.HologramManager;
import com.blademc.uselesswaifu.object.CraftParticle;

public class MainListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (CraftParticle hologram : HologramManager.getInstance().getHolograms().values()) {
            hologram.sendLines();
        }
    }
}
