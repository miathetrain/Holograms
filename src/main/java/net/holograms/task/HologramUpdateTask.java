package net.holograms.task;

import cn.nukkit.scheduler.PluginTask;
import net.holograms.FloatingPassage;
import net.holograms.HologramManager;
import net.holograms.object.CraftParticle;
import net.holograms.object.CraftParticleLine;

public class HologramUpdateTask extends PluginTask<FloatingPassage> {
    private FloatingPassage plugin;

    public HologramUpdateTask(FloatingPassage plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void onRun(int i) {
        long time = System.currentTimeMillis();
        for (CraftParticle hologram : HologramManager.getInstance().getHolograms().values()) {
            for (CraftParticleLine line : hologram.getLines()) {
                if (!line.getDisabled() && time > line.getLastUpdateTime() + line.getDelay()) {
                    line.updateLines();
                }
            }
        }
    }
}
