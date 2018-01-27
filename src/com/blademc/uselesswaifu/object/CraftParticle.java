package com.blademc.uselesswaifu.object;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/01/7 by nora.
**/

public class CraftParticle{


    // Name
    private String name;
    // Position Variables
    private Level level;
    private double x,y,z;
    private int chunkX,chunkZ;

    // The entities stored within each Hologram
    private final List<CraftParticleLine> lines;

    private boolean allowPlaceholders,deleted;


    public CraftParticle(Location location, String name) {
        updateLocation(location.level, location.x, location.y, location.z);
        this.name = name;
        lines = new ArrayList<>();
        addLine("This is an Example Line (Remove using /fp removeline 1)");
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
        int index = countLines();
        lines.add(new CraftParticleLine(this, text, index));
        sendLines();
        return index;
    }

    private void sendLines() {
        int index = 1;
        if(!this.deleted) {
            for (CraftParticleLine line : lines) {
                if (!line.getDisabled()) {
                    line.setIndex(index);
                    index++;
                    for (Player player : this.level.getPlayers().values())
                        player.dataPacket(line.sendLine());
                }
            }
        }
    }

    public void setDeleted(){
        setDeleted(true);
    }

    public void setDeleted(Boolean deleted){
        this.deleted = deleted;
            for (CraftParticleLine line : lines) {
                for (Player player : this.level.getPlayers().values())
                    player.dataPacket(line.removeLine());
            }
    }

    public Boolean getDeleted(){
        return this.deleted;
    }


    public void delLine(String arg) {
        int index = Integer.parseInt(arg);
        for (CraftParticleLine line : lines)
            if(line.getIndex() == index) {
                for (Player player : this.level.getPlayers().values()) {
                    player.dataPacket(line.delLine());
                }
                line.setDisabled(true);
        }
        reindexLines();
        sendLines();
    }

    private int countLines(){
        int index = 1;
        for(CraftParticleLine line : lines){
            if (!line.getDisabled()) {
                index++;
            }
        }
        return index;
    }

    private void reindexLines() {
        int index = 1;
        for(CraftParticleLine line : lines){
            if (!line.getDisabled()) {
                line.setIndex(index);
                index++;
            }
            else{
                line.setIndex(-1);
            }
        }
    }
}