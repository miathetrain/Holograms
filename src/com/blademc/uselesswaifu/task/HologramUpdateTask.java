package com.blademc.uselesswaifu.task;

import cn.nukkit.scheduler.PluginTask;
import com.blademc.uselesswaifu.FloatingPassage;
import com.blademc.uselesswaifu.HologramManager;
import com.blademc.uselesswaifu.object.CraftParticle;
import com.blademc.uselesswaifu.object.CraftParticleLine;

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
