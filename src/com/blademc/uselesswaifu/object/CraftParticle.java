package com.blademc.uselesswaifu.object;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/01/7 by nora.
**/

public class CraftParticle{


    // Position Variables
    private Level level;
    private double x,y,z;
    private int chunkX,chunkZ;

    // The entities stored within each Hologram
    private final List<CraftParticleLine> lines;

    private boolean allowPlaceholders,deleted;


    public CraftParticle(Location location) {
        updateLocation(location.level, location.x, location.y, location.z);
        lines = new ArrayList<>();
    }

    public Level getLevel(){
        return level;
    }

    public Float getX(){
        return (float) x;
    }

    public Float getY(){
        return (float) y;
    }

    public Float getZ(){
        return (float) z;
    }

    private void updateLocation(Level level, double x, double y, double z) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = (int) x;
        this.chunkZ = (int) z;
    }

    public Integer addLine(String text){
        int index = lines.size() +1;
        lines.add(new CraftParticleLine(this, text, index));
        sendLines();
        return index;
    }

    private void sendLines() {
        int index = 1;
            for (CraftParticleLine line : lines) {
                if (line.getDisabled() == false) {
                    line.setIndex(index);
                    index++;
                    for (Player player : this.level.getPlayers().values())
                        player.dataPacket(line.sendLine());
                }
            }
    }


    public void delLine(String arg) {
        int index = Integer.parseInt(arg);
        for (CraftParticleLine line : lines)
            if(line.getIndex() == index) {
                for (Player player : this.level.getPlayers().values())
                    player.dataPacket( line.delLine());
            line.setDisabled(true);
            }
        sendLines();
        }
}