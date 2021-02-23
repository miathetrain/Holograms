package net.holograms.task;

import cn.nukkit.scheduler.PluginTask;
import net.holograms.Holograms;
import net.holograms.HologramManager;
import net.holograms.object.CraftHologram;
import net.holograms.object.CraftHologramLine;

public class HologramUpdateTask extends PluginTask<Holograms> {
    private Holograms plugin;

    public HologramUpdateTask(Holograms plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void onRun(int i) {
        long time = System.currentTimeMillis();
        for (CraftHologram hologram : HologramManager.getInstance().getHolograms().values()) {
            hologram.sendLines();
            for (CraftHologramLine line : hologram.getLines()) {
                if (!line.getDisabled() ) {
                    line.updateLines();
                }
            }
        }
    }
}
