package net.holograms.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import net.holograms.HologramManager;
import net.holograms.object.CraftHologram;

public class MainListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (CraftHologram hologram : HologramManager.getInstance().getHolograms().values()) {
            hologram.sendLines();
        }
    }
}
