package net.holograms.object;


import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created on 2018/01/7 by nora.
 **/

public class CraftHologram {

    // The entities stored within each Hologram
    private final List<CraftHologramLine> lines;
    // Name
    private String name;
    // Position Variables
    private Level level;
    private double x, y, z;
    private int chunkX, chunkZ;
    private boolean allowPlaceholders, deleted;
    private boolean custom = false;
    private ArrayList<UUID> viewers = new ArrayList<>();


    public CraftHologram(Location location, String name) {
        updateLocation(location.level, location.x, location.y, location.z);
        this.name = name;
        lines = new ArrayList<>();
        //addLine("This is an Example Line (Remove using /fp removeline 1)");
    }

    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public Float getX() {
        return (float) x;
    }

    public Float getY() {
        return (float) y;
    }

    public Float getZ() {
        return (float) z;
    }

    public List<CraftHologramLine> getLines() {
        return lines;
    }

    public void updateLocation(Level level, double x, double y, double z) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = (int) x;
        this.chunkZ = (int) z;
    }

    public Integer addLine(String text) {
        int index = countLines();
        lines.add(new CraftHologramLine(this, text, index));
        sendLines();
        return index;
    }

    public void setLine(int index, String text) {
        for (CraftHologramLine line : lines)
            if (line.getIndex() == index) {
                line.setText(text);
            }
//        sendLines();
    }

    public void updateLines() {
        int index = 1;
        if (!this.deleted) {
            for (CraftHologramLine line : lines) {
                if (!line.getDisabled() && line.getUpdate()) {
                    line.setIndex(index);
                    index++;
                    line.sendLine(this.level.getPlayers().values());
                }
            }
        }
    }

    public void sendLines() {
        this.level.getPlayers().values().forEach(this::sendLines);
    }

    public void sendLines(Player player) {
        int index = 1;
        if (!this.deleted) {
            if (!this.viewers.contains(player.getUniqueId())) {
                if (player.distance(new Vector3(x, y, z)) <= 13) {
                    for (CraftHologramLine line : lines) {
                        if (!line.getDisabled()) {
                            line.setIndex(index);
                            index++;
                            line.sendLine(Collections.singleton(player));
                            viewers.add(player.getUniqueId());
                        }
                    }
                }
            } else {
                if (player.distance(new Vector3(x, y, z)) >= 14) {
                    for (CraftHologramLine line : lines) {
                        if (!line.getDisabled()) {
                            player.dataPacket(line.removeLine());
                            viewers.remove(player.getUniqueId());
                        }
                    }
                }
            }
        }
    }

    public void setDeleted() {
        setDeleted(true);
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
        for (CraftHologramLine line : lines) {
            for (Player player : this.level.getPlayers().values())
                player.dataPacket(line.removeLine());
        }
    }

    public void deleteLine(String arg) {
        int index = Integer.parseInt(arg);
        for (CraftHologramLine line : lines)
            if (line.getIndex() == index) {
                for (Player player : this.level.getPlayers().values()) {
                    player.dataPacket(line.delLine());
                }
                line.setDisabled(true);
            }
        reindexLines();
        sendLines();
    }

    private int countLines() {
        int index = 1;
        for (CraftHologramLine line : lines) {
            if (!line.getDisabled()) {
                index++;
            }
        }
        return index;
    }

    private void reindexLines() {
        int index = 1;
        for (CraftHologramLine line : lines) {
            if (!line.getDisabled()) {
                line.setIndex(index);
                index++;
            } else {
                line.setIndex(-1);
            }
        }
    }

    public void setCustom() {
        custom = true;
    }

    public boolean getCustom() {
        return custom;
    }
}